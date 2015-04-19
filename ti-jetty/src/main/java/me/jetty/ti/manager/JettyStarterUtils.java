package me.jetty.ti.manager;

import me.jetty.ti.srv.JettyServer;
import me.jetty.ti.srv.ServerStartedCallback;
import me.jetty.ti.srv.ServerStopedCallback;
import me.jetty.ti.starter.JettyStarter;
import me.jetty.ti.starter.ServerStarter;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月19日 下午12:08:14
 */
public class JettyStarterUtils {
	
	private static final Logger log = Log.getLogger(JettyStarterUtils.class);
	
	public static void startJetty(ServerStartedCallback startedCaller, ServerStopedCallback stopedCaller) {
		try {
			Class<?> jettyStarterClass = new ServerStarter().loader();
			JettyStarter jettyStarter = (JettyStarter) jettyStarterClass.newInstance();
			if(startedCaller != null) {
				JettyServer.JettyServerInstance.addStartedCallback(startedCaller);
			}
			if(stopedCaller != null) {
				JettyServer.JettyServerInstance.addStopedCallback(stopedCaller);
			}
			jettyStarter.start();
		} catch (Exception e) {
			log.warn("Jetty Server Started Error.", e);
		}
	}

	public static void stopJetty() {
		if (JettyServer.JettyServerInstance != null) {
			try {
				JettyServer.JettyServerInstance.stop();
				JettyServer.JettyServerInstance = null;
			} catch (Exception e) {
				log.warn("Jetty Server Stoped Error.", e);
			}
		}
	}

	public static boolean isStarted() {
		if (JettyServer.JettyServerInstance != null) {
			return JettyServer.JettyServerInstance.isStarted();
		}
		return false;
	}
}