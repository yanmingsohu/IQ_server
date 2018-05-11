package iq.sql;

import iq.assist.SystemConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OdbcBridgeDriver implements ISqlConnect {
	private String srcname; 
	
	/**
	 * @param name - ����Դ������
	 */
	public OdbcBridgeDriver() {
		srcname = SystemConfig.get("odbcsrcname");
	}
	
	public Connection creatConnect(String user, String pass)
			throws SQLException {
		String connectionUrl = "jdbc:odbc:" + srcname;
		return DriverManager.getConnection(connectionUrl, user, pass);
	}

	public String getName() {
		return "Odbc-Jdbc �Ž���������(Sun)";
	}

	public void registerDriver() throws ClassNotFoundException {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	}

}
