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

	/**
	 * Get the Logging Level for the provided log name. Using the FQCN first,
	 * then each package segment from longest to shortest.
	 * 
	 * @param props
	 *            the properties to check
	 * @param name
	 *            the name to get log for
	 * @return the logging level
	 */
	public static int getLoggingLevel(Properties props, final String name) {
		// Calculate the level this named logger should operate under.
		// Checking with FQCN first, then each package segment from longest to
		// shortest.
		String nameSegment = name;

		while ((nameSegment != null) && (nameSegment.length() > 0)) {
			String levelStr = props.getProperty(nameSegment + ".LEVEL");
			// System.err.printf("[StdErrLog.CONFIG] Checking for property [%s.LEVEL] = %s%n",nameSegment,levelStr);
			int level = getLevelId(nameSegment + ".LEVEL", levelStr);
			if (level != (-1)) {
				return level;
			}

			// Trim and try again.
			int idx = nameSegment.lastIndexOf('.');
			if (idx >= 0) {
				nameSegment = nameSegment.substring(0, idx);
			} else {
				nameSegment = null;
			}
		}

		// Default Logging Level
		return CFG_LEVEL;
	}

	protected static int getLevelId(String levelSegment, String levelName) {
		if (levelName == null) {
			return -1;
		}
		String levelStr = levelName.trim();
		if ("ALL".equalsIgnoreCase(levelStr)) {
			return LEVEL_ALL;
		} else if ("DEBUG".equalsIgnoreCase(levelStr)) {
			return LEVEL_DEBUG;
		} else if ("INFO".equalsIgnoreCase(levelStr)) {
			return LEVEL_INFO;
		} else if ("WARN".equalsIgnoreCase(levelStr)) {
			return LEVEL_WARN;
		}

		System.err.println("Unknown StdErrLog level [" + levelSegment + "]=["
				+ levelStr
				+ "], expecting only [ALL, DEBUG, INFO, WARN] as values.");
		return -1;
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
		this.level = getLoggingLevel(System.getProperties(), this.name);
		for (Class<?> cls : LOG_CLASSES) {
			try {
				AbstractLogger log;
				if (this.name == null) {
					log = (AbstractLogger) cls.newInstance();
				} else {
					Constructor<?> con = cls.getConstructor(String.class);
					log = (AbstractLogger) con.newInstance(this.name);
				}
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
				log.info(arg0);
			}
		}
	}

	@Override
	public void info(String arg0, Object... arg1) {
		if (this.level <= LEVEL_INFO) {
			for (AbstractLogger log : this.logs) {
				log.info(arg0, arg1);
			}
		}
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_INFO) {
			for (AbstractLogger log : this.logs) {
				log.info(arg0, arg1);
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
				log.warn(arg0);
			}
		}
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		if (this.level <= LEVEL_WARN) {
			for (AbstractLogger log : this.logs) {
				log.warn(arg0, arg1);
			}
		}
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		if (this.level <= LEVEL_WARN) {
			for (AbstractLogger log : this.logs) {
				log.warn(arg0, arg1);
			}
		}
	}

	@Override
	protected Logger newLogger(String arg0) {
		return new Jetty4ALog(arg0);
	}

}
