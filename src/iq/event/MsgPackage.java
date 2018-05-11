// CatfoOD 2008-6-8 上午07:31:21

package iq.event;

import java.net.SocketAddress;

/**
 * 服务端使用这个类，封装消息
 */
public class MsgPackage {
	private final String m;
	private final SocketAddress s;
	
	public MsgPackage(String msg, SocketAddress socket) {
		m = msg;
		s = socket;
	}
	
	public String getMsg() {
		return m;
	}
	
	public SocketAddress getSocketAddress() {
		return s;
	}
	
	public String toString() {
		return "["+m+"] "+s.toString();
	}
}
