package org.centny.wdev;

import java.io.File;
import java.util.Properties;

import org.centny.jetty4a.server.api.ServerListener;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * the server creator for WDir and log.
 * 
 * @author Centny.
 * 
 */
public class WebDev extends ServerListener {
	// logger.
	private Logger log = Log.getLogger(WebDev.class);
	// the app root path and configure root path.
	private File root, croot;
	// the login service.
	private HashLoginService hls;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.centny.jetty4a.server.api.ServerListener#create(java.lang.ClassLoader
	 * , java.util.Properties)
	 */
	@Override
	public Handler create(ClassLoader loader, Properties webp) {
		// adding the external WebApp for workspace path.
		WebAppContext wapp;
		wapp = new WebAppContext();
		wapp.setParentLoaderPriority(true);
		String wpath = webp.getProperty(J4A_WDIR);
		wapp.setResourceBase(wpath);
		wapp.setContextPath("/wdir");
		File web = new File(this.croot, "WDirWeb.xml");
		if (web.exists()) {
			wapp.setDescriptor(web.getAbsolutePath());
		}
		if (this.hls != null) {
			wapp.getSecurityHandler().setLoginService(hls);
		}
		return wapp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.centny.jetty4a.server.api.ServerListener#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.centny.jetty4a.server.api.ServerListener#init(java.io.File)
	 */
	@Override
	public void init(File root, File croot, File droot) {
		this.root = root;
		this.croot = croot;
		this.log.info("initial app root:" + this.root.getAbsolutePath()
				+ ",configure root:" + this.croot.getAbsolutePath());
		File wrealm = new File(this.croot, "Realm.properties");
		if (wrealm.exists()) {
			this.log.info("initial WDev Realm...");
			hls = new HashLoginService("WDevRealm");
			hls.setConfig(wrealm.getAbsolutePath());
			hls.setRefreshInterval(60);
			try {
				hls.start();
			} catch (Exception e) {
				this.log.warn("initial WDev Realm error", e);
				this.hls = null;
			}
		} else {
			this.log.info("WDev Realm file not found...");
			this.hls = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.centny.jetty4a.server.api.ServerListener#initWebApp(org.eclipse.jetty
	 * .webapp.WebAppContext)
	 */
	@Override
	public void initWebApp(WebAppContext wapp, Properties webp) {
		this.log.info("initial WebLog...");
		wapp.setResourceBase(webp.getProperty(J4A_LDIR));
		wapp.setContextPath("/log");
		if (this.hls != null) {
			wapp.getSecurityHandler().setLoginService(hls);
		}
	}

}
