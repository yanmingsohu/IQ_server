package iq.sql;
// CatfoOD 2008.5.2

import iq.assist.SystemConfig;

import java.sql.*;


public class SqlServerMicrosoftDriver implements ISqlConnect {
	private String dbservername;
	
	/**
	 * @param name - 远程SqlServer数据库的名字
	 */
	public SqlServerMicrosoftDriver() {
		dbservername =  SystemConfig.get("sqlserver");
	}
	
	public String getName() {
		return "SqlServer2005 jdbc驱动程序(Miscrosoft)";
	}

	public void registerDriver() throws ClassNotFoundException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	}

	public Connection creatConnect(String user, String pass) throws SQLException {
		String connectionUrl = "jdbc:sqlserver:" + dbservername;
		return DriverManager.getConnection(connectionUrl, user, pass);
	}
}
