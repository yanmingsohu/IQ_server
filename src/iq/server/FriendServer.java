// CatfoOD 2008-6-8 上午09:27:17

package iq.server;

import java.net.SocketAddress;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.server.queue.OnLineQueue;
import iq.server.queue.UserEntity;

public class FriendServer implements InetListener {
	
	public FriendServer() {
		// do nothing
	}

	public String getCodex() {
		return "$FRI";
	}

	public void information(Object o) {
		MsgPackage p = (MsgPackage)o;
		String fid = StringTool.getSign(p.getMsg(), "$FID");
		String info = StringTool.getSign(p.getMsg(), "$INFO");
		
		OnLineQueue online = OnLineQueue.getInstance();
		UserEntity user = online.getUserEntity(p.getSocketAddress());
		if (user!=null) {
			if (StringTool.haveSign(p.getMsg(), "$ADD")) {
				addFriend(user, fid, info);
				Tools.log(user, this, p);
			} else if ( StringTool.haveSign(p.getMsg(), "$DEL") ) {
				delFriend(user, fid);
				Tools.log(user, this, p);
			}
		}
	}
	
	private void addFriend(UserEntity user, String fid, String info) {
		user.inviteFriend(fid, info);
	}
	
	// 删除好友
	private void delFriend(UserEntity user, String fid) {
		user.delFriend(fid);
		send("$FRI.$FID"+fid+".$OK", user.getSocketAddress());
	}
	
	private void send(String msg, SocketAddress m) {
		MsgFactory.send(msg, m);
	}
	
	public String toString() {
		return "好友服务器";
	}
}
