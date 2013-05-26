package org.centny.jetty4a.server.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.centny.jetty4a.server.api.ServerListener;

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

		InputStream in = null;
		try {
			String cdir = System.getProperty(ServerListener.J4A_CDIR);
			if (cdir != null && cdir.length() > 0) {
				File j4a = new File(cdir, "jetty4a.properties");
				if (j4a.exists()) {
					in = new FileInputStream(j4a);
				}
			}
			if (in == null) {
				URL testProps = JettyBaseCfg.class.getClassLoader()
						.getResource("jetty4a.properties");
				if (testProps != null) {
					in = testProps.openStream();
				}
			}
			if (in == null) {
				return;
			}
			System.getProperties().load(in);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {

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
