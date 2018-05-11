package iq.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.sql.SqlFactory;

public class RegisterServer implements InetListener {
	private Statement sql;
	
	public RegisterServer() throws SQLException {
		 sql = SqlFactory.getInstance().getConnection().createStatement();
	}

	public String getCodex() {
		return "$REG";
	}

	public void information(Object o) {
		MsgPackage msg = (MsgPackage)o;
		String name = StringTool.getSign(msg.getMsg(), "$N");
		
		try {
			if (isAsoRegiste(name)) {
				send("$REG.$NO'"+name+"'�û��Ѿ�����", msg);
				
			} else {
				String pass = StringTool.getSign(msg.getMsg(), "$P");
				
				if (pass.length()>4) {					
					sql.executeUpdate("insert into  user_table " +
							"(name,password) values ('"+name+"','"+pass+"')");
					ResultSet set = sql.executeQuery("select id " +
							"from user_table where name='"+name+"'");
					
					if (set.next()) {
						int id = set.getInt(1);
						String info = StringTool.getSign(msg.getMsg(), "$INFO");
						info = StringTool.formatInfo(info);
						
						sql.executeUpdate("insert into info_table " +
								"(uid,info) values ("+id+",'"+info+"')");
						sql.executeUpdate("insert into friend_table" +
								"(uid,list) values ("+id+", '')");
						
						send("$REG.$OK.$ID"+id, msg);
						Tools.pl("user reg:["+name+"]"+id);
						//set.close();
					}
				} else {
					send("$RGE.$NO����������5λ", msg);
				}
				Tools.log("", this, msg.toString());
			}
		}catch(Exception e) {
			send("$RGE.$NO����������"+e, msg);
			e.printStackTrace();
		}
	}
	
	private boolean isAsoRegiste(String name) {
	 	String sqlm = "select name from user_table where name=";
	 	
		try {
			ResultSet set = sql.executeQuery(sqlm+"'"+name+"'");
			if (set.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// �����緢����Ϣ
	private void send(String msg, MsgPackage m) {
		MsgFactory.send(msg, m);
	}
	
	public String toString() {
		return "ע�������";
	}
}
