package me.srv.ti.starter;

import me.srv.ti.srv.JettyServer;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月16日 下午5:31:50
 */
public class JettyStarter {

	private final static Logger log = Log.getLogger(JettyStarter.class);

	private JettyServer server;

	public JettyStarter() {
		super();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					stop();
				} catch (Exception e) {
					log.warn("Jetty Stop Error.", e);
				}
			}
		}));
		try {
			start();
		} catch (Exception e) {
			log.warn("Jetty Start Error.", e);
		}
	}

	public void start() throws Exception {
		server = new JettyServer();
		server.start();
	}

	public void stop() throws Exception {
		if (server != null) {
			server.stop();
			server = null;
		}
	}
}