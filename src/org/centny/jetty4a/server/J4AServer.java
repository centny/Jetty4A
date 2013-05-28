package org.centny.jetty4a.server;

import java.io.File;

import org.centny.jetty4a.server.api.JettyServer;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import android.os.Handler;
import android.os.Message;
import dalvik.system.DexClassLoader;

public class J4AServer extends JettyServer {

	public static enum ServerStatus {
		Stopped, Stopping, Starting, Started
	}

	private static int listenPort__ = 0;
	private static J4AServer sharedServer__;
	private static Logger slog = Log.getLogger("SharedServer");

	public static void createSharedServer() {
		try {
			try {
				String sport = System.getProperty("J4A_LISTEN_PORT");
				listenPort__ = Integer.parseInt(sport);
			} catch (Exception e) {
				listenPort__ = 8080;
				slog.debug("listen port configure not found,using default:"
						+ listenPort__);
			}
			sharedServer__ = (J4AServer) JettyServer
					.createServer(J4AServer.class);
			QueuedThreadPool threadPool = new QueuedThreadPool();
			threadPool.setMaxThreads(10);
			SelectChannelConnector connector = new SelectChannelConnector();
			connector.setThreadPool(threadPool);
			connector.setPort(listenPort__);
			sharedServer__.addConnector(connector);
			slog.info("initial shared server in port:" + listenPort__);
		} catch (Exception e) {
			slog.warn("initial shared server error", e);
			sharedServer__ = null;
		}
	}

	public static void send(Handler h, ServerStatus status) {
		Message msg = new Message();
		msg.obj = status;
		h.sendMessage(msg);
	}

	public static boolean isSharedServerStartted() {
		if (sharedServer__ == null) {
			return false;
		} else {
			return sharedServer__.isStarted();
		}
	}

	public static void startSharedServer(final Handler h) {
		if (sharedServer__ == null) {
			slog.warn("shared server is not initial,call createSharedServer first");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					send(h, ServerStatus.Starting);
					sharedServer__.start();
					send(h, ServerStatus.Started);
				} catch (Exception e) {
					send(h, ServerStatus.Stopped);
					slog.warn("start server error", e);
				}
			}
		}).start();
	}

	public static void stopSharedServer(final Handler h) {
		if (sharedServer__ == null) {
			slog.warn("shared server is not initial,call createSharedServer first");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					send(h, ServerStatus.Stopping);
					sharedServer__.stop();
					sharedServer__.join();
					send(h, ServerStatus.Stopped);
				} catch (Exception e) {
					send(h, ServerStatus.Stopped);
					slog.warn("stop server error", e);
				}
			}
		}).start();
	}

	public static int listenPort() {
		return listenPort__;
	}

	public J4AServer(File wsdir, File deploy) {
		super(wsdir, deploy);
	}

	public J4AServer(File wsdir, File deploy, int port) {
		super(wsdir, deploy, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.centny.jetty4a.server.api.JettyServer#buildClassLoader(java.io.File,
	 * java.lang.ClassLoader)
	 */
	@Override
	protected ClassLoader buildClassLoader(File root, ClassLoader tcl) {
		ClassLoader pcl = super.buildClassLoader(root, tcl);
		File tf = new File(root, "classes.jar");
		if (tf.exists()) {
			DexClassLoader dcl = null;
			dcl = new DexClassLoader(tf.getAbsolutePath(),
					root.getAbsolutePath(), null, pcl);
			return dcl;
		} else {
			return pcl;
		}
	}

}
