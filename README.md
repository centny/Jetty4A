Jetty Web Server For Android
=======
![icon](https://raw.github.com/Centny/Jetty4A/master/j4a.png "Jy4a")


##Features
- contain most of Jetty Web Server features(JSP,Servlet)
- custom Realm Basic Authorization for each WebApp
- network change listener for dynamic dns.

##Install Jetty4A

- Install manual <a href="https://docs.google.com/file/d/0B0QqxjdrDvmZSTN6RGg4a0RBc00/edit?usp=sharing">Jetty4A.apk</a>
- Install by Play store
- Export APK manual for install:
	- clone the code:

		```
		cd <target directory>
		git clone https://github.com/Centny/Centny
		git clone https://github.com/Centny/Jetty4A

		```
	- open the Jetty4A project by ADT
	- export the apk file.





##Install war2j4a tools
- make sure your **DX_HOME** environment value had configured.
- run blew script in console:

	```
	cd */Jetty4A/script
	chmod +x war2j4a
	sudo cp war2j4a /usr/local/bin
	```

##Develop Normal Web App
if the web app is normal .war app,you can convert it to .j4a file by **war2j4a**.

##Develop Custom Web App
if yout want to develop web app by using Jetty API,like adding handler by code/Using Basic Authorization:

- Prepare **jetty-all-server-8.\*.jar**,**jsch-\*.jar**,**servlet-api-3.\*.jar**,**commons-io-\*.jar**,you can download by yourself or you can copy from */Jetty4A/libs
- open eclipse **User Libraries** panel and add library by name **Jetty4A**,then add all jars in step one to Jetty4A library.
- New dynamic web project in eclipse
- Add **Jetty4A** library to project build path.
- if yout want to add ant build for yout project,donwload sample <a href="https://raw.github.com/Centny/Jetty4A/master/J4AWebDev/build.xml">build.xml</a> to project and modify it.
- see detail:

**Note:**see example project **Jetty4A/J4AWebDev**

##Start Web Server by JUnit
- add **jetty4a.properties** file in **src/**:

	```
	#configure jetty4a.
	J4A_LOG_CLASSES=
	J4A_LOG_LEVEL=DEBUG
	J4A_LISTEN_PORT=8080
	org.eclipse.jetty.util.log.class=org.centny.jetty4a.server.log.Jetty4ALog
	org.eclipse.LEVEL=INFO
	j4a.dnsdynamic.class=org.centny.jetty4a.server.api.DnsDynamicOrgImpl
	j4a.connector.builder.class=org.centny.jetty4a.server.api.DefaultConnectorBuilder
	org.centny.LEVEL=DEBUG
	```
- add JUnit test class:
	
	```
	public class WebDevTest {
	private static int listenPort__ = 8080;
	@Test
	public void testMgw() throws Exception {
		JettyDevServer.initWebDev();
		try {
			String sport = System.getProperty("J4A_LISTEN_PORT");
			listenPort__ = Integer.parseInt(sport);
		} catch (Exception e) {
			listenPort__ = 8080;
		}
		JettyServer jds = JettyServer.createServer(JettyDevServer.class,
				listenPort__);
		jds.start();
		jds.join();
	}
	}
	```
	

##Using Custom Base Authroization
- create a normal web app
- add **Jetty4A** library to project(see <a href="#develop-custom-web-app">Develop Custom Web App</a>)
- add base authorization configure to **web.xml**:

	```
  <security-constraint>
    <web-resource-collection>
      <web-resource-name>WDevRealm</web-resource-name>
      <url-pattern>/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
      <role-name>log</role-name>
    </auth-constraint>
  </security-constraint>
  <login-config>
    <auth-method>BASIC</auth-method>
    <realm-name>WDevRealm</realm-name>
  </login-config>
	```
- create **Realm.properties** file in **WEB-INF**,add content like below:

	```
	#the admin for J4AWebLog
	#default password:j4a
	admin: CRYPT:adj7rHs7GA9YQ,log,wdir,dev
	#default password:log
	log: CRYPT:lo9V8yD.yQMps,log
	#default password:wdir
	wdir: CRYPT:wdHLqn0AYYU4w,wdir
	```
	**Note:**about how to create Hash Realm file,see Jetty document.
- add a class **org.centny.wdev.WebDev** extends **ServerListener**
<br/>
add code to **init** method to initial **HashLoginService**:

	```
	File wrealm = new File(this.croot, "Realm.properties");
	if (wrealm.exists()) {
		this.log.info("initial WDev Realm...");
		hls = new HashLoginService("WDevRealm");
		hls.setConfig(wrealm.getAbsolutePath());
		hls.setRefreshInterval(60);
		try {
			hls.start();
		} catch (Exception e) {
			this.log.warn("initial WDev Realm error", e);
			this.hls = null;
		}
	} else {
		this.log.info("WDev Realm file not found...");
		this.hls = null;
	}
	```
add code to **initWebApp** method to make affectived:

	```
	if (this.hls != null) {
		wapp.getSecurityHandler().setLoginService(hls);
	}
	```
- add **web.properties** file to setting ServerListener or WebContext configure:
	
	```
	#------------------------------------------------------------------------------
	#  Properties external.
	#  it can use all android default env and system properties field.
	#  like 
	#   EXTERNAL_STORAGE the external storage path.
	#  external env name:
	#   J4A_EDIR   the jetty4a external path.
	#   J4A_DDIR   the jetty4a deploy path.
	#   J4A_LDIR   the jetty4a log path.
	#   J4A_CDIR   the jetty4a configure path.
	#------------------------------------------------------------------------------
	#the Web configure.
	WebName=J4AWebDev
	ServerListener=org.centny.wdev.WebDev
	#configure for the WebAppContext,it will be used when web.xml exist.
	WebResourceBase=$(J4A_LDIR)
	WebContextPath=/log
	```

##Configure HttpServlet by code
- add normal HttpServlet as standard Web App:
	
	```
public class ShowServlet extends HttpServlet {
	private static final long serialVersionUID = 2342757834042191580L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
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
	```
- add a class **org.centny.wdev.WebDev** extends **ServerListener**
<br/>
add configure code:

	```
	@Override
	public Handler create(ClassLoader loader, Properties webp) {
		ContextHandlerCollection chc = new ContextHandlerCollection();
		// add dev path for develop command.
		WebAppContext wapp;
		wapp = new WebAppContext();
		wapp.setParentLoaderPriority(true);
		String wpath = webp.getProperty(J4A_WDIR);
		wapp.setResourceBase(wpath);
		wapp.setContextPath("/dev");
		if (this.hls != null) {
			wapp.getSecurityHandler().setLoginService(hls);
		}
		File web = new File(this.croot, "DevWeb.xml");
		if (web.exists()) {
			wapp.setDescriptor(web.getAbsolutePath());
		}
		wapp.addServlet(new ServletHolder(new ShowServlet()), "/show.cmd");
		chc.addHandler(wapp);
		return chc;
	}
	```
**Note:**Jetty have other configure way,see Jetty document.

##Add Custom environment value to System.getProperties

- add **env.peroerties** file to **<sdcard>/jetty4a/config**
- add key value by **key=value**


##Configure dynamic DNS
- add **DnsDynamic.properties** file to **<sdcard>/jetty4a/config
- add blew code:

	```
	#configure DnsDynamic
	USER=** #the username
	PASS=** #the password
	HOST=** #the dnsdynamic server host.
	####################
	#the period time for timer
	#if not set,timer will now started.
	PERIOD=10000
	```
**Note:**the Jy4a default implemetation is <http://dnsdynamic.org>

##Custom dynamic DNS implementation
see example:**Jetty4A/J4AExtSample** project

- add class **org.centny.j4aext.CustomDnsDynamicImpl** implements DnsDynamic.Listener:

	```
	public class CustomDnsDynamicImpl implements DnsDynamic.Listener {
		Logger log = Log.getLogger(CustomDnsDynamicImpl.class);
		@Override
		public void update(DnsDynamic dns) {
			this.log.debug("upate.........");
		}
	}
	```
- update **jetty4a.properties** to:
	
	```
	j4a.dnsdynamic.class=org.centny.j4aext.CustomDnsDynamicImpl
	```

##Custom Jetty ConnectorBuilder
see example:**Jetty4A/J4AExtSample** project

- add class extends **org.centny.j4aext.CustomConnectorBuilder** implements ConnectorBuilder

	```
	public class CustomConnectorBuilder implements ConnectorBuilder {
		Logger log = Log.getLogger(CustomConnectorBuilder.class);
		@Override
		public Connector create(int port) {
			QueuedThreadPool threadPool = new QueuedThreadPool();
			threadPool.setMaxThreads(10);
			SelectChannelConnector connector = new SelectChannelConnector();
			connector.setThreadPool(threadPool);
			connector.setPort(9000);
			this.log.debug("building.........");
			return connector;
		}
	}
	```

- update **jetty4a.properties** to:

	```
	j4a.connector.builder.class=org.centny.j4aext.CustomConnectorBuilder
	```