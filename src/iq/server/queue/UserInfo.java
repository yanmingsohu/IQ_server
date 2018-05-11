// CatfoOD 2008-6-7 ����09:17:36

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
 * �û�������
 * �����ʼ���û����������
 */
public class UserInfo {
	/**
	 * info���ڲ����ݸ�ʽ:
	 * [ÿ��һ������]
	 * <��������>=<����>\n
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
	 * ����ָ���������
	 * @param key
	 * @return String - ���Ե�ֵ
	 */
	public String get(String key) {
		return infos.get(key);
	}
	
	/**
	 * �����û�����
	 * @param newinfo - �µ��û�����
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
			Tools.pl("�����û����ϳ���."+e);
			return false;
		}
	}
	
	/**
	 * ȡ���û�������
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
	 * �����û�����ϸ����(����)
	 * @return - String�Ҳ���������򷵻�null
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
			Tools.pl("��ʼ���û���Ϣ����:"+e);
//			e.printStackTrace();
		}
		return null;
	}
}
