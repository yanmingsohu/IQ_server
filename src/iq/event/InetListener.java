package iq.event;

public interface InetListener {
	/**
	 * Listener关心的字符串掩码
	 * @return - 返回这个掩码
	 */
	public String getCodex();
	
	/**
	 * 当从网络接受到的数据符合getCodex()的数据格式，这个方法被通知<br>
	 * 应该尽快返回,否则会阻塞消息线程.
	 * @param o - 服务端为 MsgPackage
	 */
	public void information(Object o);
}

//class a implements InetListener {
//
//	@Override
//	public String getCodex() {
//		return "$MSG";
//	}
//
//	@Override
//	public void information(Object o) {
//		o.toString();		
//	}
//	
//}