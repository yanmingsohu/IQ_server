// CatfoOD 2008-6-6 ����10:19:21

package iq.assist;

public class StringTool {
	private StringTool() {}
	
	/**
	 * ����dotc��λ�÷���src�е����ַ���<br>
	 * ���Դ�ַ���Ϊ: $msg.$abc.$fff.$s789<br>
	 * dotc==2,����$fff.$s789<br>
	 * <br>
	 * ����Ҳ���������""<br>
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
	 * �����ַ�����'.'������<br>
	 * $msg.$abc.$fff.$s78 �е������Ϊ3<br>
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
	 * ����sing�ķ��ŷ���src�е����ַ���<br>
	 * ���Դ�ַ���Ϊ: "$msg.$abc.$fff.$s789tool.exe.$namejym"<br><br>
	 * sign=='$a',		����'bc'<br>
	 * sign=='$f',		����'ff'<br>
	 * sign=='$name',	����'jym'<br>
	 * sign=='$s',		����'789tool.exe'<br>
	 * sing=='$msg',	����''(û�г��ȵ��ַ���)<br>
	 * <br>
	 * ��������ظ��򷵻ص�һ����Ч���ַ���<br>
	 * ���򷵻�"",���ص��ַ����������ո�
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
	 * �ж��Ƿ���src�е������ַ���sign<br>
	 * ���Դ�ַ���Ϊ: "$msg.$abc.$fff.$s789tool.exe.$namejym"<br>
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
	 * ��ʽ����������յ��ĸ��������ַ�����ת��Ϊ���ݿ��е��ַ���
	 * @param s - ��������յ����ַ���
	 * @return - ������Sql����������ݿ��е��ַ���
	 */
	public static String formatInfo(String s) {
		// ��ֹ���з������һ��λ�ó���
		if (s.lastIndexOf('\n')==s.length()-1) {
			s=s.substring(0, s.length()-1);
		}
		return s.replaceAll("[\n]", "'+char(10)+'");
	}
}
