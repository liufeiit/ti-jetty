package me.srv.ti.srv;

import java.io.File;
import java.lang.management.ManagementFactory;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月12日 上午10:12:01
 */
public class JettyServer extends AbstractServer {

	private Server server;

	public JettyServer() {
		super();
	}

	protected void start0() throws Exception {
		server = new Server();
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		server.addBean(mbContainer);
		new org.eclipse.jetty.plus.jndi.EnvEntry(server, "version", "1.0.0-Final", false);
		if (profile.isSslEnable()) {
			SslSelectChannelConnector connector = new SslSelectChannelConnector();
			connector.setPort(profile.getPort());
			connector.setAcceptQueueSize(profile.getAcceptQueueSize());
			connector.setAcceptors(Runtime.getRuntime().availableProcessors() * 2);
			connector.setMaxIdleTime(profile.getMaxIdleTime());
			SslContextFactory contextFactory = connector.getSslContextFactory();
			contextFactory.setKeyStorePath(profile.getKeyStorePath());
			contextFactory.setKeyStorePassword(profile.getKeyStorePassword());
			contextFactory.setKeyManagerPassword(profile.getKeyManagerPassword());
			if (StringUtils.isNotBlank(profile.getTrustStorePath())) {
				contextFactory.setTrustStore(profile.getTrustStorePath());
			}
			if (StringUtils.isNotBlank(profile.getTrustStorePassword())) {
				contextFactory.setTrustStorePassword(profile.getTrustStorePassword());
			}
			contextFactory.setNeedClientAuth(profile.isClientAuth());
			if (StringUtils.isNotBlank(profile.getCertAlias())) {
				contextFactory.setCertAlias(profile.getCertAlias());
			}
			server.addConnector(connector);
		} else {
			SelectChannelConnector connector = new SelectChannelConnector();
			connector.setPort(profile.getPort());
			connector.setAcceptQueueSize(profile.getAcceptQueueSize());
			connector.setAcceptors(Runtime.getRuntime().availableProcessors() * 2);
			connector.setMaxIdleTime(profile.getMaxIdleTime());
			server.addConnector(connector);
		}
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(profile.getQueuedMaxThreads());
		threadPool.setMinThreads(profile.getQueuedMinThreads());
		threadPool.setMaxQueued(profile.getQueuedMaxQueued());
		threadPool.setMaxStopTimeMs(profile.getQueuedMaxStopTimeMs());
		threadPool.setMaxIdleTimeMs(profile.getQueuedMaxIdleTimeMs());
		threadPool.setDaemon(true);
		threadPool.setDetailedDump(true);
		threadPool.setName(profile.getQueuedName());
		threadPool.setThreadsPriority(Thread.NORM_PRIORITY);
		server.setThreadPool(threadPool);
		WebApp context = new WebApp(WebApp.SESSIONS | WebApp.SECURITY);
		context.setContextPath(profile.getContextPath());
		context.setWar(new File(profile.getWar()).getAbsolutePath());
		context.setParentLoaderPriority(true);
		context.setExtractWAR(true);
		File tmp = new File(Temp_Directory, guid());
		if (!tmp.exists()) {
			tmp.mkdirs();
		}
		context.setTempDirectory(tmp);
		setSessionHandler(server, context);
		setLogHandler(context);
		server.setHandler(context);
		log.info("Starting Jetty Server ...\n" + " Listen Port : " + profile.getPort());
		server.setStopAtShutdown(true);
		server.setSendServerVersion(true);
		server.start();
		started.set(true);
		server.dumpStdErr();
		log.info("Jetty Server Started Success.\n" + " Listen Port : " + profile.getPort());
		server.join();
	}

	protected void stop0() throws Exception {
		log.info("Stoping Jetty Server ...");
		try {
			server.stop();
			server = null;
		} catch (Exception e) {
			log.warn("Jetty Server Stop Error.", e);
		}
		log.info("Stop Jetty Server Success.");
	}

	public boolean isStarted() {
		return super.isStarted() && server != null && server.isStarted();
	}
}