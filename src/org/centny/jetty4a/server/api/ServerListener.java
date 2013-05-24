package org.centny.jetty4a.server.api;

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
	 * the key of system properties.<br/>
	 * the Jetty4A external folder where contain configure file,log folder and
	 * other.
	 */
	public static final String J4A_EDIR = "J4A_EDIR";

	/**
	 * the key of system properties.<br/>
	 * the Jetty4A deploy path.
	 */
	public static final String J4A_DDIR = "J4A_DDIR";

	/**
	 * the key of system properties.<br/>
	 * the Jetty4A log path.
	 */
	public static final String J4A_LDIR = "J4A_LDIR";

	/**
	 * the key of system properties.<br/>
	 * the Jetty4A configure path.
	 */
	public static final String J4A_CDIR = "J4A_CDIR";

	/**
	 * the key of system properties.<br/>
	 * the Jetty4A workspace path.
	 */
	public static final String J4A_WDIR = "J4A_WDIR";

	/**
	 * call it when server initial context.
	 * 
	 * @param root
	 *            the root path for WebApp physical path.
	 * @param croot
	 *            the configure root path for WebApp physical path.
	 */
	public void init(File root, File croot) {

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
	 * call it after a WebAppContext by web.xml is initialed.
	 * 
	 * @param wapp
	 *            the target WebAppContext.
	 * @param webp
	 *            the web.properties.
	 */
	public void initWebApp(WebAppContext wapp, Properties webp) {
	}

	/**
	 * call it when server destroy.
	 */
	public void destroy() {

	}
}
