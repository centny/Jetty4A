package org.centny.wdev.test;

import org.centny.jetty4a.server.api.JettyServer;
import org.centny.jetty4a.server.dev.JettyDevServer;
import org.junit.Test;

public class WebDevTest {
	private static int listenPort__ = 8080;

	@Test
	public void testMgw() throws Exception {
		JettyDevServer.initWebDev();
		try {
			String sport = System.getProperty("J4A_LISTEN_PORT");
			listenPort__ = Integer.parseInt(sport);
		} catch (Exception e) {
			listenPort__ = 8080;
		}
		JettyServer jds = JettyServer.createServer(JettyDevServer.class,
				listenPort__);
		jds.start();
		jds.join();
	}
}
