package iq.net;

import java.net.*;
import java.io.*;


public class MsgSender {
	private DatagramSocket socket = null;
	// 客户端发送线程比接受线程 大1，所以要从客户端的发送线程端口-1
	private final static int PORTOFFSITE = 500;
	
	public MsgSender() throws IOException
	{
		socket = new DatagramSocket();
	}

	protected void send(UdpPackage msg) throws IOException
	{
		DatagramPacket packet = new DatagramPacket(	
				msg.getMsg().getBytes(), 
				msg.getMsg().getBytes().length, 
				msg.getRemoteIP(),
				msg.getPort()-PORTOFFSITE );
		
		socket.send(packet);
	}
	
	/**
	 * 发送消息
	 * @param msg - 消息字符串
	 * @throws IOException - 如果网络出错，抛出这个异常
	 */
	public synchronized void send(String msg, InetSocketAddress i) 
	throws IOException 
	{
		send( new UdpPackage(msg, i) );
	}
}

