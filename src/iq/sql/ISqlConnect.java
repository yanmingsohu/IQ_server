package iq.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ISqlConnect {
	/**
	 * ע�����ݿ���������һ����Class.forName();
	 * ������׳��쳣����˵��ע��ɹ�
	 * @throws ClassNotFoundException - ����Ҳ������������׳�����쳣
	 */
	public void registerDriver() 
	throws ClassNotFoundException;
	
	/**
	 * �������ݿ����ӣ�"jdbc:sqlserver:xxx"��һ��������
	 * @param user - ��½���ݿ���û���
	 * @param pass - ��½���ݿ������
	 * @throws SQLException - ������Ӵ����׳�һ���쳣
	 */
	public Connection creatConnect(String user, String pass) 
	throws SQLException;
	
	/**
	 * �������ӵ������磺"Jdbc-Odbc �Ž���������(Sun)"
	 */
	public String getName();
}
