// CatfoOD 2008-6-8 下午03:47:03

package iq.server;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.server.queue.OnLineQueue;
import iq.server.queue.UserEntity;
import iq.server.queue.UserInfo;

public class InformationServer implements InetListener{
	
	public InformationServer() {
		// do nothing
	}

	public void information(Object o) {
		MsgPackage p = (MsgPackage)o;
		String id = StringTool.getSign(p.getMsg(), "$ID");
		
		if (StringTool.haveSign(p.getMsg(), "$PUT")) {
			OnLineQueue online = OnLineQueue.getInstance();
			UserEntity user = online.getUserEntity(p.getSocketAddress());
			
			if (user!=null) {
				String newinfo = StringTool.getSign(p.getMsg(), "$BODY");
				if (user.updataInfo(newinfo)) {
					MsgFactory.send("$INFO.$ID"+id+".$OK", p);
					return;
				}
			}
			MsgFactory.send("$INFO.$ID"+id+".$NO", p);
			
			Tools.log(user, this, p);
			return;
		}
		else if (StringTool.haveSign(p.getMsg(), "$GET")) {
			String info = UserInfo.getUserInformation(id);
			
			String msg = "$INFO.$ID"+id;
			if (info!=null) {
				MsgFactory.send(msg+".$OK.$BODY"+info, p);
			} else {
				MsgFactory.send(msg+".$NO", p);
			}
		}
	}

	public String getCodex() {
		return "$INFO";
	}
	
	public String toString() {
		return "信息服务器";
	}
}
