package org.centny.jetty4a.server.dev;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.centny.jetty4a.server.api.JettyServer;
import org.centny.jetty4a.server.cfg.JettyBaseCfg;
import org.centny.jetty4a.server.log.Jetty4ALog;

/**
 * run Jetty server in web project for develop.
 * 
 * @author Centny.
 * 
 */
public class JettyDevServer extends JettyServer {

	/**
	 * the default constructor.
	 * 
	 * @param wsdir
	 *            the workspace directory.
	 * @param deploy
	 *            the deploy directory.
	 */
	public JettyDevServer(File wsdir, File deploy) {
		super(wsdir, deploy);
		this.loadWebApp(new File("."), new File("WebContent/WEB-INF"));
	}

	/**
	 * the default constructor.
	 * 
	 * @param wsdir
	 *            the workspace directory.
	 * @param deploy
	 *            the deploy directory.
	 * @param port
	 *            the listen port.
	 */
	public JettyDevServer(File wsdir, File deploy, int port) {
		super(wsdir, deploy, port);
		this.loadWebApp(new File("."), new File("WebContent/WEB-INF"));
	}

	/**
	 * initial ENV for develop in web project.
	 */
	public static void initWebDev() {
		JettyBaseCfg.loadJett4ACfg();
		Jetty4ALog.loadCfg(System.getProperties());
		File root = new File("Jetty4ADev");
		if (!root.exists()) {
			root.mkdirs();
		}
		JettyBaseCfg.initJ4ARunEnv(root);
		File wdir = new File(root, "wsdir");
		if (!wdir.exists()) {
			wdir.mkdirs();
		}
		JettyBaseCfg.initWsDir(wdir);

	}

	@Override
	protected ClassLoader buildConnectorClassLoader(File jar, ClassLoader tcl) {
		try {
			URLClassLoader ucl = null;
			ucl = new URLClassLoader(new URL[] { new URL("file://"
					+ jar.getAbsolutePath()) }, tcl);
			return ucl;
		} catch (Exception e) {
			e.printStackTrace();
			return tcl;
		}
	}

}
