package iq.sql;

import iq.assist.SystemConfig;
import iq.assist.Tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SqlFactory {
	private static SqlFactory instance = new SqlFactory();
	
	public static SqlFactory getInstance() {
		return instance;
	}
	
	public static void init() throws SQLException {
		if (null==instance.getConnection()) {
			throw new SQLException(instance.errorBuff.toString());
		} else {
			instance.errorBuff = new StringBuffer();
			instance.executeUpdate("use "+SystemConfig.get("databasename"));
		}
	}
	// ------------------------------------------------------------
	
	private Connection con;
	private Statement statement;
	private StringBuffer errorBuff = new StringBuffer();
	
	private ISqlConnect[] isc = new ISqlConnect[]{
		new SqlServerMicrosoftDriver(),
		new OdbcBridgeDriver(),
	};
	
	private int useconnect = -1;

	private SqlFactory() {
		String user = "sa";
		String pass = "";
		
		for (int i=0; i<isc.length; ++i) {
			try {
				isc[i].registerDriver();
				con = isc[i].creatConnect(user, pass);
				statement = con.createStatement();
				useconnect = i;
				break;
			} catch(Exception e) {
				errorBuff.append("\n["+
						isc[i].getName()+"]:\n "+e.getMessage()+"\n");
			}
		}
	}
	
	/**
	 * 重新连接数据库，直到成功连接
	 * 这个方法会阻塞所有操作
	 */
	public final synchronized void reLink() {
		String user = "sa";
		String pass = "";
		boolean linked = false;
		
		try {
			con.createStatement();
		} catch(Exception e) {
			while (linked) {
				for (int i=0; i<isc.length; ++i) {
					try {
						isc[i].registerDriver();
						con = isc[i].creatConnect(user, pass);
						statement = con.createStatement();
						useconnect = i;
						linked = true;
						break;
					} catch(Exception ei) {
						Tools.pl(ei.getMessage());
					}
				}
			}
		}
	}
	
	public String getConnectDriverName() {
		return isc[useconnect].getName();
	}
	
	public void useDB(String dbname) throws SQLException {
		statement.execute("use "+dbname);
	}
	
	public Statement getStatement() {
		return statement;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public ResultSet executeQuery(String sql) throws SQLException {
		return statement.executeQuery(sql);
	}
	
	public int executeUpdate(String sql) throws SQLException {
		return statement.executeUpdate(sql);
	}
}
