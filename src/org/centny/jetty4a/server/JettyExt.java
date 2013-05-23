package org.centny.jetty4a.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidParameterException;

import org.eclipse.jetty.util.IO;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.Environment;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * the jetty4a external method for android adapter.
 * 
 * @author Centny.
 * 
 */
public class JettyExt {
	/**
	 * create a server in port by system properties.
	 * 
	 * @param port
	 *            target port.
	 * @return the JettyServer instance.
	 */
	public static JettyServer createServer(int port) {
		File wsdir = new File(System.getProperties().getProperty("J4A_WDIR"));
		File dpdir = new File(System.getProperties().getProperty("J4A_DDIR"));
		if (wsdir.exists() && dpdir.exists()) {
			return new JettyServer(wsdir, dpdir, port);
		} else {
			throw new InvalidParameterException(
					"workspace or deploy not exist.");
		}
	}

	/**
	 * call all initial method.
	 */
	public static void loadAll() {
		System.out.println("Configure Jetty4A....");
		initLogback();
	}

	/*
	 * configure all.
	 */
	static {
		loadJett4ACfg();
		initJettyServerEnv();
		Jetty4ALog.loadCfg(System.getProperties());
	}

	/**
	 * load jetty4a.properties to system properties.
	 */
	public static void loadJett4ACfg() {
		URL testProps = JettyExt.class.getClassLoader().getResource(
				"jetty4a.properties");
		if (testProps != null) {
			InputStream in = null;
			try {
				in = testProps.openStream();
				System.getProperties().load(in);
			} catch (IOException e) {
				System.err.println("Unable to load " + testProps);
				e.printStackTrace(System.err);
			} finally {
				IO.close(in);
			}
		}
	}

	/**
	 * initial JettyServer.
	 */
	public static void initJettyServerEnv() {
		File edir = new File(Environment.getExternalStorageDirectory(),
				"jetty4a");
		if (!edir.exists()) {
			edir.mkdirs();
		}
		System.getProperties().setProperty("J4A_EDIR", edir.getAbsolutePath());
		//
		File ddir = new File(edir, "webapp");
		if (!ddir.exists()) {
			ddir.mkdirs();
		}
		System.getProperties().setProperty("J4A_DDIR", ddir.getAbsolutePath());
		//
		File ldir = new File(edir, "log");
		if (!ldir.exists()) {
			ldir.mkdirs();
		}
		System.getProperties().setProperty("J4A_LDIR", ldir.getAbsolutePath());
		//
		File cdir = new File(edir, "config");
		if (!cdir.exists()) {
			cdir.mkdirs();
		}
		System.getProperties().setProperty("J4A_CDIR", cdir.getAbsolutePath());
		//
	}

	/**
	 * initial JettyServer workspace folder.
	 * 
	 * @param wrapper
	 *            the ContextWrapper.
	 */
	public static void initJServerWs(Context ctx) {
		File wdir = new File(ctx.getFilesDir(), "wsdir");
		if (!wdir.exists()) {
			wdir.mkdirs();
		}
		System.getProperties().setProperty("J4A_WDIR", wdir.getAbsolutePath());
	}

	/**
	 * initial log back.
	 */
	public static void initLogback() {
		File lf = new File(System.getProperties().getProperty("J4A_CDIR"),
				"logback.xml");
		InputStream in = null;
		if (lf.exists()) {
			try {
				in = new FileInputStream(lf);
			} catch (FileNotFoundException e) {
				e.printStackTrace(System.err);
				System.err.println("Unable to load " + lf.getAbsolutePath());
				IO.close(in);
				return;
			}
		} else {
			URL testProps = JettyExt.class.getClassLoader().getResource(
					"logback.xml");
			if (testProps != null) {
				in = null;
				try {
					in = testProps.openStream();
				} catch (IOException e) {
					System.err.println("Unable to load " + testProps);
					e.printStackTrace(System.err);
					IO.close(in);
					return;
				}
			} else {
				System.err.println("Unable to load defalut logback.xml file");
				IO.close(in);
				return;
			}
		}
		try {
			LoggerContext lc = (LoggerContext) LoggerFactory
					.getILoggerFactory();
			lc.reset();
			JoranConfigurator config = new JoranConfigurator();
			config.setContext(lc);
			config.doConfigure(in);
		} catch (JoranException e) {
			e.printStackTrace();
		}
		IO.close(in);
	}
	// /**
	// * initial log4j.<br/>
	// * it dependent initJettyServerEnv.
	// */
	// public static void initLog4j() {
	// File lf = new File(System.getProperties().getProperty("J4A_CDIR"),
	// "log4j.properties");
	// Properties log4j = new Properties();
	// if (lf.exists()) {
	// InputStream in = null;
	// try {
	// in = new FileInputStream(lf);
	// log4j.load(in);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// IO.close(in);
	// }
	// } else {
	// URL testProps = JettyExt.class.getClassLoader().getResource(
	// "log4j-default.properties");
	// if (testProps != null) {
	// InputStream in = null;
	// try {
	// in = testProps.openStream();
	// log4j.load(in);
	// } catch (IOException e) {
	// System.err.println("Unable to load " + testProps);
	// e.printStackTrace(System.err);
	// } finally {
	// IO.close(in);
	// }
	// } else {
	// System.err
	// .println("can't find the log4j-default.properties file");
	// }
	// }
	// PropertyConfigurator.configure(log4j);
	// }
}
