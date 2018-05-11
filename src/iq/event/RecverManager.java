package iq.event;

import iq.assist.CodexMaker;
import iq.assist.StringTool;
import iq.assist.Tools;
import iq.net.MsgFactory;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

/**
 * 注意： 这个类被MsgFactory初始化，并保存有效时列
 */
public class RecverManager implements InetListener {
	private ArrayList<InetListener> listens = new ArrayList<InetListener>();
	private final EventQueueThread eventQueue;
	
	public RecverManager() {
		releseAllListen();
		eventQueue = new EventQueueThread();
	}
	
	/** 释放所有监听器 */
	public void releseAllListen() {
		listens.removeAll(listens);
	}
	
	/** 释放一个监听器 */
	public boolean releaseListen(InetListener o) {
		return listens.remove(o);
	}
	
	/** 注册一个监听器 */
	public boolean regListen(InetListener i) {
		return listens.contains(i)? false: listens.add(i);
	}
	
	/** 消息管理自动去掉头32字节的数据，并自动回应它 */
	public void information(Object o) {
		DatagramPacket packet = (DatagramPacket)o;
		
		String msg = new String(packet.getData(), 
				packet.getOffset(), packet.getLength());
		
		// 回应消息
		msg = affirmResponses(msg, packet);
		MsgPackage mp = new MsgPackage(msg, packet.getSocketAddress());
		
		// dispose(mp);
		eventQueue.add(mp);
	}
	
	// 回应消息
	private String affirmResponses(String s, DatagramPacket d) {
		InetSocketAddress i = (InetSocketAddress)d.getSocketAddress();
		String respon;
		if (s.startsWith(CodexMaker.RESPONSES)) {
			respon = CodexMaker.RESPONSES +
							StringTool.getSign(s, CodexMaker.RESPONSES);
			
			if ( respon.length()==Tools.getCodesLength() ) {
				MsgFactory.send("$ASK."+respon, i);
				return StringTool.getDot(s, 1);
			}
		}
		return s;
	}
	
	// 简单判断是否是指定的前缀
	private void dispose(MsgPackage strMsg) {
		for (int i=0; i<listens.size(); ++i) {
			InetListener liser = listens.get(i);
			if ( strMsg.getMsg().startsWith( liser.getCodex() ) ) {
				try {
					liser.information(strMsg);
				} catch(Exception e) {
					Tools.pl("\n从一个严重错误中恢复,未处理的信息:" +strMsg.getMsg()+ "\n"+
							"客户端IP:" + strMsg.getSocketAddress() +"\n" +
							"错误堆栈:\n");
					e.printStackTrace();
					Toolkit.getDefaultToolkit().beep();
					Tools.pl("");
				}
			}
		}
	}

	/** 消息管理器的这个方法总是返回null */
	public String getCodex() {
		return null;
	}
	
	// 消息队列线成
	private class EventQueueThread extends Thread {
		private Queue<MsgPackage> queue; 
		public EventQueueThread() {
			queue = new ArrayDeque<MsgPackage>();
			start();
		}
		
		public void run() {
			while (true) {
				MsgPackage mp = queue.poll();
				if (mp!=null) {
					dispose(mp);
				} else {
					try {
						sleep(200);
					} catch (InterruptedException e) {}
				}
			}
		}
		
		public void add(MsgPackage p) {
			queue.add(p);
		}
	}
}
