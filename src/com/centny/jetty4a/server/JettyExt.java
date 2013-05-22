package com.centny.jetty4a.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.Loader;

import android.util.Log;

/**
 * the jetty external method for android adapter.
 * 
 * @author Centny.
 * 
 */
public class JettyExt {
	/**
	 * load Jetty configure to system properties.
	 */
	public static void loadJettyCfg() {
		URL testProps = Loader.getResource(Log.class,
				"jetty-logging.properties", true);
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
}
