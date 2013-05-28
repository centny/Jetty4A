package org.centny.jetty4a.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.centny.jetty4a.server.cfg.JettyBaseCfg;
import org.centny.jetty4a.server.log.Jetty4ALog;
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
public class JettyCfgAndroid extends JettyBaseCfg {
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
		initJettyServerEnv();
		JettyBaseCfg.loadJett4ACfg();
		Jetty4ALog.loadCfg(System.getProperties());
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
		JettyBaseCfg.initJ4ARunEnv(edir);
	}

	/**
	 * initial JettyServer workspace folder.
	 * 
	 * @param wrapper
	 *            the ContextWrapper.
	 */
	public static void initJServerWs(Context ctx) {
		File wdir = new File(ctx.getFilesDir(), "wsdir");
		JettyBaseCfg.initWsDir(wdir);
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
			URL testProps = JettyCfgAndroid.class.getClassLoader().getResource(
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
