package com.centny.jetty4a;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.centny.jetty4a.server.JettyExt;
import com.centny.jetty4a.server.JettyServer;

/**
 * the main activity.
 * @author Centny
 *
 */
public class JettyActivity extends Activity {

	static {
		JettyExt.loadJettyCfg();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jetty);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jetty, menu);
		return true;
	}

	/**
	 * the Jetty Server.
	 */
	JettyServer js = null;

	/**
	 * click for start Jetty server or stop.
	 * 
	 * @param v
	 *            button view.
	 */
	public void onClk(View v) {
		Log.d("File", getApplicationContext().getFilesDir().getAbsolutePath());
		for (String n : new File(getApplicationContext().getFilesDir()
				.getAbsolutePath()).list()) {
			Log.d("File", n);
		}
		if (js != null) {
			try {
				js.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			js = null;
			Log.d("Server", "stopped");
		} else {
			File wdir = new File(Environment.getExternalStorageDirectory(),
					"webapp");
			if (!wdir.exists()) {
				wdir.mkdirs();
			}
			Log.d("Server", this.getApplicationContext().getFilesDir()
					.getAbsolutePath());
			Log.d("Server", wdir.getAbsolutePath());
			js = new JettyServer(this.getApplicationContext().getFilesDir(),
					wdir, 8080);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Log.d("Server", "check deploy...");
						js.checkDeploy();
						Log.d("Server", "load web context...");
						js.loadWebContext();
						Log.d("Server", "starting...");
						js.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
