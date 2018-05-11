package iq.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UdpPackage {
	private String msg;
	private InetAddress remote;
	private int remoteport;
	
	public UdpPackage(String msg, InetAddress ip, int port) {
		this.msg = msg;
		remote = ip;
		remoteport = port;
	}
	
	public UdpPackage(String msg, InetSocketAddress i) {
		this.msg = msg;
		remote = i.getAddress();
		remoteport = i.getPort();
	}
	
	public String getMsg() {
		return msg;
	}
	
	public InetAddress getRemoteIP() {
		return remote;
	}
	
	public int getPort() {
		return remoteport;
	}
}
