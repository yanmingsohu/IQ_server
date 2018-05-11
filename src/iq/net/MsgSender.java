package iq.net;

import java.net.*;
import java.io.*;


public class MsgSender {
	private DatagramSocket socket = null;
	// �ͻ��˷����̱߳Ƚ����߳� ��1������Ҫ�ӿͻ��˵ķ����̶߳˿�-1
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
	 * ������Ϣ
	 * @param msg - ��Ϣ�ַ���
	 * @throws IOException - �����������׳�����쳣
	 */
	public synchronized void send(String msg, InetSocketAddress i) 
	throws IOException 
	{
		send( new UdpPackage(msg, i) );
	}
}

