package org.centny.jetty4a.server.api;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * the default connector builder.
 * 
 * @author Centny.
 * 
 */
public class DefaultConnectorBuilder implements ConnectorBuilder {

	/**
	 * the logger.
	 */
	private Logger log = Log.getLogger(DefaultConnectorBuilder.class);

	@Override
	public Connector create(int port) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(10);
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setThreadPool(threadPool);
		connector.setPort(port);
		this.log.debug("create a connector in port " + port
				+ " by max threads 10");
		return connector;
	}

}
