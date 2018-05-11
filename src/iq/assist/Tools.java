package iq.assist;

import iq.event.MsgPackage;
import iq.server.queue.UserEntity;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import javax.xml.crypto.Data;

public final class Tools {
	
	public static void p(Object o) {
		System.out.print(o);
	}
	
	/** System.out.println(o); */
	public static void pl(Object o) {
		System.out.println(o);
	}
	
	/**
	 * �Ѳ�����ӡ��Ϊ��־��ʽ
	 * @param id - �û���
	 * @param o - ��Ϣ
	 * @param ip - �ͻ���ip
	 */
	public static void log(Object id, Object msg, Object ip) {
		String f = new Date().toLocaleString() +" "+ ip+ " ["+id+"] "+msg;
		pl(f);
	}
	
	
	/** ���һ��������Ϣ */
	public static void error(Exception e) {
		e.printStackTrace();
	}
	
	/** �öԻ�����ʾ��Ϣ */
	public static void show(Object o) {
		javax.swing.JOptionPane.showMessageDialog(null, o.toString());
	}
	
	/** ���ط�������ַ */
	public static InetAddress getServerAddress() {
		try {
			return InetAddress.getByName(SystemConfig.get(SystemConfig.SERVERIP));
		} catch (IOException e) {
			error(e);
		}
		return null;
	}
	
	/** ���ط������˿� */
	public static int getServerPort() {
		try {
			return Integer.parseInt(SystemConfig.get(SystemConfig.SERVERPORT));
		} catch (Exception e) {
			error(e);
		}
		return 5000;
	}
	
	/** ����32�ֽ����� */
	public static String getCodes() {
		return CodexMaker.getCodes();
	}
	
	/** ��������Ĺ̶����� */
	public static int getCodesLength() {
		return CodexMaker.getCodesLength();
	}
	
	/** ���ر��ض˿� */
	public static int getLocalPort() {	
		try {
			return Integer.parseInt(SystemConfig.get(SystemConfig.CLIENTPORT));
		} catch (Exception e) {
			error(e);
		}
		return 6000;
	}
}
