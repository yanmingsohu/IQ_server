// CatfoOD 2008-6-7 上午08:39:33

package iq.server.queue;

import iq.assist.Tools;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Exception.LogonException;

/**
 * 在线队列，维护所有在线用户列表
 */
public class OnLineQueue {
	private static OnLineQueue instance;
	
	public static OnLineQueue getInstance() {
		if (instance==null) throw 
				new IllegalStateException("在线队列未被初始化.");
		
		return instance;
	}
	
	/** 初始化在线队列 */
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
	 * 确定指定的用户是否在线
	 * @param user - 要检查的用户
	 * @return - 在线返回true
	 */
	public boolean isOnline(UserEntity user) {
		return list.containsValue(user);
	}
	
	/**
	 * 确定指定的用户是否在线
	 * @param user - 要检查的用户登陆时使用的网络
	 * @return - 在线返回true
	 */
	public boolean isOnline(SocketAddress user) {
		return list.containsKey(user);
	}
	
	/**
	 * 返回用户的实体
	 * @param socket - 客户端连接的信息
	 * @return - 如果不存在返回null
	 */
	public UserEntity getUserEntity(SocketAddress socket) {
		return list.get(socket);
	}
	
	/**
	 * 按照用户的id查找在线用户的实体
	 * @param id - 用户id
	 * @return - UserEntity,如果未发现返回null
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
	 * 向在线用户列表添加一个用户，成功返回true
	 * @param v - 用户实体
	 * @return - 失败的原因，用户已经在线
	 */
	public boolean addUserEntity(UserEntity v) throws LogonException {
		if (isOnline(v)) {
			throw new LogonException("此用户已经在线");
		}
		list.put(v.getSocketAddress(), v);
		return true;
	}
	
	/**
	 * 用户离线
	 */
	public void removeUserEntity(UserEntity v) {
		list.remove(v.getSocketAddress());
	}
	
	public String toString() {
		return "在线队列";
	}
}
