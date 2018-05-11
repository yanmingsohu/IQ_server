// CatfoOD 2008-6-10 下午09:21:07

package iq.server;

import java.sql.ResultSet;
import java.sql.Statement;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.sql.SqlFactory;

public class NamedServer implements InetListener {

	public String getCodex() {
		return "$NAME";
	}

	public void information(Object o) {
		MsgPackage msgpack = (MsgPackage)o;
		String src = msgpack.getMsg();
		String id = StringTool.getSign(src, "$ID");
		String name = "";
		try {
			Statement sql =
				SqlFactory.getInstance().getConnection().createStatement();
			ResultSet set = 
				sql.executeQuery("select name from user_table where id="+id);
			if (set.next()) {
				name = set.getString(1);
			}
		} catch(Exception e) {
			Tools.pl("发送名字时出错."+e);
		}
		MsgFactory.send("$NAME.$ID"+id+".$UN"+name, msgpack);
		Tools.log(id, name, msgpack.toString());
	}

	public String toString() {
		return "名字服务器";
	}
}
