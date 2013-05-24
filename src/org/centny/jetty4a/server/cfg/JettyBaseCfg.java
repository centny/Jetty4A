package org.centny.jetty4a.server.cfg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.centny.jetty4a.server.api.ServerListener;
import org.eclipse.jetty.util.IO;

/**
 * the Jetty4A base configure method.
 * 
 * @author Centny.
 * 
 */
public class JettyBaseCfg {
	/**
	 * load jetty4a.properties to system properties.
	 */
	public static void loadJett4ACfg() {
		URL testProps = JettyBaseCfg.class.getClassLoader().getResource(
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
	public static void initJ4ARunEnv(File edir) {
		System.getProperties().setProperty(ServerListener.J4A_EDIR,
				edir.getAbsolutePath());
		//
		File ddir = new File(edir, "webapp");
		if (!ddir.exists()) {
			ddir.mkdirs();
		}
		System.getProperties().setProperty(ServerListener.J4A_DDIR,
				ddir.getAbsolutePath());
		//
		File ldir = new File(edir, "log");
		if (!ldir.exists()) {
			ldir.mkdirs();
		}
		System.getProperties().setProperty(ServerListener.J4A_LDIR,
				ldir.getAbsolutePath());
		//
		File cdir = new File(edir, "config");
		if (!cdir.exists()) {
			cdir.mkdirs();
		}
		System.getProperties().setProperty(ServerListener.J4A_CDIR,
				cdir.getAbsolutePath());
		//
		File rdir = new File(edir, "data");
		if (!rdir.exists()) {
			rdir.mkdirs();
		}
		System.getProperties().setProperty(ServerListener.J4A_RDIR,
				rdir.getAbsolutePath());
		//
	}

	/**
	 * initial jetty4a workspace path.
	 * 
	 * @param wdir
	 *            target path.
	 */
	public static void initWsDir(File wdir) {
		if (!wdir.exists()) {
			wdir.mkdirs();
		}
		System.getProperties().setProperty(ServerListener.J4A_WDIR,
				wdir.getAbsolutePath());
	}
}
