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
	 * 把参数打印成为日志格式
	 * @param id - 用户名
	 * @param o - 消息
	 * @param ip - 客户端ip
	 */
	public static void log(Object id, Object msg, Object ip) {
		String f = new Date().toLocaleString() +" "+ ip+ " ["+id+"] "+msg;
		pl(f);
	}
	
	
	/** 输出一条错误消息 */
	public static void error(Exception e) {
		e.printStackTrace();
	}
	
	/** 用对话框显示消息 */
	public static void show(Object o) {
		javax.swing.JOptionPane.showMessageDialog(null, o.toString());
	}
	
	/** 返回服务器地址 */
	public static InetAddress getServerAddress() {
		try {
			return InetAddress.getByName(SystemConfig.get(SystemConfig.SERVERIP));
		} catch (IOException e) {
			error(e);
		}
		return null;
	}
	
	/** 返回服务器端口 */
	public static int getServerPort() {
		try {
			return Integer.parseInt(SystemConfig.get(SystemConfig.SERVERPORT));
		} catch (Exception e) {
			error(e);
		}
		return 5000;
	}
	
	/** 返回32字节掩码 */
	public static String getCodes() {
		return CodexMaker.getCodes();
	}
	
	/** 返回掩码的固定长度 */
	public static int getCodesLength() {
		return CodexMaker.getCodesLength();
	}
	
	/** 返回本地端口 */
	public static int getLocalPort() {	
		try {
			return Integer.parseInt(SystemConfig.get(SystemConfig.CLIENTPORT));
		} catch (Exception e) {
			error(e);
		}
		return 6000;
	}
}
