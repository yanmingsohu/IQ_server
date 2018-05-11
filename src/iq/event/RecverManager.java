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
 * ע�⣺ ����౻MsgFactory��ʼ������������Чʱ��
 */
public class RecverManager implements InetListener {
	private ArrayList<InetListener> listens = new ArrayList<InetListener>();
	private final EventQueueThread eventQueue;
	
	public RecverManager() {
		releseAllListen();
		eventQueue = new EventQueueThread();
	}
	
	/** �ͷ����м����� */
	public void releseAllListen() {
		listens.removeAll(listens);
	}
	
	/** �ͷ�һ�������� */
	public boolean releaseListen(InetListener o) {
		return listens.remove(o);
	}
	
	/** ע��һ�������� */
	public boolean regListen(InetListener i) {
		return listens.contains(i)? false: listens.add(i);
	}
	
	/** ��Ϣ�����Զ�ȥ��ͷ32�ֽڵ����ݣ����Զ���Ӧ�� */
	public void information(Object o) {
		DatagramPacket packet = (DatagramPacket)o;
		
		String msg = new String(packet.getData(), 
				packet.getOffset(), packet.getLength());
		
		// ��Ӧ��Ϣ
		msg = affirmResponses(msg, packet);
		MsgPackage mp = new MsgPackage(msg, packet.getSocketAddress());
		
		// dispose(mp);
		eventQueue.add(mp);
	}
	
	// ��Ӧ��Ϣ
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
	
	// ���ж��Ƿ���ָ����ǰ׺
	private void dispose(MsgPackage strMsg) {
		for (int i=0; i<listens.size(); ++i) {
			InetListener liser = listens.get(i);
			if ( strMsg.getMsg().startsWith( liser.getCodex() ) ) {
				try {
					liser.information(strMsg);
				} catch(Exception e) {
					Tools.pl("\n��һ�����ش����лָ�,δ�������Ϣ:" +strMsg.getMsg()+ "\n"+
							"�ͻ���IP:" + strMsg.getSocketAddress() +"\n" +
							"�����ջ:\n");
					e.printStackTrace();
					Toolkit.getDefaultToolkit().beep();
					Tools.pl("");
				}
			}
		}
	}

	/** ��Ϣ������������������Ƿ���null */
	public String getCodex() {
		return null;
	}
	
	// ��Ϣ�����߳�
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
