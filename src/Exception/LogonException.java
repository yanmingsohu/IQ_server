package Exception;

/**
 * 一个登陆异常，一般在效验用户名和密码不符时被抛出
 */
public class LogonException extends Exception {
	private static final long serialVersionUID = 1L;
	private String s = null;
	
	public LogonException(String s) {
		super(s);
		this.s = s;
	}
	
	public String getMessage() {
		return s;
	}
	
	public String toString() {
		return s;
	}
}
