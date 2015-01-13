package test;

import java.io.File;

import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ovea.jetty.session.redis.RedisSessionIdManager;
import com.ovea.jetty.session.redis.RedisSessionManager;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月9日 下午5:34:01
 */
public class ServerCluster {

	public static void main(String[] args) throws Exception {
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
		threadPool.setMaxQueued(100);
		threadPool.setMinThreads(5);
		threadPool.setMaxIdleTimeMs(3000);
		threadPool.setDaemon(true);
		threadPool.setDetailedDump(true);
		threadPool.setName("jetty.tp");
		threadPool.setThreadsPriority(Thread.NORM_PRIORITY);
		server.setThreadPool(threadPool);
		
		WebAppContext context = new WebAppContext();
		context.setContextPath("/");
		context.setWar("/Users/yp/workspace/test-web.war");
		context.setParentLoaderPriority(true);

		File dir = new File(System.getProperty("user.dir") + "/itemp/");
		context.setTempDirectory(dir);
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxActive(1000);
		poolConfig.setMinIdle(1000);
		poolConfig.setMaxIdle(1000);
		poolConfig.setMaxWait(3000);
		
		JedisPool pool = new JedisPool(poolConfig, "127.0.0.1", 6379, 3000);
		RedisSessionManager sessionHandler = new RedisSessionManager(pool);
		sessionHandler.setSessionIdManager(new RedisSessionIdManager(server, pool));
		
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
}