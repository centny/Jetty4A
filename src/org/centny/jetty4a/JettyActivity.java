package org.centny.jetty4a;

import org.centny.jetty4a.server.J4AServer;
import org.centny.jetty4a.server.JettyCfgAndroid;
import org.centny.jetty4a.server.api.JettyServer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jetty);
		JettyCfgAndroid.initJServerWs(this.getApplicationContext());
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
		// Log.d("File",
		// getApplicationContext().getFilesDir().getAbsolutePath());
		// for (String n : new File(getApplicationContext().getFilesDir()
		// .getAbsolutePath()).list()) {
		// Log.d("File", n);
		// }
		if (js != null) {
			try {
				js.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			js = null;
			Log.d("Server", "stopped");
		} else {
			// File wdir = new File(Environment.getExternalStorageDirectory(),
			// "webapp");
			// if (!wdir.exists()) {
			// wdir.mkdirs();
			// }
			// Log.d("Server", this.getApplicationContext().getFilesDir()
			// .getAbsolutePath());
			// Log.d("Server", wdir.getAbsolutePath());
			js = JettyServer.createServer(J4AServer.class, 8080);
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
//						Log.d("Server", "check deploy...");
//						js.checkDeploy();
//						Log.d("Server", "load web context...");
//						js.loadWebContext();
//						Log.d("Server", "starting...");
						js.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
}
