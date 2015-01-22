package test;

import java.io.File;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2014年12月13日 下午7:32:48
 */
public class JettyMain {

	public static void main22(String[] args) throws Exception {
		Server server = new Server();

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
		
        NCSARequestLog requestLog = new NCSARequestLog(System.getProperty("user.dir") + "/itemp/NCSA.log");
        requestLog.setExtended(false);
		RequestLogHandler log = new RequestLogHandler();
		log.setRequestLog(requestLog);
		context.setHandler(log);
		
		server.setHandler(context);
		server.start();
		server.join();
		System.err.println("Jetty Server started.");
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html"});
		resourceHandler.setResourceBase("/Users/yp");
		resourceHandler.setStylesheet("");
		resourceHandler.setEtags(true);
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler });
		server.setHandler(handlers);
		server.start();
		server.join();
		System.err.println("Jetty File Server started.");
	}
}
