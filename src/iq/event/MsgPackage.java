// CatfoOD 2008-6-8 ����07:31:21

package iq.event;

import java.net.SocketAddress;

/**
 * �����ʹ������࣬��װ��Ϣ
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
