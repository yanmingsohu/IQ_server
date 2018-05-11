// CatfoOD 2008-6-8 下午09:54:00

package iq.server;

import java.sql.SQLException;
import java.sql.Statement;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.server.queue.OnLineQueue;
import iq.server.queue.UserEntity;
import iq.sql.SqlFactory;

public class MessageServer implements InetListener{
	private Statement sql;
	
	public MessageServer() throws SQLException {
		sql = SqlFactory.getInstance().getConnection().createStatement();
	}

	public String getCodex() {
		return "$MSG";
	}

	public void information(Object o) {
		MsgPackage p = (MsgPackage)o;
		OnLineQueue online = OnLineQueue.getInstance();
		UserEntity user =  online.getUserEntity(p.getSocketAddress());
		
		if (user==null) {
			Tools.log("unkonwID", "指定的用户不存在,或者已经下线", p);
			return;
		}
		String id = user.getUserID();
		String fid = StringTool.getSign(p.getMsg(), "$S");
		String msg = StringTool.getDot(p.getMsg(), 2);
		
		UserEntity puser = online.getUserEntity(fid);
		
		if (puser!=null) {
			MsgFactory.send("$MSG.$S"+id+"."+msg, puser.getSocketAddress());
			Tools.log(puser, "send msg", p);
			
		} else {
			String talkmsg = StringTool.getSign(p.getMsg(), "$UM");
			if (talkmsg.trim().length()>0) {
				talkmsg = StringTool.formatInfo(talkmsg);
				String sqlspen = "insert into offline_msg" +
						"(sid,rid,msg) values ("+id+","+fid+",'"+msg+"')";
				
				try {
					sql.executeUpdate(sqlspen);
					Tools.log(id, "offline msg", p);
				} catch (SQLException e) {
					Tools.pl("加入离线数据出错."+e);
				}
			}
		}
	}
	
	public String toString() {
		return "聊天消息服务器";
	}
}
