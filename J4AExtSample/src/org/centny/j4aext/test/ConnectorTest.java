package org.centny.j4aext.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.centny.jetty4a.server.api.JettyServer;
import org.centny.jetty4a.server.dev.JettyDevServer;
import org.eclipse.jetty.util.IO;
import org.junit.Test;

public class ConnectorTest {
	static {
		URL url = ConnectorTest.class.getResource("log4jetty.properties");
		if (url != null) {
			InputStream is = null;
			try {
				is = url.openStream();
				System.getProperties().load(is);
			} catch (IOException e) {
			}
			IO.close(is);
		}
	}

	@Test
	public void testWLog() throws Exception {
		JettyDevServer.initWebDev();
		JettyServer js = JettyServer.createServer(JettyDevServer.class, 8080);
		js.start();
		js.join();
	}
}
