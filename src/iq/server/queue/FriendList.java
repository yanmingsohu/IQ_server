// CatfoOD 2008-6-7 上午08:50:45

package iq.server.queue;

import iq.assist.Tools;
import iq.sql.SqlFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 好友列表负责：
 * . 更新数据库相关字段
 * . 与对方建立添加好友的过程
 */
public class FriendList {
	private ArrayList<String> list;
	private Statement sql;
	private UserEntity user;
	
	public FriendList(UserEntity info) {
		list = new ArrayList<String>();
		user = info;
		// 到数据库中查找好友数据,并初始化他
		try {
			sql = SqlFactory.getInstance().getConnection().createStatement();
			ResultSet set =sql.executeQuery("select list " +
					"from friend_table where uid="+info.getUserID());
			
			if (set.next()) {
				String[] list_s = set.getString(1).split(":");
				for (int i=0; i<list_s.length; ++i) {
					if (list_s[i].length()>0) {
						list.add(list_s[i]);
					}
				}
			}
			set.close();
		} catch (SQLException e) {
			Tools.pl("初始化好友列表出错."+e);
		}
	}
	
	public boolean isFriend(UserEntity fid) {
		return isFriend(fid.getUserID());
	}
	
	public boolean isFriend(String fid) {
		return list.contains(fid);
	}
	
	public boolean addFriend(UserEntity fid) {
		boolean complete = 
			list.contains(fid.getUserID())? false: list.add(fid.getUserID());
		// 策略：是否应该自动发出用户列表
		if (complete) {
			updataSql();
		}
		return complete;
	}
	
	public boolean delFriend(String fid) {
		boolean complete = list.remove(fid);
		if (complete) {
			updataSql();
		}
		return complete;
	}
	
	/**
	 * 更新数据库
	 */
	public void updataSql() {
		String buff =listToString();
		try {
			sql.executeUpdate("update friend_table set list=" +
					"'"+buff.toString()+"' where uid="+user.getUserID());
		} catch (SQLException e) {
			Tools.pl("更新好友列表出错."+e);
		}
	}
	
	/**
	 * 返回用户列表的String格式
	 */
	public String getFriendList() {
		return listToString();
	}
	
	private String listToString() {
		StringBuffer buff = new StringBuffer();
		buff.append(":");
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			buff.append( it.next()+':' );
		}
		return buff.toString();
	}
	
	/**
	 * 静态删除用户uid的好友fid
	 */
	protected static void delOffLineFriend(String uid, String fid) {
		try {
			Statement sql = 
				SqlFactory.getInstance().getConnection().createStatement();
			ResultSet set = 
				sql.executeQuery("select list from friend_table where uid="+uid);
			
			if (set.next()) {
				String result = set.getString(1);
				if (result!=null) {
					String s = result.replaceFirst(":"+fid+":", ":");
					if (!s.equals(result)) {
						sql.executeUpdate("update friend_table " +
								"set list='"+s+"' where uid="+uid);
					}
				}
			}
			set.close();
		} catch (SQLException e) {
			Tools.pl("离线用户好友删除失败."+e);
		}
	}
}
