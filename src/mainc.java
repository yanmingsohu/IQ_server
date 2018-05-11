import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;

import iq.assist.PrintStreamLog;
import iq.assist.SystemConfig;
import iq.assist.Tools;
import iq.assist.VersionControl;
import iq.event.InetListener;
import iq.event.RecverManager;
import iq.net.MsgFactory;
import iq.server.*;
import iq.server.queue.OnLineQueue;
import iq.sql.SqlFactory;

// CatfoOD 2008.6.6

public class mainc {
	public final static String line;
	public final static RecverManager regs;
	
	static {
		String sl ="";
		for (int i=0;i<79; ++i) {
			sl += "-";
		}
		line = sl;
		regs = MsgFactory.getRecverManager();
	}

	public static void main(String[] args) {	
		new PrintStreamLog();	
		printWelcome();
		checkConnect();
		checkSql();
		
		reg(RegisterServer.class);
		reg(LogonServer.class);
		reg(FriendServer.class);
		reg(InformationServer.class);
		reg(SearchServer.class);
		reg(ListServer.class);
		reg(MessageServer.class);
		reg(OfflineMsgServer.class);
		reg(NamedServer.class);
		
		OnLineQueue.init();
		congratulate();
	}
	
	public static void reg(Class<?> c) {
		final int slength = 30;
		try {
			InetListener i = (InetListener)c.newInstance();
			String proname = "启动 . . ."+i;
			p(proname);
			for (int j=slength-proname.getBytes().length; j>0; --j) {
				p(" ");
			}
			regs.regListen(i);
			pl("ok");
		} catch (Exception e) {			
			pl("error");
			pl(e.getMessage());
			quit();
		}
	}

	public static void checkSql() {
		try {
			p("连接数据库 . . . .\t\t");
			SqlFactory.init();
			pl("ok");
			pl("\t\t\t\t"+SqlFactory.getInstance().getConnectDriverName()+"\n");
		} catch (SQLException e) {
			pl("error");
			pl(e.getMessage());
			quit();
		}
	}
	
	public static void checkConnect() {
		try {
			p("开启网络连接 . . .\t\t");
			MsgFactory.init();
			pl("ok");
		} catch (IOException e) {
			pl("error");
			pl(e.getMessage());
			quit();
		}
	}

	public static void printWelcome() {
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			hostname = "/127.0.0.1";
		}
		pl(VersionControl.staff+"\n");
		
		p(VersionControl.programNameEN+" "+VersionControl.version);
		pl("  last edit."+VersionControl.lastEdit);
		p("[Server name: "+hostname+"]");
		pl("  [Port: "+SystemConfig.get(SystemConfig.SERVERPORT)+"]");
		pl(line);
	}
	
	public static void congratulate() {
		pl("\niQ2008 服务器启动完成. ("+new Date().toLocaleString()+")");
	}
	
	private static void p(Object o) {
		Tools.p(o);
	}
	
	private static void pl(Object o) {
		Tools.pl(o);
	}
	
	private static void quit() {
		Tools.pl("\n .. 有错误,系统退出");
		System.out.flush();
		System.err.flush();
		System.exit(1);
	}
}
