package org.centny.j4aext.test;

import java.io.File;

import org.apache.commons.codec.binary.Base64;
import org.centny.jetty4a.server.api.DnsDynamic;
import org.centny.jetty4a.server.dev.JettyDevServer;
import org.junit.Test;

public class DnsDynamicTest extends DnsDynamic {

	@Override
	protected String createBase64(String tar) {
		return new String(Base64.encodeBase64String(tar.getBytes()));
	}

	@Override
	protected ClassLoader externalClassLoader(File jar, ClassLoader parent) {
		return parent;
	}

	@Test
	public void testDnsDynamic() {
		JettyDevServer.initWebDev();
		this.loadDnsConfig();
		this.loadExtListener();
		this.updateDnsDynamic();
	}
}
