// CatfoOD 2008-6-6 下午10:19:21

package iq.assist;

public class StringTool {
	private StringTool() {}
	
	/**
	 * 根据dotc的位置返回src中的子字符串<br>
	 * 如果源字符串为: $msg.$abc.$fff.$s789<br>
	 * dotc==2,返回$fff.$s789<br>
	 * <br>
	 * 如果找不到，返回""<br>
	 */
	public static String getDot(String src, int dotc) {
		int startIndex = 0;
		for (int si = 0; si<dotc; si++) {
			startIndex = src.indexOf('.', startIndex);
			if (startIndex<0) return "";
			++startIndex;
		}
		
		return src.substring(startIndex);
	}
	
	/**
	 * 计算字符串中'.'的数量<br>
	 * $msg.$abc.$fff.$s78 中点的数量为3<br>
	 */
	public static int getDotCount(String src) {
		char[] cs = src.toCharArray();
		int count = 0;
		for (int i=0; i<cs.length; ++i) {
			if (cs[i]=='.') {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 根据sing的符号返回src中的子字符串<br>
	 * 如果源字符串为: "$msg.$abc.$fff.$s789tool.exe.$namejym"<br><br>
	 * sign=='$a',		返回'bc'<br>
	 * sign=='$f',		返回'ff'<br>
	 * sign=='$name',	返回'jym'<br>
	 * sign=='$s',		返回'789tool.exe'<br>
	 * sing=='$msg',	返回''(没有长度的字符串)<br>
	 * <br>
	 * 如果存在重复则返回第一个有效的字符串<br>
	 * 否则返回"",返回的字符串不包含空格
	 */
	public static String getSign(String src, String sign) {
		int i = src.indexOf(sign);
		i += sign.length();
		if (i<0 || i>=src.length()-1) return "";
		int e = i;
		while ( true ) {
			e++;
			if (e<src.length()) {
				if (src.charAt(e)=='$' && src.charAt(e-1)=='.' ) {
					e--;
					break;
				}
			} else {
				break;
			}
		}
		return src.substring(i, e).trim();
	}
	

	/**
	 * 判断是否在src中的有子字符串sign<br>
	 * 如果源字符串为: "$msg.$abc.$fff.$s789tool.exe.$namejym"<br>
	 * $sign=='$msg',	return true;<br>
	 * $sign=='abc',	return false;<br>
	 */
	public static boolean haveSign(String src, String sign) {
		int s = src.indexOf(sign);
		int e = s+sign.length();
		if (s<0) {
			return false;
		} else if (s==0 || src.charAt(s-1)=='.') {
			if (e==src.length()) {
				return true;
			} else {
				if (src.charAt(e)=='.') {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 格式化从网络接收到的个人资料字符串，转换为数据库中的字符串
	 * @param s - 从网络接收到的字符串
	 * @return - 可以用Sql命令放入数据库中的字符串
	 */
	public static String formatInfo(String s) {
		// 防止换行符在最后一个位置出现
		if (s.lastIndexOf('\n')==s.length()-1) {
			s=s.substring(0, s.length()-1);
		}
		return s.replaceAll("[\n]", "'+char(10)+'");
	}
}
