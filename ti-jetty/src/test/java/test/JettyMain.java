package test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLDecoder;
import java.security.ProtectionDomain;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 
 * https://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 * 
 * http://stackoverflow.com/questions/14958251/map-jetty-resourcehandler-to-a-url
 * 
 * https://github.com/eclipse/jetty.project/blob/master/examples/embedded/src/main/java/org/eclipse/jetty/embedded/
 * SecuredHelloHandler.java
 * 
 * http://www.programcreek.com/java-api-examples/index.php?api=org.eclipse.jetty.server.handler.ResourceHandler
 * 
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2014年12月13日 下午7:32:48
 */
public class JettyMain {

    public static void main(String[] args) throws Throwable {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new HttpServlet() {
            private static final long serialVersionUID = 1L;
            @Override
            public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
                res.getWriter().write("name : " + req.getParameter("name"));
            }
            public String decode(String parameter) {
                try {
                    return URLDecoder.decode(parameter, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        }), "/test.html");
        server.start();
        System.err.println("Jetty Server started.");
        server.join();
    }

    public static void main22(String[] args) throws Exception {
        Server server = new Server();
        server.addBean(new MBeanContainer(ManagementFactory.getPlatformMBeanServer()));
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setHost("127.0.0.1");
        connector.setPort(8080);
        connector.setAcceptQueueSize(50);
        connector.setAcceptors(2);
        connector.setMaxIdleTime(3000);
        server.addConnector(connector);

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(100);
        threadPool.setMinThreads(5);
        threadPool.setMaxQueued(100);
        threadPool.setMaxStopTimeMs(11);
        threadPool.setMaxIdleTimeMs(3000);
        threadPool.setDaemon(true);
        threadPool.setDetailedDump(true);
        threadPool.setName("jetty.tp");
        threadPool.setThreadsPriority(Thread.NORM_PRIORITY);
        server.setThreadPool(threadPool);

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setWar("/Users/yp/workspace/af-cfc-yeepay/af-cfc-yeepay/web/target/lc");
        context.setParentLoaderPriority(true);

        File dir = new File(System.getProperty("user.dir") + "/itemp/");
        context.setTempDirectory(dir);

        SessionManager sessionHandler = new HashSessionManager();
        context.setSessionHandler(new SessionHandler(sessionHandler));


        NCSARequestLog requestLog = new NCSARequestLog(System.getProperty("user.dir") + "/logs/yyyy_mm_dd.request.log");
        requestLog.setFilename(System.getProperty("user.dir") + "/logs/yyyy_mm_dd.request.log");
        requestLog.setFilenameDateFormat("yyyy_MM_dd");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(true);
        requestLog.setLogCookies(false);
        requestLog.setLogTimeZone("GMT");
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);

        context.setHandler(requestLogHandler);

        server.setHandler(context);

        // ConnectorStatistics.addToAllConnectors(server);

        server.start();
        server.join();
        System.err.println("Jetty Server started.");
    }

    public static void main122(String[] args) throws Exception {
        Server server = new Server(8080);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[] {"index.html"});
        resourceHandler.setResourceBase("/Users/yp");
        resourceHandler.setStylesheet("");
        resourceHandler.setEtags(true);

        resourceHandler.setAliases(true);
        resourceHandler.setCacheControl("");
        // resourceHandler.setMimeTypes(MimeTypes.);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resourceHandler});
        server.setHandler(handlers);
        server.start();
        server.join();
        System.err.println("Jetty File Server started.");
        WebAppProvider appProvider;
    }

    public static void main1233(String[] args) {
        ProtectionDomain protectionDomain = JettyMain.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        System.out.println(location);
    }

    /**
     * java -jar hudson.war
     */
    public static void mainqw(String[] args) {
        Server server = new Server();
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(8080);
        server.setConnectors(new Connector[] {connector});

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");

        ProtectionDomain protectionDomain = JettyMain.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setWar(location.toExternalForm());
        System.out.println(location);
        server.setHandler(context);
        try {
            server.start();
            System.in.read();
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
