package iq.net;
import iq.assist.Tools;
import iq.event.InetListener;

import java.net.*;
import java.io.*;


public class MsgRecver extends Thread
{
	private DatagramSocket socket = null;
	private DatagramPacket packet = null;
	private byte [] buff;
	private boolean running = true;
	private InetListener listener;

	public MsgRecver(int listenport, InetListener listener)
	throws IOException
	{
		buff = new byte[1024];
		socket = new DatagramSocket(listenport);
		packet = new DatagramPacket(buff, 1024);
		this.listener = listener;
		start();
	}
	
	public void run()
	{
		while(running)
		{
			try{
				socket.receive(packet);
				listener.information(packet);
			}
			catch (IOException ie){
				//ie.printStackTrace();
			}
		}		
	}
	
	public void stopd() {
		running = false;
		socket.close();
	}
}

