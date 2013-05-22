package com.centny.jetty4a.server;

import java.io.File;
import java.util.Properties;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * the base class for listen server context create context handle and create the
 * custom context handler.<br/>
 * the implement class must have an empty argument constructor.<br/>
 * add class name to web.properties file,the sever will auto create it and call
 * it.
 * 
 * @author Centny.
 * 
 */
public class ServerListener {

	/**
	 * call it when server initial context.
	 * 
	 * @param root
	 *            the root path for context.
	 */
	public void init(File root) {

	}

	/**
	 * call to create custom context handler.
	 * 
	 * @param loader
	 *            the class loader already load all classes in lib and classes
	 *            folder.
	 * @param webp
	 *            the web.properties.
	 * @return a context handler.
	 */
	public Handler create(ClassLoader loader, Properties webp) {
		return null;
	}

	/**
	 * call it when initial a WebAppContext by web.xml.
	 * 
	 * @param wapp
	 *            the target WebAppContext.
	 */
	public void initWebApp(WebAppContext wapp) {

	}

	/**
	 * call it when server destroy.
	 */
	public void destroy() {

	}
}
