package iq.assist;

import java.io.IOException;

import iq.event.InetListener;
import iq.net.MsgFactory;

/**
 * 两种使用方法：<br>
 * .	new MessageResponsionThread(String msg);<br>
 * .	extends MessageResponsionThread;<br>
 * 
 * 不要忘记开始这个线程:start();
 */
public class MessageResponsionThread extends Thread implements InetListener {
	private String codex = Tools.getCodes();
	/** 如果有回应返回true; */
	private volatile boolean responed = false;
	/** 如果线程结束（超时，或者完成）为true */
	private volatile boolean threadReturned = false;
	/** 默认的超时值,毫秒 */
	private long TIMEOUT = 15000;
	private long starttime;
	
	/**
	 * 建立一个需要应答的数据发送线程，简化编程
	 * @param sendMsg
	 * @throws IOException
	 */
	public MessageResponsionThread(String sendMsg) throws IOException {
		MsgFactory.getRecverManager().regListen(this);
//		MsgFactory.getMsgSender().send(codex+'.'+sendMsg);
		starttime = System.currentTimeMillis();
	}
	
	/**
	 * 开始线程，并设置一个超时
	 * @param timeout - 超时的毫秒
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
	 * 等待，直到线程从网络接受到消息，或者服务器超时
	 */
	public void waiting() {
		while (!threadReturned) {
			try {
				sleep(200);
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * 是否收到服务器的应答，收到返回true
	 */
	public boolean isResponsioned() {
		return responed;
	}
	
	/**
	 * 子类重写的方法,在超时后会被调用,默认的方法为空
	 */
	public void overTime() {}
	
	/**
	 * 子类重写的方法，在有效的时间内得到了应答的消息，被调用
	 */
	public void complete() {}
}

// 实例

//class frame {
//	///..............
//	void send() {
//		try {
//			new a("你好。");
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
//		 * 子类重写的方法,在超时后会被调用,默认的方法为空
//		 */
//		public void overTime() {
//			// 超市，
//			// 现实：刚才的消息没有发送成功
//		}
//		
//		/**
//		 * 子类重写的方法，在有效的时间内得到了应答的消息，被调用
//		 */
//		public void complete() {
//			// 完成，受到服务器应答
//			// 你自己的
//		}
//	}
//}