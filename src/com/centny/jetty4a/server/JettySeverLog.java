package com.centny.jetty4a.server;

import java.util.Properties;

import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.Logger;

import android.util.Log;

/**
 * the JettyServerLog for android console log.
 * 
 * @author Centny.
 * 
 */
public class JettySeverLog extends AbstractLogger {
	public static final int LEVEL_ALL = 0;
	public static final int LEVEL_DEBUG = 1;
	public static final int LEVEL_INFO = 2;
	public static final int LEVEL_WARN = 3;
	private static int CFG_LEVEL;
	static {
		String level = System.getProperties().getProperty("", "INFO");
		if (level.equals("ALL")) {
			CFG_LEVEL = LEVEL_ALL;
		} else if (level.equals("DEBUG")) {
			CFG_LEVEL = LEVEL_DEBUG;
		} else if (level.equals("INFO")) {
			CFG_LEVEL = LEVEL_INFO;
		} else if (level.equals("WARN")) {
			CFG_LEVEL = LEVEL_WARN;
		} else {
			CFG_LEVEL = LEVEL_INFO;
		}
	}
	private String tag;
	private String name;
	private Properties props;
	private boolean debugEnabled;
	private int level;

	public JettySeverLog() {
		this(null);
	}

	public JettySeverLog(String name) {
		this(name, null);
	}

	public JettySeverLog(String name, Properties props) {
		this.name = name;
		this.props = props;
		this.tag = this.name == null ? "" : this.name;
		this.level = CFG_LEVEL;
	}

	private String format(String fmt, Object... args) {
		fmt = fmt.replaceAll("\\{\\}", "%s");
		return String.format(fmt, args);
	}

	public Properties getProps() {
		return props;
	}

	@Override
	public void debug(Throwable arg0) {
		if (this.level <= LEVEL_DEBUG) {
			Log.d(this.tag, "", arg0);
		}
	}

	@Override
	public void debug(String arg0, Object... arg1) {
		if (this.level <= LEVEL_DEBUG) {
			Log.d(this.tag, format(arg0, arg1));
		}
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_DEBUG) {
			Log.d(this.tag, arg0, arg1);
		}
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
		if (this.level <= LEVEL_INFO) {
			Log.i(this.tag, "", arg0);
		}
	}

	@Override
	public void info(String arg0, Object... arg1) {
		if (this.level <= LEVEL_INFO) {
			Log.i(this.tag, format(arg0, arg1));
		}
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_INFO) {
			Log.i(this.tag, arg0, arg1);
		}
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
		if (this.level <= LEVEL_WARN) {
			Log.w(this.tag, "", arg0);
		}
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		if (this.level <= LEVEL_WARN) {
			Log.w(this.tag, format(arg0, arg1));
		}
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_WARN) {
			Log.w(this.tag, arg0, arg1);
		}
	}

	@Override
	protected Logger newLogger(String arg0) {
		return new JettySeverLog(arg0);
	}

}
