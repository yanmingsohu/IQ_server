package Exception;

/**
 * һ����½�쳣��һ����Ч���û��������벻��ʱ���׳�
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
