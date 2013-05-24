package org.centny.wdev;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for show system properties and environment.
 * 
 * @author Centny.
 * 
 */
public class ShowServlet extends HttpServlet {

	/**
	 * serial id.
	 */
	private static final long serialVersionUID = 2342757834042191580L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head></head><body>");
		// Environment.
		sb.append("<div><div>Environment:</div><div><table>");
		for (String key : System.getenv().keySet()) {
			sb.append("<tr><td>" + key + "</td><td>" + System.getenv(key)
					+ "</td></tr>");
		}
		sb.append("</table></div>");
		sb.append("<br/><br/>");
		// Properties.
		sb.append("<div><div>Properties:</div><div><table>");
		for (Object key : System.getProperties().keySet()) {
			sb.append("<tr><td>" + key + "</td><td>"
					+ System.getProperty(key + "") + "</td></tr>");
		}
		sb.append("</table></div>");
		sb.append("</body><html>");
		resp.getOutputStream().write(sb.toString().getBytes());
	}
}
