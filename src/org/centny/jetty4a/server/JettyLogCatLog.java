package org.centny.jetty4a.server;

import org.eclipse.jetty.util.log.AbstractLogger;
import org.eclipse.jetty.util.log.Logger;

import android.util.Log;

/**
 * the JettyServerLog for android console log.
 * 
 * @author Centny.
 * 
 */
public class JettyLogCatLog extends AbstractLogger {
	private String tag;
	private String name;
	private boolean debugEnabled;

	public JettyLogCatLog() {
		this(null);
	}

	public JettyLogCatLog(String name) {
		this.name = name;
		this.tag = this.name == null ? "" : this.name;
	}

	private String format(String fmt, Object... args) {
		fmt = fmt.replaceAll("\\{\\}", "%s");
		return String.format(fmt, args);
	}

	@Override
	public void debug(Throwable arg0) {
		Log.d(this.tag, "", arg0);
	}

	@Override
	public void debug(String arg0, Object... arg1) {
		Log.d(this.tag, format(arg0, arg1));
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		Log.d(this.tag, arg0, arg1);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void ignore(Throwable arg0) {

	}

	@Override
	public void info(Throwable arg0) {
		Log.i(this.tag, "", arg0);
	}

	@Override
	public void info(String arg0, Object... arg1) {
		Log.i(this.tag, format(arg0, arg1));
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		Log.i(this.tag, arg0, arg1);
	}

	@Override
	public boolean isDebugEnabled() {
		return this.debugEnabled;
	}

	@Override
	public void setDebugEnabled(boolean arg0) {
		this.debugEnabled = arg0;
	}

	@Override
	public void warn(Throwable arg0) {
		Log.w(this.tag, "", arg0);
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		Log.w(this.tag, format(arg0, arg1));
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		Log.w(this.tag, arg0, arg1);
	}

	@Override
	protected Logger newLogger(String arg0) {
		return new JettyLogCatLog(arg0);
	}

}
