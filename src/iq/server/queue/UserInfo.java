// CatfoOD 2008-6-7 上午09:17:36

package iq.server.queue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.sql.SqlFactory;

/**
 * 用户资料类
 * 负责初始化用户的相关资料
 */
public class UserInfo {
	/**
	 * info的内部数据格式:
	 * [每行一个数据]
	 * <属性名字>=<内容>\n
	 */	
	private Map<String, String> infos = 
					new HashMap<String, String>();
	
	private String info;
	
	public UserInfo(UserEntity user) {
		info = getUserInformation(user.getUserID());
		if (info!=null) {
			stringToMap(info);
		}
	}
	
	/**
	 * 返回指定项的数据
	 * @param key
	 * @return String - 属性的值
	 */
	public String get(String key) {
		return infos.get(key);
	}
	
	/**
	 * 更新用户资料
	 * @param newinfo - 新的用户资料
	 */
	public boolean updataInfo(String newinfo, UserEntity user) {
		newinfo = StringTool.formatInfo(newinfo);
		String sent = "update info_table " +
				"set info='"+newinfo+"' where uid="+user.getUserID();
		try {
			SqlFactory.getInstance().executeUpdate(sent);
			
			info = newinfo;
			stringToMap(info);
			return true;
		} catch (SQLException e) {
			Tools.pl("更新用户资料出错."+e);
			return false;
		}
	}
	
	/**
	 * 取得用户的资料
	 */
	public String getInformation() {
		return info;
	}
	
	private final void stringToMap(String info) {
		String[] s1 = info.split("[\n]");
		for (int i=0; i<s1.length; ++i) {
			String[] s2 = s1[i].split("=", 2);
			if (s2.length==2) {
				infos.put(s2[0], s2[1]);
			}
		}
	}
	
	/**
	 * 返回用户的详细资料(离线)
	 * @return - String找不到或出错则返回null
	 */
	public static String getUserInformation(String uid) {
		try {
			Statement sql = 
				SqlFactory.getInstance().getConnection().createStatement();
			ResultSet set = sql.executeQuery(
					"select info from info_table where uid="+uid);
			if (set.next()) {
				return set.getString(1);
			}
			set.close();
		} catch (SQLException e) {
			Tools.pl("初始化用户信息出错:"+e);
//			e.printStackTrace();
		}
		return null;
	}
}
