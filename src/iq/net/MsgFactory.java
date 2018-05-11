package iq.net;

import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.event.RecverManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class MsgFactory {
	private MsgFactory(){}
	
	/**
	 * 初始化消息工厂
	 */
	public static void init() throws IOException {
		getMsgSender();
		creatMsgRecver(Tools.getServerPort(), getRecverManager());
	}
	
	private static MsgSender send = null;
	private static MsgRecver recv = null;
	private static RecverManager remanager = null;
	
	/**
	 * 返回发送器
	 * @return MsgSender
	 * @throws IOException 
	 * @throws IOException - 如果不能建立网络连接，跑出这个异常
	 */
	public static MsgSender getMsgSender() throws IOException {
		if (send==null) {
			send = new MsgSender();
		}
		return send;
	}	
	
	/**
	 * 返回消息管理器
	 */
	public static RecverManager getRecverManager() {		
		if (remanager==null) {
			remanager = new RecverManager();
		}
		return remanager;
	}
	
	// 内部实现
	private static MsgRecver creatMsgRecver(int port, InetListener recverManager) 
	throws IOException
	{
		recv = new MsgRecver(port, recverManager);
		return recv;
	}

	public static void send(String msg, MsgPackage m) {
		send(msg, m.getSocketAddress());
	}
	
	public static void send(String msg, SocketAddress m) {
		InetSocketAddress inet = (InetSocketAddress)m;
		MsgSender send;
		try {
			send = MsgFactory.getMsgSender();
			send.send(msg, inet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
