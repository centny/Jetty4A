package org.centny.wlog;

import java.io.File;
import java.util.Properties;

import org.centny.jetty4a.server.api.ServerListener;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebLog extends ServerListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.centny.jetty4a.server.api.ServerListener#create(java.lang.ClassLoader
	 * , java.util.Properties)
	 */
	@Override
	public Handler create(ClassLoader loader, Properties webp) {
		WebAppContext wapp = new WebAppContext();
		wapp.setParentLoaderPriority(true);
		String lpath = webp.getProperty(J4A_LDIR);
		wapp.setResourceBase(lpath);
		wapp.setContextPath("/log");
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
	public void init(File root) {
		super.init(root);
	}

}
