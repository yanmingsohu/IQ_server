// CatfoOD 2008-6-8 上午09:06:52

package iq.server;


import Exception.LogonException;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.server.queue.OnLineQueue;
import iq.server.queue.UserEntity;

public class LogonServer implements InetListener {
	
	public LogonServer() {
		// 注册离线消息
		MsgFactory.getRecverManager().regListen( new InetListener() {
			public String getCodex() {
				return "$LOGOUT";
			}

			public void information(Object o) {
				MsgPackage msgpack = (MsgPackage)o;
				UserEntity user = OnLineQueue.getInstance().
						getUserEntity(msgpack.getSocketAddress());
				if (user!=null) {
					user.logout();
					Tools.log(user, "logout", msgpack);
				}
			}
		});
	}

	public String getCodex() {
		return "$LOGON";
	}

	public void information(Object o) {
		MsgPackage msgpack = (MsgPackage)o;
		String src = msgpack.getMsg();
		String name = StringTool.getSign(src, "$ID");
		String pass = StringTool.getSign(src, "$P");
		try {
			UserEntity user = 
				new UserEntity(name, pass, msgpack.getSocketAddress());
			OnLineQueue.getInstance().addUserEntity(user);
			send("$LOGON.$OK", msgpack);
			
			Tools.log(user, "logon", msgpack);
		} catch (LogonException e) {
			send("$LOGON.$NO"+e, msgpack);
		}
	}
	
	// 向网络发送消息
	private void send(String msg, MsgPackage m) {
		MsgFactory.send(msg, m);
	}
	
	public String toString() {
		return "登陆服务器";
	}
}
