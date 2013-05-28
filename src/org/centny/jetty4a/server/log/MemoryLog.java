package org.centny.jetty4a.server.log;

import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.Logger;

public class MemoryLog extends AbstractLogger {

	private static StringBuffer logs__ = new StringBuffer();
	private static int storaged__ = 0;
	private static int storage_max__ = 300;
	private static String longestLine__ = "";

	public static void setStorageMax(int max) {
		storage_max__ = max;
	}

	public static void addLog(MemoryLog mlog, String line) {
		synchronized (logs__) {

			if (storaged__ > storage_max__) {
				logs__.delete(0, logs__.indexOf("\n") + 1);
				storaged__--;
			}
			logs__.append(line + "\n");
			storaged__++;
			if (line.length() > longestLine__.length()) {
				longestLine__ = line;
			}
		}
	}

	public static String longedsLine() {
		return longestLine__;
	}

	public static String allLog() {
		synchronized (logs__) {
			return logs__.toString();
		}
	}

	private String name;
	private boolean debugEnabled;

	/**
	 * 
	 */
	public MemoryLog() {
	}

	/**
	 * @param name
	 */
	public MemoryLog(String name) {
		if (name != null) {
			String[] names = name.split("\\.");
			StringBuffer sbuf = new StringBuffer();
			for (int i = 0; i < names.length - 1; i++) {
				sbuf.append(names[i].charAt(0));
			}
			if (sbuf.length() > 0) {
				sbuf.append(".");
			}
			sbuf.append(names[names.length - 1]);
			this.name = sbuf.toString();
		} else {
			this.name = name;
		}
	}

	private String format(String fmt, Object... args) {
		fmt = fmt.replaceAll("\\{\\}", "%s");
		return String.format(fmt, args);
	}

	private void addLog(String type, String msg) {
		if (name == null) {
			addLog(this, type + " " + msg);
		} else {
			addLog(this, type + " " + name + " " + msg);
		}
	}

	@Override
	public void debug(Throwable arg0) {
		this.addLog("DEBUG", arg0.getMessage());
	}

	@Override
	public void debug(String arg0, Object... arg1) {
		this.addLog("DEBUG", format(arg0, arg1));
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		this.addLog("DEBUG", arg0 + " " + arg1.getMessage());
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void ignore(Throwable arg0) {

	}

	@Override
	public void info(Throwable arg0) {
		this.addLog("INFO  ", arg0.getMessage());
	}

	@Override
	public void info(String arg0, Object... arg1) {
		this.addLog("INFO  ", format(arg0, arg1));
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		this.addLog("INFO  ", arg0 + " " + arg1.getMessage());
	}

	@Override
	public boolean isDebugEnabled() {
		return this.debugEnabled;
	}

	@Override
	public void setDebugEnabled(boolean arg0) {
		this.debugEnabled = arg0;
	}

	@Override
	public void warn(Throwable arg0) {
		this.addLog("WARN  ", arg0.getMessage());
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		this.addLog("WARN  ", format(arg0, arg1));
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		this.addLog("WARN  ", arg0 + " " + arg1.getMessage());
	}

	@Override
	protected Logger newLogger(String arg0) {
		return new MemoryLog(arg0);
	}

}
