package iq.sql;

import iq.assist.SystemConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OdbcBridgeDriver implements ISqlConnect {
	private String srcname; 
	
	/**
	 * @param name - 数据源的名字
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
		return "Odbc-Jdbc 桥接驱动程序(Sun)";
	}

	public void registerDriver() throws ClassNotFoundException {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	}

}
