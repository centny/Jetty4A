package org.centny.jetty4a.server;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.Logger;

public class Jetty4ALog extends AbstractLogger {
	public static final int LEVEL_ALL = 0;
	public static final int LEVEL_DEBUG = 1;
	public static final int LEVEL_INFO = 2;
	public static final int LEVEL_WARN = 3;
	private static int CFG_LEVEL = LEVEL_INFO;
	private static Set<Class<?>> LOG_CLASSES = new HashSet<Class<?>>();

	public static void loadCfg(Properties pro) {
		if (pro == null) {
			return;
		}
		String level = pro.getProperty("ANDROID_LOG_LEVEL", "INFO");
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
		String cls = pro.getProperty("ANDROID_LOG_CLASSES",
				"com.centny.jetty4.server.JettyLogCatLog");
		String[] classes = cls.split("[\\;\\:\\|]");
		for (String name : classes) {
			try {
				Class<?> logc = Loader.loadClass(Jetty4ALog.class, name.trim());
				LOG_CLASSES.add(logc);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private String name;
	private boolean debugEnabled;
	private int level;
	private Set<AbstractLogger> logs = new HashSet<AbstractLogger>();

	public Jetty4ALog() {
		this(null);
	}

	public Jetty4ALog(String name) {
		this.name = name;
		this.level = CFG_LEVEL;
		for (Class<?> cls : LOG_CLASSES) {
			try {
				Constructor<?> con = cls.getConstructor(String.class);
				AbstractLogger log = (AbstractLogger) con
						.newInstance(this.name);
				this.logs.add(log);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void debug(Throwable arg0) {
		if (this.level <= LEVEL_DEBUG) {
			for (AbstractLogger log : this.logs) {
				log.debug(arg0);
			}
		}
	}

	@Override
	public void debug(String arg0, Object... arg1) {
		if (this.level <= LEVEL_DEBUG) {
			for (AbstractLogger log : this.logs) {
				log.debug(arg0, arg1);
			}
		}
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_DEBUG) {
			for (AbstractLogger log : this.logs) {
				log.debug(arg0, arg1);
			}
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
			for (AbstractLogger log : this.logs) {
				log.debug(arg0);
			}
		}
	}

	@Override
	public void info(String arg0, Object... arg1) {
		if (this.level <= LEVEL_INFO) {
			for (AbstractLogger log : this.logs) {
				log.debug(arg0, arg1);
			}
		}
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_INFO) {
			for (AbstractLogger log : this.logs) {
				log.debug(arg0, arg1);
			}
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
			for (AbstractLogger log : this.logs) {
				log.debug(arg0);
			}
		}
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		if (this.level <= LEVEL_WARN) {
			for (AbstractLogger log : this.logs) {
				log.debug(arg0, arg1);
			}
		}
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_WARN) {
			for (AbstractLogger log : this.logs) {
				log.debug(arg0, arg1);
			}
		}
	}

	@Override
	protected Logger newLogger(String arg0) {
		return new Jetty4ALog(arg0);
	}

}
