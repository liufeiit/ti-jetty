package me.jetty.ti.server;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import me.jetty.ti.etc.Connector;
import me.jetty.ti.etc.Resource;
import me.jetty.ti.etc.SslConnector;
import me.jetty.ti.server.handler.StartedEventHandler;
import me.jetty.ti.server.handler.StartingEventHandler;
import me.jetty.ti.server.handler.StopedEventHandler;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;

/**
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午6:19:52
 */
public class JettyServer extends AbstractServer {

	private Server server;

	public JettyServer() {
		super();
	}

	protected void start0() throws Exception {
		startingEventCaller();
		server = new Server();
		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
		server.addBean(mbContainer, true);
		new EnvEntry(server, "version", "1.0.0-Final", true);
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
		server.setThreadPool(newThreadPool());
		File[] apps = scanWebapps();
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		if (apps != null && apps.length > 0) {
			boolean singleWar = (apps.length == 1);
			for (File file : apps) {
				String contextPath = file.getName();
				String configAppPath = contextMapping.get(contextPath);
				if (StringUtils.isNotBlank(configAppPath)) {
					contextPath = configAppPath;
				}
				if (singleWar && profile.isSingleAppRoot()) {
					contextPath = ROOT_APP_PATH;
				}
				Application context = new Application(Application.SESSIONS | Application.SECURITY);
				context.setContextPath(contextPath);
				context.setWar(file.getAbsolutePath());
				context.setParentLoaderPriority(true);
				context.setExtractWAR(true);
				File tmp = new File(TEMP_DIRECTORY, guid());
				if (!tmp.exists()) {
					tmp.mkdirs();
				}
				context.setTempDirectory(tmp);
				setSessionHandler(server, context);
				setLogHandler(context);
				contexts.addHandler(context);
			}
		}
		List<Resource> resources = profile.getResources();
		if (resources != null && !resources.isEmpty()) {
			for (Resource resource : resources) {
				if (!resource.isEnable()) {
					continue;
				}
				String[] welcomeFiles = null;
				if (StringUtils.isNotBlank(resource.getWelcomeFiles())) {
					welcomeFiles = StringUtils.split(resource.getWelcomeFiles(), DOT);
				}
				ContextHandler context = new ContextHandler();
				context.setContextPath(resource.getContext());
				ResourceHandler handler = new ResourceHandler();
				handler.setDirectoriesListed(resource.isDirectoriesListed());
				handler.setWelcomeFiles(welcomeFiles);
				handler.setResourceBase(resource.getBaseResource());
				handler.setStylesheet(resource.getStylesheet());
				handler.setEtags(resource.isEtags());
				handler.setAliases(resource.isAliases());
				handler.setCacheControl(resource.getCacheControl());
				context.setHandler(handler);
				contexts.addHandler(context);
			}
		}
		contexts.addHandler(new DefaultHandler());
		server.setHandler(contexts);
		log.info("Starting Jetty Server ...");
		server.setDumpAfterStart(profile.isDumpStdErr());
		server.setStopAtShutdown(true);
		server.setSendServerVersion(true);
		server.setSendDateHeader(true);
		server.start();
		log.info("Jetty Server Started Success.");
		startedEventCaller();
		server.join();
	}

	private void startingEventCaller() {
		if (startingEventHandlers.isEmpty()) {
			return;
		}
		for (StartingEventHandler handler : startingEventHandlers) {
			try {
				handler.starting(this);
			} catch (Exception e) {
				log.warn("StartingEventHandler Invoking Error.", e);
			}
		}
	}

	private void startedEventCaller() {
		if (startedEventHandlers.isEmpty()) {
			return;
		}
		for (StartedEventHandler handler : startedEventHandlers) {
			try {
				handler.started(this);
			} catch (Exception e) {
				log.warn("StartedEventHandler Invoking Error.", e);
			}
		}
	}

	private void stopedEventCall() {
		if (stopedEventHandlers.isEmpty()) {
			return;
		}
		for (StopedEventHandler handler : stopedEventHandlers) {
			try {
				handler.stoped();
			} catch (Exception e) {
				log.warn("StopedEventHandler Invoking Error.", e);
			}
		}
	}

	protected void stop0() throws Exception {
		log.info("Stoping Jetty Server ...");
		try {
			server.stop();
			server = null;
			stopedEventCall();
			stopedEventHandlers.clear();
			startedEventHandlers.clear();
		} catch (Exception e) {
			log.warn("Jetty Server Stop Error.", e);
		}
		log.info("Stop Jetty Server Success.");
	}

	public boolean isStarted() {
		return server != null && server.isStarted();
	}
}