package org.centny.jetty4a.server.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * the dnsdynamic.com http client.
 * 
 * @author Centny.
 * 
 */
public abstract class DnsDynamic extends TimerTask {

	public static interface Listener {
		void update(DnsDynamic dns);
	}

	protected String usr;
	protected String pwd;
	protected String host;
	protected String myip;
	protected long period;
	protected Timer timer;
	protected Properties cfg;

	private boolean running;
	private long remain;
	private List<Listener> listeners = new ArrayList<Listener>();
	private Logger log = Log.getLogger(DnsDynamic.class);

	public DnsDynamic() {
		this.running = false;
		this.period = 0;
		this.cfg = new Properties(System.getProperties());
	}

	public boolean isTimerRunning() {
		return this.timer != null;
	}

	public void require() {
		synchronized (this) {
			this.remain = 0;
		}
	}

	public void startTimer() {
		if (this.period < 5000) {
			this.log.warn("the dns period configure less 5000,DnsDynamic will not start.");
			return;
		}
		if (this.listeners.isEmpty()) {
			this.log.warn("the dynamic dns listener not found.");
			return;
		}
		synchronized (this) {
			this.remain = 0;
		}
		this.timer = new Timer();
		this.timer.schedule(this, 0, 5000);
	}

	public void stopTimer() {
		if (this.timer == null) {
			return;
		}
		this.timer.cancel();
		this.timer = null;
	}

	public void loadDnsConfig() {
		String cdir = System.getProperty(ServerListener.J4A_CDIR);
		File dns = new File(cdir, "DnsDynamic.properties");
		if (!dns.exists()) {
			return;
		}
		FileInputStream is = null;
		try {
			is = new FileInputStream(dns);
			this.cfg.load(is);
		} catch (Exception e) {

		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		if (this.cfg.containsKey("USER")) {
			this.usr = this.cfg.getProperty("USER");
		}
		if (this.cfg.containsKey("PASS")) {
			this.usr = this.cfg.getProperty("PASS");
		}
		if (this.cfg.containsKey("HOST")) {
			this.usr = this.cfg.getProperty("HOST");
		}
		if (this.cfg.containsKey("PERIOD")) {
			this.usr = this.cfg.getProperty("PERIOD");
		}
	}

	public void add(Listener l) {
		this.listeners.add(l);
	}

	@Override
	public void run() {
		synchronized (this) {
			if (this.remain > 0) {
				this.remain -= 5000;
				return;
			}
			this.running = true;
		}
		this.log.debug("processing " + this.listeners.size() + " listener...");
		this.preUpdate();
		for (Listener l : this.listeners) {
			try {
				l.update(this);
			} catch (Exception e) {
				this.log.warn("running listener error:", e);
			}
		}
		this.postUpdate();
		synchronized (this) {
			this.running = false;
			this.remain = this.period;
		}
	}

	protected abstract String createBase64(String tar);

	protected void preUpdate() {

	}

	protected void postUpdate() {

	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		synchronized (this) {
			return running;
		}
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the myip
	 */
	public String getMyip() {
		return myip;
	}

	/**
	 * @param myip
	 *            the myip to set
	 */
	public void setMyip(String myip) {
		this.myip = myip;
	}

	/**
	 * @param usr
	 *            the usr to set
	 */
	public void setUsr(String usr) {
		this.usr = usr;
	}

	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}

	/**
	 * @return the cfg
	 */
	public Properties getCfg() {
		return cfg;
	}

}
