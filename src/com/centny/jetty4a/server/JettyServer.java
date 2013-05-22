package com.centny.jetty4a.server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import dalvik.system.DexClassLoader;

public class JettyServer extends Server {
	private File wsdir;
	private ContextHandlerCollection contexts = new ContextHandlerCollection();
	private Map<String, Handler> servers = new HashMap<String, Handler>();

	public JettyServer(File wsdir, int port) {
		super(port);
		this.wsdir = wsdir;
		if (!this.wsdir.exists()) {
			this.wsdir.mkdirs();
		}
		if (!this.wsdir.exists()) {
			throw new InvalidParameterException("initial server in workspace "
					+ this.wsdir.getAbsolutePath() + " error");
		}
		this.initWebContext();
	}

	private void initWebContext() {
		File[] webdir = this.wsdir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return new File(pathname, "Web.properties").exists();
			}
		});
		if (webdir == null) {
			return;
		}
		for (File wapp : webdir) {
			try {
				Handler h = this.loadWebApp(wapp);
				this.contexts.addHandler(h);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.setHandler(this.contexts);
	}

	private Handler loadWebApp(File root) {
		ContextHandlerCollection chcs = new ContextHandlerCollection();
		File tf;
		tf = new File(root, "libs");
		ClassLoader tcl = this.contexts.getClass().getClassLoader();
		URLClassLoader ucl = null;
		try {
			ucl = new URLClassLoader(
					new URL[] { new URL(tf.getAbsolutePath()) }, tcl);
			tcl = ucl;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		tf = new File(root, "classes.dex");
		DexClassLoader dcl = null;
		if (tf.exists()) {
			dcl = new DexClassLoader(tf.getAbsolutePath(),
					root.getAbsolutePath(), null, tcl);
			tcl = dcl;
		}
		tf = new File(root, "Web.properties");
		Properties webp = new Properties();
		if (tf.exists()) {
			try {
				webp.load(new FileInputStream(tf));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServerListener sl = new ServerListener();
		if (webp.containsKey("ServerListener")) {
			try {
				sl = (ServerListener) tcl.loadClass(
						(String) webp.get("ServerListener")).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sl.init(root);
		try {
			Handler h = sl.create(tcl, webp);
			if (h != null) {
				chcs.addHandler(h);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tf = new File(root, "Web.xml");
		if (tf.exists()) {
			WebAppContext wapp = new WebAppContext();
			wapp.setDescriptor(tf.getAbsolutePath());
			if (webp.contains("WebResourceBase")) {
				wapp.setResourceBase(new File(root, webp
						.getProperty("WebResourceBase")).getAbsolutePath());
			}
			if (webp.containsKey("WebContextPath")) {
				wapp.setContextPath(webp.getProperty("WebResourceBase"));
			}
			wapp.setParentLoaderPriority(true);
			wapp.setClassLoader(tcl);
			sl.initWebApp(wapp);
			chcs.addHandler(wapp);
		}
		String name = root.getName();
		if (webp.containsKey("WebName")) {
			name = webp.getProperty("WebName");
		}
		this.servers.put(name, chcs);
		return chcs;
	}

	/**
	 * @return the wsdir
	 */
	public File getWsdir() {
		return wsdir;
	}

}
