// CatfoOD 2008-6-7 上午08:46:36

package iq.server.queue;

import iq.assist.CodexMaker;
import iq.assist.MessageThread;
import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.sql.SqlFactory;

import java.net.SocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Exception.LogonException;

public class UserEntity {
	private FriendList flist;
	private String id;
	//private String ps;
	private UserInfo info;
	private final SocketAddress socket;
	private boolean running=true;
	
	// 监测用户在线的间隙时间
	public final int CHECKTIME = 30*1000;
	
	/**
	 * 	 检查相关的条件 是否允许建立
	 *	 不允许应该跑出异常
	 */
	public UserEntity(String id, String ps, SocketAddress s) throws LogonException {
		socket = s;
		this.id = id;

		// 检查是否在线
		if (OnLineQueue.getInstance().isOnline(this)) {
			throw new LogonException("用户已经登陆");
		}
		
		// 检查数据库中用户名和密码
		checkUser(id, ps);
		
		// 初始化用户资料
		info = new UserInfo(this);
		// 初始化好友列表
		flist = new FriendList(this);

		startOnlineCheck();
	}
	
	/**
	 * 检查用户名和密码是否正确
	 * @throws LogonException - 如果错误抛出这个异常
	 */
	private void checkUser(String id, String ps) throws LogonException {
		// 检查数据库中用户名和密码
		ResultSet c = null;
		try {
			c = SqlFactory.getInstance().executeQuery(
					"select id,password from user_table where id="+id+
					" and password='"+ps+"'");
			
			if (c.next()) {
				if (! (c.getString(1).equals(id) && c.getString(2).equals(ps)) ) {
					throw new SQLException();
				}
			} else {
				throw new SQLException();
			}
			
		} catch (SQLException e) {
			throw new LogonException("iQ号或密码错误");
			
		} finally {
			if (c!=null) {
				try {
					c.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	private void startOnlineCheck() {
		new Thread() {
			public void run() {
				while (running) {
					try {
						sleep(CHECKTIME);
					} catch (InterruptedException e) {}
					new checkOnlineThread();
				}
			}
		}.start();
	}
	
	/** 返回用户id */
	public String getUserID() {
		return id;
	}
	
//	/** 返回用户密码 */
//	private String getUserPS() {
//		return ps;
//	}
	
	/** 返回与客户绑定的socket */
	public SocketAddress getSocketAddress() {
		return socket;
	}
	
	/** 更新用户资料 */
	public boolean updataInfo(String newinfo) {
		return info.updataInfo(newinfo, this);
	}
	
	/** 取得用户的资料 */
	public String getInfomation() {
		return info.getInformation();
	}
	
	/** 取得好友列表的String格式 */
	public String getFriendList() {
		return flist.getFriendList();
	}
	
	/** 删除好友,自动删除对方好友列表 */
	public void delFriend(String fir) {delFriend(fir, true);}
	
	/** 删除好友 */
	public void delFriend(String fir, boolean trade) {
		if (trade) {
			UserEntity fuser = OnLineQueue.getInstance().getUserEntity(fir);
			if (fuser!=null) {
				fuser.delFriend(this.getUserID(), false);
			} else {
				FriendList.delOffLineFriend(fir, this.getUserID());
			}
		}
		flist.delFriend(fir);
	}
	
	/** 邀请好友 */
	public void inviteFriend(String fid, String info) {
		UserEntity fuser = OnLineQueue.getInstance().getUserEntity(fid);
		if (fuser!=null) {
			new InviteFriendThread(fuser, info);
		}
	}
	
	/** 添加好友，自动添加对方的列表 */
	public void addFriend(UserEntity fid) {
		flist.addFriend(fid);
	}
	
	// 邀请好友的线程
	private class InviteFriendThread extends MessageThread {
		private UserEntity fuser;
		private String code = CodexMaker.getCodes();
		// 30分钟超时
		private final int FTIMEOUT = 1000*60*30;
		
		public InviteFriendThread(UserEntity fuser, String info) {
			this.fuser = fuser;
			start(FTIMEOUT);
			String sysinfo = id+ " 请求你加为好友.";
			
			send("$FRI.$REQU"+code+".$INFO"+info+
					".$SYS"+sysinfo, fuser.getSocketAddress());
		}
		
		public void complete(MsgPackage msgpack) {
			String msgs = msgpack.getMsg();
			String info = StringTool.getSign(msgs, "$INFO");
			
			if (StringTool.haveSign(msgs, "$OK")) {
				send("$FRI.$FID"+fuser.getUserID()+
						".$OK"+".$INFO"+info+".$SYS"+fuser.id+" 好友添加成功", socket);
				
				addFriend(fuser);
				fuser.addFriend(UserEntity.this);			
			} 
			else if (StringTool.haveSign(msgs, "$NO")) {
				send("$FRI.$FID"+fuser.getUserID()+
						".$NO"+".$INFO"+info+".$SYS"+fuser.id+" 好友添加失败", socket);
			}
		}

		public void overTime() {
			// do nothing of 对方不响应
		}

		public String getCodex() {
			return "$FRI.$RESP"+code;
		}
	}
	
	// 在线监测线程
	private class checkOnlineThread extends MessageThread {
		private String codex;
		private final int waittime = 1000* 10;
		
		public checkOnlineThread() {
			codex = CodexMaker.getCodes();
			start(waittime);
			MsgFactory.send(codex, socket);
		}
		
		public void complete(MsgPackage msgpack) {
			// do nothing
		}

		public void overTime() {
			// 超时说明用户已经离线
			logout();
		}

		public String getCodex() {
			return "$ASK."+codex;
		}
	}
	
	/**
	 * 离线
	 */
	public void logout() {
		running = false;
		OnLineQueue.getInstance().removeUserEntity(UserEntity.this);
	}
	
	/**
	 * 比较两个UserEntity是否相等的条件，
	 * 如果用户ID相等则返回true
	 */
	public boolean equals(Object o) {
		if (o==this) return true;
		if ( id.equals(o.toString()) ) return true;
		return false;
	}
	
	public String toString() {
		return id;
	}
}
