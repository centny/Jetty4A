package org.centny.j4aext;

import org.centny.jetty4a.server.api.ConnectorBuilder;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class CustomConnectorBuilder implements ConnectorBuilder {
	Logger log = Log.getLogger(CustomConnectorBuilder.class);

	@Override
	public Connector create(int port) {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(10);
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setThreadPool(threadPool);
		connector.setPort(9000);
		this.log.debug("building.........");
		return connector;
	}

}
