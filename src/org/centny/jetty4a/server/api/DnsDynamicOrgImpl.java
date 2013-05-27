package org.centny.jetty4a.server.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class DnsDynamicOrgImpl implements DnsDynamic.Listener {
	private Logger log = Log.getLogger(DnsDynamicOrgImpl.class);

	@Override
	public void update(DnsDynamic dns) {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			if (dns.usr == null || dns.usr.trim().length() < 1) {
				return;
			}
			if (dns.pwd == null || dns.pwd.trim().length() < 1) {
				return;
			}
			if (dns.host == null || dns.host.trim().length() < 1) {
				return;
			}
			if (dns.myip == null || dns.myip.trim().length() < 1) {
				return;
			}
			String turl = String.format(
					"https://www.dnsdynamic.org/api?hostname=%s&myip=%s",
					dns.host, dns.myip);
			this.log.debug("start request:" + turl);
			String upwd = String.format("%s:%s", dns.usr, dns.pwd);
			String encoding = dns.createBase64(upwd);
			URL url = new URL(turl);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setConnectTimeout(10000);
			http.setInstanceFollowRedirects(true);
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setRequestProperty("Authorization", "Basic " + encoding);
			is = http.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			this.log.info("DnsDynamic response:" + sb.toString());
		} catch (Exception e) {
			this.log.warn("DnsDynamic request error:" + e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
