package org.centny.j4aext;

import org.centny.jetty4a.server.api.DnsDynamic;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class CustomDnsDynamicImpl implements DnsDynamic.Listener {

	Logger log = Log.getLogger(CustomDnsDynamicImpl.class);

	@Override
	public void update(DnsDynamic dns) {
		this.log.debug("upate.........");
	}

}
