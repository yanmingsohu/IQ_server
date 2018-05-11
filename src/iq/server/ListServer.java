// CatfoOD 2008-6-8 下午09:35:22

package iq.server;

import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.server.queue.OnLineQueue;
import iq.server.queue.UserEntity;

public class ListServer implements InetListener{
	public ListServer() {
		// do nothing;
	}
	
	public String toString() {
		return "列表服务器";
	}

	public String getCodex() {
		return "$LIST";
	}

	public void information(Object o) {
		MsgPackage p = (MsgPackage)o;
		OnLineQueue online = OnLineQueue.getInstance();
		UserEntity user = online.getUserEntity(p.getSocketAddress());
		
		if (user!=null) {
			String list = user.getFriendList();
			MsgFactory.send("$LIST.$UL"+list, p);
		} else {
			/// ... do nothing
		}
		Tools.log(user+"", this, p+"");
	}
}
