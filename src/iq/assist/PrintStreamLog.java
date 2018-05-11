// CatfoOD 2008-6-15 上午07:21:01

package iq.assist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;

/**
 * 重新定位系统输出流到日志文件并实现缓冲
 * 原有的内容继续输出到命令行控制台
 */
public class PrintStreamLog  {
	private final static String LOGPATH = "log/";
	private FileOutputStream logout;
	private PrintStream out;
	
	private PrintStream sysout;
	
	public PrintStreamLog() {
		try {
			initFileOutputStream();
			out = new PrintStream(new LogOutputStream());
			sysout = System.out;
			System.setErr(out);
			System.setOut(out);
			new flushThread();
		} catch (FileNotFoundException e) {
			Tools.pl("重新定位输出流到日志文件错误.");
			e.printStackTrace();
		}
	}
	
	private void initFileOutputStream() throws FileNotFoundException {
		Calendar c = Calendar.getInstance();
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH)+1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		String sd = y+""+(m<10?"0"+m:m)+""+(d<10?"0"+d:d)+".txt";
		
		File logf  = new File(LOGPATH+File.separatorChar+sd);
		logout = new FileOutputStream(logf, true);
	}
	
	private class flushThread extends Thread {
		public flushThread() {
			start();
		}
		
		public void run() {
			while (true) {
				out.flush();
				try {
					sleep(450);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	private class LogOutputStream extends OutputStream {
		private byte[] cache = new byte[2];
		private boolean two  = false;
		private StringBuffer sbuff = new StringBuffer();

		public void write(int d) throws IOException {
			if (d<0 && !two) {
				two = true;
				cache[0] = (byte)d;
				return;
			}
			if (two){
				cache[1] = (byte)d;
				append( new String(cache,0,2) );
				two = false;
				return;
			}
			append( (char)d+"" );
		}
		
		private void append(String s) {
			sbuff.append(s);
		}
		
		public void flush() {
			String s = sbuff.toString();
			if (s.length()>0) {
				sbuff.replace(0, sbuff.length(), "");
				sysout.print(s);
				try {
					logout.write(s.getBytes());
					logout.flush();
				} catch (IOException e) {}
			}
		}
	}
}
