package me.jetty.ti.srv;

import java.io.File;
import java.io.FileFilter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import me.jetty.ti.etc.Connector;
import me.jetty.ti.etc.QueuedPool;
import me.jetty.ti.etc.SslConnector;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
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
		server.addBean(mbContainer, true);
		new org.eclipse.jetty.plus.jndi.EnvEntry(server, "version", "1.0.0-Final", false);
		List<SelectChannelConnector> connectors = new ArrayList<SelectChannelConnector>();
		List<Connector> profileConnectors = profile.getConnectors();
		if (profileConnectors != null && !profileConnectors.isEmpty()) {
			for (Connector connect : profileConnectors) {
				if (!connect.isEnable()) {
					continue;
				}
				SelectChannelConnector connector = newConnector(connect);
				connectors.add(connector);
			}
		}
		List<SslConnector> profileSslConnectors = profile.getSslConnectors();
		if (profileSslConnectors != null && !profileSslConnectors.isEmpty()) {
			for (SslConnector connect : profileSslConnectors) {
				if (!connect.isEnable()) {
					continue;
				}
				SslSelectChannelConnector connector = newSslConnector(connect);
				connectors.add(connector);
			}
		}
		server.setConnectors(connectors.toArray(new SelectChannelConnector[connectors.size()]));

		QueuedThreadPool threadPool = new QueuedThreadPool();
		QueuedPool queuedPool = profile.getQueuedPool();
		threadPool.setMaxThreads(queuedPool.getMaxThreads());
		threadPool.setMinThreads(queuedPool.getMinThreads());
		threadPool.setMaxQueued(queuedPool.getMaxQueued());
		threadPool.setMaxStopTimeMs(queuedPool.getMaxStopTimeMs());
		threadPool.setMaxIdleTimeMs(queuedPool.getMaxIdleTimeMs());
		threadPool.setDaemon(true);
		threadPool.setDetailedDump(true);
		threadPool.setName(queuedPool.getName());
		threadPool.setThreadsPriority(Thread.NORM_PRIORITY);
		server.setThreadPool(threadPool);

		File[] apps = Apps_Directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isFile()) {
					return file.getName().toLowerCase().endsWith(".war");
				}
				return true;
			}
		});

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		for (File file : apps) {
			String contextPath = file.getName();
			if (contextMapping.get(contextPath) != null) {
				contextPath = contextMapping.get(contextPath);
			}
			Application context = new Application(Application.SESSIONS | Application.SECURITY);
			context.setContextPath(contextPath);
			context.setWar(file.getAbsolutePath());
			context.setParentLoaderPriority(true);
			context.setExtractWAR(true);
			File tmp = new File(Temp_Directory, guid());
			if (!tmp.exists()) {
				tmp.mkdirs();
			}
			context.setTempDirectory(tmp);
			setSessionHandler(server, context);
			setLogHandler(context);
			contexts.addHandler(context);
		}
		server.setHandler(contexts);

		log.info("Starting Jetty Server ...");
		server.setStopAtShutdown(true);
		server.setSendServerVersion(true);
		server.start();
		started.set(true);
		server.dumpStdErr();
		log.info("Jetty Server Started Success.");
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