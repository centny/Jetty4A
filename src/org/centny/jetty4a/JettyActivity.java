package org.centny.jetty4a;

import java.util.Timer;
import java.util.TimerTask;

import org.centny.jetty4a.J4AService.ServerStatus;
import org.centny.jetty4a.server.log.MemoryLog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * the main activity.
 * 
 * @author Centny
 * 
 */
public class JettyActivity extends Activity {

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
		J4AService.setHandler(statush);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.jetty, menu);
		return true;
	}

	/**
	 * click for start Jetty server or stop.
	 * 
	 * @param v
	 *            button view.
	 */
	public void onClk(View v) {
		if (J4AService.isSharedServerStartted()) {
			stopService(new Intent(this, J4AService.class));
		} else {
			startService(new Intent(this, J4AService.class));
		}
	}

	private static class StatusHandler extends Handler {
		public Activity aty;
		public TextView statusv;
		public Button startBtn;

		@Override
		public void handleMessage(Message msg) {
			if (msg.obj == null || !(msg.obj instanceof ServerStatus)) {
				return;
			}
			ServerStatus status = (ServerStatus) msg.obj;
			switch (status) {
			case Stopped:
				this.startBtn.setText("Start");
				this.startBtn.setEnabled(true);
				break;
			case Stopping:
				this.startBtn.setText("Start");
				this.startBtn.setEnabled(false);
				break;
			case Started:
				this.startBtn.setText("Stop");
				this.startBtn.setEnabled(true);
				break;
			case Starting:
				this.startBtn.setText("Starting");
				this.startBtn.setEnabled(false);
				break;
			}
			this.statusv.setText("(" + status.toString() + ","
					+ J4AService.localListener(aty) + ")");
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

	}

	@Override
	protected void onDestroy() {
		J4AService.setHandler(null);
		super.onDestroy();
	};

}
