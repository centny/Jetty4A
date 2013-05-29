package org.centny.jetty4a.server;

import java.io.File;

import org.centny.jetty4a.server.api.JettyServer;

import dalvik.system.DexClassLoader;

public class J4AServer extends JettyServer {


	public J4AServer(File wsdir, File deploy) {
		super(wsdir, deploy);
	}

	public J4AServer(File wsdir, File deploy, int port) {
		super(wsdir, deploy, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.centny.jetty4a.server.api.JettyServer#buildClassLoader(java.io.File,
	 * java.lang.ClassLoader)
	 */
	@Override
	protected ClassLoader buildClassLoader(File root, ClassLoader tcl) {
		ClassLoader pcl = super.buildClassLoader(root, tcl);
		File tf = new File(root, "classes.jar");
		if (tf.exists()) {
			DexClassLoader dcl = null;
			dcl = new DexClassLoader(tf.getAbsolutePath(),
					root.getAbsolutePath(), null, pcl);
			return dcl;
		} else {
			return pcl;
		}
	}

}
