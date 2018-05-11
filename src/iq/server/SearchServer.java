// CatfoOD 2008-6-8 下午08:09:46

package iq.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import iq.assist.StringTool;
import iq.assist.Tools;
import iq.event.InetListener;
import iq.event.MsgPackage;
import iq.net.MsgFactory;
import iq.sql.SqlFactory;

public class SearchServer implements InetListener {
	private Statement sql;
	
	public SearchServer() throws SQLException {		
		sql = SqlFactory.getInstance().getConnection().createStatement();
	}

	public String getCodex() {
		return "$FIND";
	}

	public void information(Object o) {
		MsgPackage p = (MsgPackage)o;
		String sex = StringTool.getSign(p.getMsg(), "$S");
		String age = StringTool.getSign(p.getMsg(), "$A");
		String city= StringTool.getSign(p.getMsg(), "$C");
		String nick= StringTool.getSign(p.getMsg(), "$N");
		final String RE = "$FIND.$L";
		// 是否在线，暂时不处理
		String sqlspen = "select uid from info_table, user_table " +
				"where (info like '%sex=%"+sex+"%') " +
				"  and (info like '%age=%"+age+"%') " +
				"  and (info like '%city=%"+city+"%') " +
				"  and (name like '%"+nick+"%') and uid=id";
		try {
			ResultSet set = sql.executeQuery(sqlspen);
			StringBuffer buff = new StringBuffer();
			buff.append(RE);
			while (set.next()) {
				buff.append( set.getString(1)+":" );
			}
			MsgFactory.send(buff.toString(), p);
		} catch (SQLException e) {
			Tools.pl("搜索失败."+e);
			MsgFactory.send(RE, p);
		}
		Tools.log("find", this, p.toString());
	}

	public String toString() {
		return "搜索服务器";
	}
}
