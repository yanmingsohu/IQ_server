package iq.assist;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

public class CodexMaker {
	public static final String RESPONSES = "$REPON";
	
	/** 返回32字节掩码 */
	public static String getCodes() {
		Calendar c = Calendar.getInstance();
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH)+1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int h = c.get(Calendar.HOUR_OF_DAY);
		int mm = c.get(Calendar.MINUTE);
		int s = c.get(Calendar.SECOND);
		int ms = c.get(Calendar.MILLISECOND);
		
		String time = y+ fInt2(m)+ fInt2(d)+ fInt2(h)+ 
						fInt2(mm)+ fInt2(s) + fInt3(ms);
		
		String radomint = fInt3((int)(Math.random()*1000));
		
		return RESPONSES + time + getLocalHost() + radomint;
	}
	
	/** 返回掩码的固定长度 */
	public static int getCodesLength() {
		if (cl<0) cl = getCodes().length();
		return cl;
	}
	private static int cl = -1;
	
	// 返回本地IP的字符串表示
	private static String getLocalHost() {
		if (localHost==null) {
			String name;
			try {
				name = InetAddress.getLocalHost().getCanonicalHostName();
			} catch (UnknownHostException e) {
				name = "UNKNOW";
			}
			char[] dst = new char[] {'0','0','0','0','0','0','0','0','0','0','/'};
			int srcEnd = name.length()<dst.length? name.length(): dst.length;
			name.getChars(0, srcEnd, dst, 0);
			localHost = new String(dst);
		}
		return localHost;
	}
	private static String localHost = null;
	
	public static final String fInt2(int i) {
		return i<10? "0"+i: ""+i;
	}
	
	public static final String fInt3(int i) {
		return i<10? "00"+i : i<100? "0"+i: ""+i;
	}
}
