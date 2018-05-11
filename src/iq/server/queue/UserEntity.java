// CatfoOD 2008-6-7 ����08:46:36

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
	
	// ����û����ߵļ�϶ʱ��
	public final int CHECKTIME = 30*1000;
	
	/**
	 * 	 �����ص����� �Ƿ�������
	 *	 ������Ӧ���ܳ��쳣
	 */
	public UserEntity(String id, String ps, SocketAddress s) throws LogonException {
		socket = s;
		this.id = id;

		// ����Ƿ�����
		if (OnLineQueue.getInstance().isOnline(this)) {
			throw new LogonException("�û��Ѿ���½");
		}
		
		// ������ݿ����û���������
		checkUser(id, ps);
		
		// ��ʼ���û�����
		info = new UserInfo(this);
		// ��ʼ�������б�
		flist = new FriendList(this);

		startOnlineCheck();
	}
	
	/**
	 * ����û����������Ƿ���ȷ
	 * @throws LogonException - ��������׳�����쳣
	 */
	private void checkUser(String id, String ps) throws LogonException {
		// ������ݿ����û���������
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
			throw new LogonException("iQ�Ż��������");
			
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
	
	/** �����û�id */
	public String getUserID() {
		return id;
	}
	
//	/** �����û����� */
//	private String getUserPS() {
//		return ps;
//	}
	
	/** ������ͻ��󶨵�socket */
	public SocketAddress getSocketAddress() {
		return socket;
	}
	
	/** �����û����� */
	public boolean updataInfo(String newinfo) {
		return info.updataInfo(newinfo, this);
	}
	
	/** ȡ���û������� */
	public String getInfomation() {
		return info.getInformation();
	}
	
	/** ȡ�ú����б��String��ʽ */
	public String getFriendList() {
		return flist.getFriendList();
	}
	
	/** ɾ������,�Զ�ɾ���Է������б� */
	public void delFriend(String fir) {delFriend(fir, true);}
	
	/** ɾ������ */
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
	
	/** ������� */
	public void inviteFriend(String fid, String info) {
		UserEntity fuser = OnLineQueue.getInstance().getUserEntity(fid);
		if (fuser!=null) {
			new InviteFriendThread(fuser, info);
		}
	}
	
	/** ��Ӻ��ѣ��Զ���ӶԷ����б� */
	public void addFriend(UserEntity fid) {
		flist.addFriend(fid);
	}
	
	// ������ѵ��߳�
	private class InviteFriendThread extends MessageThread {
		private UserEntity fuser;
		private String code = CodexMaker.getCodes();
		// 30���ӳ�ʱ
		private final int FTIMEOUT = 1000*60*30;
		
		public InviteFriendThread(UserEntity fuser, String info) {
			this.fuser = fuser;
			start(FTIMEOUT);
			String sysinfo = id+ " �������Ϊ����.";
			
			send("$FRI.$REQU"+code+".$INFO"+info+
					".$SYS"+sysinfo, fuser.getSocketAddress());
		}
		
		public void complete(MsgPackage msgpack) {
			String msgs = msgpack.getMsg();
			String info = StringTool.getSign(msgs, "$INFO");
			
			if (StringTool.haveSign(msgs, "$OK")) {
				send("$FRI.$FID"+fuser.getUserID()+
						".$OK"+".$INFO"+info+".$SYS"+fuser.id+" ������ӳɹ�", socket);
				
				addFriend(fuser);
				fuser.addFriend(UserEntity.this);			
			} 
			else if (StringTool.haveSign(msgs, "$NO")) {
				send("$FRI.$FID"+fuser.getUserID()+
						".$NO"+".$INFO"+info+".$SYS"+fuser.id+" �������ʧ��", socket);
			}
		}

		public void overTime() {
			// do nothing of �Է�����Ӧ
		}

		public String getCodex() {
			return "$FRI.$RESP"+code;
		}
	}
	
	// ���߼���߳�
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
			// ��ʱ˵���û��Ѿ�����
			logout();
		}

		public String getCodex() {
			return "$ASK."+codex;
		}
	}
	
	/**
	 * ����
	 */
	public void logout() {
		running = false;
		OnLineQueue.getInstance().removeUserEntity(UserEntity.this);
	}
	
	/**
	 * �Ƚ�����UserEntity�Ƿ���ȵ�������
	 * ����û�ID����򷵻�true
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
