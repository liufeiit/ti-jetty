package me.jetty.ti.starter;

import me.jetty.ti.server.JettyServer;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午6:20:02
 */
public class JettyStarter {

	private final static Logger log = Log.getLogger(JettyStarter.class);

	public JettyStarter() {
		super();
		if(JettyServer.JettyServerInstance == null) {
			JettyServer.JettyServerInstance = new JettyServer();
		}
	}

	public void start() throws Exception {
		JettyServer.JettyServerInstance.start();
		Runtime.getRuntime().addShutdownHook(new Thread(new JettyShutdownHook()));
	}

	public void stop() throws Exception {
		if (JettyServer.JettyServerInstance != null) {
			JettyServer.JettyServerInstance.stop();
			JettyServer.JettyServerInstance = null;
		}
	}
	
	private class JettyShutdownHook implements Runnable {
		@Override
		public void run() {
			try {
				stop();
			} catch (Exception e) {
				log.warn("Jetty Stop Error.", e);
			}
		}
	}
}