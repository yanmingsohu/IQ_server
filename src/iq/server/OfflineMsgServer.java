// CatfoOD 2008-6-10 下午07:48:43

package iq.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.server.queue.OnLineQueue;
import iq.server.queue.UserEntity;
import iq.sql.SqlFactory;

public class OfflineMsgServer implements InetListener {

	public String getCodex() {
		return "$OFFLINE";
	}

	public void information(Object o) {
		MsgPackage msgpack = (MsgPackage)o;
		UserEntity user = OnLineQueue.getInstance().
					getUserEntity(msgpack.getSocketAddress());
		String id = user.getUserID();
		
		try {
			Statement sql = 
				SqlFactory.getInstance().getConnection().createStatement();
			String sqlspen =
				"select msg,sid from offline_msg where rid="+ id;
			
			ResultSet set = sql.executeQuery(sqlspen);
			String msg;
			String sid;
			while (set.next()) {
				msg = set.getString(1);
				sid = set.getString(2);
				MsgFactory.send("$MSG.$S"+sid+"."+msg, msgpack);
			}
			sql.executeUpdate("delete from offline_msg where rid="+ id);
			set.close();
			Tools.log(user, this, msgpack);
		} catch (SQLException e) {
			Tools.pl("发送离线数据错误."+e);
		}
	}
	
	public String toString() {
		return "离线消息服务器";
	}
}
