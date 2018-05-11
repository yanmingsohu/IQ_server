// CatfoOD 2008-6-7 ����08:39:33

package iq.server.queue;

import iq.assist.Tools;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Exception.LogonException;

/**
 * ���߶��У�ά�����������û��б�
 */
public class OnLineQueue {
	private static OnLineQueue instance;
	
	public static OnLineQueue getInstance() {
		if (instance==null) throw 
				new IllegalStateException("���߶���δ����ʼ��.");
		
		return instance;
	}
	
	/** ��ʼ�����߶��� */
	public static void init() {
		if (instance==null) {
			instance = new OnLineQueue();
		}
	}
	
	// -------------------- instance ---------------------------
	private Map<SocketAddress, UserEntity> list;
	
	private OnLineQueue() {
		list = new HashMap<SocketAddress, UserEntity>();
	}
	
	/**
	 * ȷ��ָ�����û��Ƿ�����
	 * @param user - Ҫ�����û�
	 * @return - ���߷���true
	 */
	public boolean isOnline(UserEntity user) {
		return list.containsValue(user);
	}
	
	/**
	 * ȷ��ָ�����û��Ƿ�����
	 * @param user - Ҫ�����û���½ʱʹ�õ�����
	 * @return - ���߷���true
	 */
	public boolean isOnline(SocketAddress user) {
		return list.containsKey(user);
	}
	
	/**
	 * �����û���ʵ��
	 * @param socket - �ͻ������ӵ���Ϣ
	 * @return - ��������ڷ���null
	 */
	public UserEntity getUserEntity(SocketAddress socket) {
		return list.get(socket);
	}
	
	/**
	 * �����û���id���������û���ʵ��
	 * @param id - �û�id
	 * @return - UserEntity,���δ���ַ���null
	 */
	public UserEntity getUserEntity(String id) {
		Collection<UserEntity> cl = list.values();
		for (UserEntity user:cl) {
			if (user.equals(id)) {
				return user;
			}
		}
		return null;
	}
	
	/**
	 * �������û��б����һ���û����ɹ�����true
	 * @param v - �û�ʵ��
	 * @return - ʧ�ܵ�ԭ���û��Ѿ�����
	 */
	public boolean addUserEntity(UserEntity v) throws LogonException {
		if (isOnline(v)) {
			throw new LogonException("���û��Ѿ�����");
		}
		list.put(v.getSocketAddress(), v);
		return true;
	}
	
	/**
	 * �û�����
	 */
	public void removeUserEntity(UserEntity v) {
		list.remove(v.getSocketAddress());
	}
	
	public String toString() {
		return "���߶���";
	}
}
