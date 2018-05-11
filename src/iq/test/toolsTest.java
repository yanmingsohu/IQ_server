// CatfoOD 2008-6-6 ÏÂÎç10:50:22

package iq.test;

import iq.assist.StringTool;
import iq.assist.Tools;

/**
 * ×Ö·û´®½âÎö²âÊÔ
 */
public class toolsTest {
	public static final void main(String[] se) {
		String s = StringTool.formatInfo("a\nb");
		Tools.pl(s);
	}
	
	public static final void main2(String[] se) {
		String result=":f:123:444:";
		String fid = "";
		String s = result.replaceFirst(":"+fid+":", ":");
		Tools.pl(s);
	}
	
	public static void tT() {
		String s = "$msg.$abc.$fff.$s789tool.exe.$namejym";
		
		Tools.pl( StringTool.getDot(s, 2) );
		
		Tools.pl( StringTool.getSign(s, "name"));
		
		Tools.pl( StringTool.getSign(s, "$s"));
		
	}
	
	public static void tT2() {
		String s = "$msg.$abc.$fff.$s789tool.exe.$namejym";
		Tools.pl(StringTool.haveSign(s, "$msg"));
		Tools.pl(StringTool.haveSign(s, "$msg."));
		Tools.pl(StringTool.haveSign(s, "$namejym"));
	}
	
	public static void tT3() {

		String info = "name=1\nkey=2\nb=3";
		String[] s1 = info.split("[\n]");
		for (int i=0; i<s1.length; ++i) {
			Tools.pl(s1[i]);
		}
	}
}
