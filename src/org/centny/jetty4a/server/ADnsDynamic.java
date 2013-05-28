package org.centny.jetty4a.server;

import java.io.File;

import org.centny.cny4a.util.Util;
import org.centny.jetty4a.server.api.DnsDynamic;
import org.centny.jetty4a.server.api.ServerListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Base64;
import dalvik.system.DexClassLoader;

/**
 * the implemented DnsDynamic class for android.
 * 
 * @author Centny.
 * 
 */
public class ADnsDynamic extends DnsDynamic {

	/**
	 * shared instance.
	 */
	private static ADnsDynamic sharedInstance_;

	/**
	 * get shared instance.
	 * 
	 * @return the instance.
	 */
	public static ADnsDynamic sharedInstance() {
		if (sharedInstance_ == null) {
			sharedInstance_ = new ADnsDynamic();
		}
		return sharedInstance_;
	}

	//
	private NetworkChangedReceiver receiver;
	private Activity aty;
	private boolean only4Wifi;

	/**
	 * the default constructor.
	 */
	public ADnsDynamic() {
		this.only4Wifi = true;
		this.loadExtListener();
	}

	public void loadExtListener() {
		try {
			String ddl = System.getProperty("j4a.dnsdynamic.class");
			if (ddl == null || ddl.trim().length() < 1) {
				return;
			}
			String wdir = System.getProperty(ServerListener.J4A_WDIR);
			String cdir = System.getProperty(ServerListener.J4A_CDIR);
			File ddex = new File(cdir, "DnsDynamic.jar");
			ClassLoader cl = this.getClass().getClassLoader();
			if (ddex.exists()) {
				DexClassLoader dcl = new DexClassLoader(ddex.getAbsolutePath(),
						wdir, null, cl);
				cl = dcl;
			}
			Class<?> cls = cl.loadClass(ddl);
			DnsDynamic.Listener l = (Listener) cls.newInstance();
			this.add(l);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	@Override
	protected String createBase64(String tar) {
		return Base64.encodeToString(tar.getBytes(), Base64.DEFAULT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.centny.jetty4a.server.api.DnsDynamic#preUpdate()
	 */
	@Override
	protected void preUpdate() {
		if (this.aty != null) {
			this.setMyip(Util.localIpAddress(this.aty, this.only4Wifi));
		} else {
			this.setMyip(null);
		}
	}

	public void startNetworkListener(Activity aty) {
		this.receiver = new NetworkChangedReceiver(this);
		this.aty = aty;
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.aty.registerReceiver(this.receiver, filter);
	}

	public void stopNetworkListener() {
		if (this.receiver == null) {
			return;
		}
		this.aty.unregisterReceiver(this.receiver);
		this.receiver = null;
		this.aty = null;
	}

	private void onNetworkChanged() {
		if (this.isRunning()) {
			return;
		}
		this.require();
	}

	/**
	 * the network status change listener.
	 * 
	 * @author Centny.
	 * 
	 */
	private static class NetworkChangedReceiver extends BroadcastReceiver {
		/**
		 * the dns.
		 */
		private ADnsDynamic dns;

		/**
		 * the default constructor.
		 * 
		 * @param dns
		 *            the dns dynamic.
		 */
		public NetworkChangedReceiver(ADnsDynamic dns) {
			super();
			this.dns = dns;
		}

		@SuppressLint("InlinedApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
						.getAction())) {
					return;
				}
				this.dns.onNetworkChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the only4Wifi
	 */
	public boolean isOnly4Wifi() {
		return only4Wifi;
	}

	/**
	 * @param only4Wifi
	 *            the only4Wifi to set
	 */
	public void setOnly4Wifi(boolean only4Wifi) {
		this.only4Wifi = only4Wifi;
	}

}
