package org.centny.jetty4a;

import java.util.Timer;
import java.util.TimerTask;

import org.centny.cny4a.util.Util;
import org.centny.jetty4a.server.ADnsDynamic;
import org.centny.jetty4a.server.J4AServer;
import org.centny.jetty4a.server.J4AServer.ServerStatus;
import org.centny.jetty4a.server.JettyCfgAndroid;
import org.centny.jetty4a.server.log.MemoryLog;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.centny.jetty4a.R;

/**
 * the main activity.
 * 
 * @author Centny
 * 
 */
public class JettyActivity extends Activity {

	static {
		System.out.println(JettyCfgAndroid.class);// for load static configure.
		JettyCfgAndroid.loadAll();
	}

	private TextView logv;
	private TextView titlev;
	private TextView statusv;
	private Button startBtn;
	private Timer logTimer = new Timer();
	private static LogHandler logh = new LogHandler();
	private static StatusHandler statush = new StatusHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jetty);
		this.logv = (TextView) this.findViewById(R.id.log_v);
		this.titlev = (TextView) this.findViewById(R.id.title_v);
		this.startBtn = (Button) this.findViewById(R.id.startBtn);
		this.statusv = (TextView) this.findViewById(R.id.status_v);
		logh.logv = this.logv;
		statush.startBtn = this.startBtn;
		statush.statusv = this.statusv;
		statush.aty = this;
		this.titlev.setText("Jetty Server");
		this.startLogTimer();
		//
		JettyCfgAndroid.initJServerWs(this.getApplicationContext());
		ADnsDynamic dd = ADnsDynamic.sharedInstance();
		dd.setHost("git.dnsd.me");
		dd.setUsr("centny@gmail.com");
		dd.setPwd("wsh123456");
		dd.setPeriod(300000);
		dd.startTimer();
		dd.startNetworkListener(this);
		J4AServer.createSharedServer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jetty, menu);
		return true;
	}

	/**
	 * click for start Jetty server or stop.
	 * 
	 * @param v
	 *            button view.
	 */
	public void onClk(View v) {
		if (J4AServer.isSharedServerStartted()) {
			J4AServer.startSharedServer(statush);
		} else {
			J4AServer.stopSharedServer(statush);
		}
		// WifiManager wifimanage = (WifiManager) this
		// .getSystemService(Context.WIFI_SERVICE);// 获取WifiManager
		//
		// if (!wifimanage.isWifiEnabled()) {
		// wifimanage.setWifiEnabled(true);
		// }
		//
		// WifiInfo wifiinfo = wifimanage.getConnectionInfo();
		// String ip = intToIp(wifiinfo.getIpAddress());
		// DnsDynamic dd = ADnsDynamic.sharedInstance();
		// try {
		// dd.setHost("git.dnsd.me");
		// dd.setMyip(ip);
		// dd.setUsr("centny@gmail.com");
		// dd.setPwd("wsh123456");
		// dd.startTimer(300000);
		// } catch (Exception e) {
		//
		// }
		// Log.d("File",
		// getApplicationContext().getFilesDir().getAbsolutePath());
		// for (String n : new File(getApplicationContext().getFilesDir()
		// .getAbsolutePath()).list()) {
		// Log.d("File", n);
		// "[ss]".matches("^\\[\\s*remote\\s*\\]$");
		// }
		// if (js != null) {
		// try {
		// js.stop();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// js = null;
		// Log.d("Server", "stopped");
		// ((Button) v).setText("Stopped");
		// } else {
		// File wdir = new File(Environment.getExternalStorageDirectory(),
		// "webapp");
		// if (!wdir.exists()) {
		// wdir.mkdirs();
		// }
		// Log.d("Server", this.getApplicationContext().getFilesDir()
		// .getAbsolutePath());
		// Log.d("Server", wdir.getAbsolutePath());
		// ((Button) v).setText("Started");
		// js = JettyServer.createServer(J4AServer.class);
		// QueuedThreadPool threadPool = new QueuedThreadPool();
		// threadPool.setMaxThreads(10);
		// SelectChannelConnector connector = new SelectChannelConnector();
		// connector.setThreadPool(threadPool);
		// connector.setPort(8080);
		// js.addConnector(connector);
		// try {
		// Log.d("Server", "check deploy...");
		// js.checkDeploy();
		// Log.d("Server", "load web context...");
		// js.loadWebContext();
		// Log.d("Server", "starting...");
		// js.start();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
	}

	private static class StatusHandler extends Handler {
		public Activity aty;
		public TextView statusv;
		public Button startBtn;

		private String localListener() {
			String ip = Util.localIpAddress(aty);
			if (ip == null) {
				ip = "0.0.0.0";
			}
			return ip + ":" + J4AServer.listenPort();
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.obj == null || !(msg.obj instanceof ServerStatus)) {
				return;
			}
			ServerStatus status = (ServerStatus) msg.obj;
			this.startBtn.setText(status.toString());
			this.statusv.setText("(" + status.toString() + ","
					+ this.localListener());
		}

	}

	public void startLogTimer() {
		this.logTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				logh.sendEmptyMessage(0);
			}
		}, 0, 1500);
	}

	private static class LogHandler extends Handler {
		public TextView logv;
		float maxWidth = 0;

		@Override
		public void handleMessage(Message msg) {
			Paint p = logv.getPaint();
			float width = p.measureText(MemoryLog.longedsLine());
			if (width > maxWidth) {
				maxWidth = width;
				logv.getLayoutParams().width = (int) maxWidth;
			}
			logv.setText(MemoryLog.allLog());
		}

	};
}
