package iq.event;

public interface InetListener {
	/**
	 * Listener���ĵ��ַ�������
	 * @return - �����������
	 */
	public String getCodex();
	
	/**
	 * ����������ܵ������ݷ���getCodex()�����ݸ�ʽ�����������֪ͨ<br>
	 * Ӧ�þ��췵��,�����������Ϣ�߳�.
	 * @param o - �����Ϊ MsgPackage
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