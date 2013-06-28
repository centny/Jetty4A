package org.centny.wdev.test;

import org.centny.jetty4a.server.api.JettyServer;
import org.centny.jetty4a.server.dev.JettyDevServer;
import org.junit.Test;

public class WebDevTest {
	@Test
	public void testMgw() throws Exception {
		JettyDevServer.initWebDev();
		JettyServer jds = JettyServer.createServer(JettyDevServer.class, 8080);
		jds.start();
		jds.join();
	}
}
