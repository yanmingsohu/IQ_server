package iq.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ISqlConnect {
	/**
	 * 注册数据库驱动程序，一般是Class.forName();
	 * 如果不抛出异常，就说明注册成功
	 * @throws ClassNotFoundException - 如果找不到驱动程序抛出这个异常
	 */
	public void registerDriver() 
	throws ClassNotFoundException;
	
	/**
	 * 建立数据库连接，"jdbc:sqlserver:xxx"是一个连接名
	 * @param user - 登陆数据库的用户名
	 * @param pass - 登陆数据库的密码
	 * @throws SQLException - 如果连接错误抛出一个异常
	 */
	public Connection creatConnect(String user, String pass) 
	throws SQLException;
	
	/**
	 * 描述连接的类型如："Jdbc-Odbc 桥接驱动程序(Sun)"
	 */
	public String getName();
}
