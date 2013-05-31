package org.centny.jetty4a.server.api;

import org.eclipse.jetty.server.Connector;

/**
 * the interface to create a connector for server.<br/>
 * it will be added to org.eclipse.jetty.server.Server.
 * 
 * @author Centny.
 * 
 */
public interface ConnectorBuilder {
	/**
	 * create a connector.
	 * 
	 * @param port
	 *            the configure port will be listened.
	 * @return a connector.
	 */
	Connector create(int port);
}
