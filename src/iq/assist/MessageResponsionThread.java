package iq.assist;

import java.io.IOException;

import iq.event.InetListener;
import iq.net.MsgFactory;

/**
 * ����ʹ�÷�����<br>
 * .	new MessageResponsionThread(String msg);<br>
 * .	extends MessageResponsionThread;<br>
 * 
 * ��Ҫ���ǿ�ʼ����߳�:start();
 */
public class MessageResponsionThread extends Thread implements InetListener {
	private String codex = Tools.getCodes();
	/** ����л�Ӧ����true; */
	private volatile boolean responed = false;
	/** ����߳̽�������ʱ��������ɣ�Ϊtrue */
	private volatile boolean threadReturned = false;
	/** Ĭ�ϵĳ�ʱֵ,���� */
	private long TIMEOUT = 15000;
	private long starttime;
	
	/**
	 * ����һ����ҪӦ������ݷ����̣߳��򻯱��
	 * @param sendMsg
	 * @throws IOException
	 */
	public MessageResponsionThread(String sendMsg) throws IOException {
		MsgFactory.getRecverManager().regListen(this);
//		MsgFactory.getMsgSender().send(codex+'.'+sendMsg);
		starttime = System.currentTimeMillis();
	}
	
	/**
	 * ��ʼ�̣߳�������һ����ʱ
	 * @param timeout - ��ʱ�ĺ���
	 */
	public void start(long timeout) {
		TIMEOUT = timeout;
		super.start();
	}
	
	public void run() {
		while (!responed) {
			try {
				if (starttime+TIMEOUT<System.currentTimeMillis()) {
					threadReturned = true;
					overTime();
					return;
				}
				sleep(200);
			} catch (InterruptedException e) {}
		}
		threadReturned = true;
		MsgFactory.getRecverManager().releaseListen(this);
		if (responed) complete();
	}

	public String getCodex() {
		return "$ASK."+codex;
	}

	public void information(Object o) {
		responed = true;
	}
	
	/**
	 * �ȴ���ֱ���̴߳�������ܵ���Ϣ�����߷�������ʱ
	 */
	public void waiting() {
		while (!threadReturned) {
			try {
				sleep(200);
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * �Ƿ��յ���������Ӧ���յ�����true
	 */
	public boolean isResponsioned() {
		return responed;
	}
	
	/**
	 * ������д�ķ���,�ڳ�ʱ��ᱻ����,Ĭ�ϵķ���Ϊ��
	 */
	public void overTime() {}
	
	/**
	 * ������д�ķ���������Ч��ʱ���ڵõ���Ӧ�����Ϣ��������
	 */
	public void complete() {}
}

// ʵ��

//class frame {
//	///..............
//	void send() {
//		try {
//			new a("��á�");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	class a extends MessageResponsionThread {
//		a(String s) throws IOException {
//			super(s);
//		}
//		
//		/**
//		 * ������д�ķ���,�ڳ�ʱ��ᱻ����,Ĭ�ϵķ���Ϊ��
//		 */
//		public void overTime() {
//			// ���У�
//			// ��ʵ���ղŵ���Ϣû�з��ͳɹ�
//		}
//		
//		/**
//		 * ������д�ķ���������Ч��ʱ���ڵõ���Ӧ�����Ϣ��������
//		 */
//		public void complete() {
//			// ��ɣ��ܵ�������Ӧ��
//			// ���Լ���
//		}
//	}
//}