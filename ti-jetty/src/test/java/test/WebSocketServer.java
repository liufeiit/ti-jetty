package test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月25日 下午6:29:31
 */
public class WebSocketServer {

	@WebSocket
	public static class EchoSocket {
		@OnWebSocketMessage
		public void onMessage(Session session, String message) {
			session.getRemote().sendStringByFuture(message);
		}
	}

	@SuppressWarnings("serial")
	public static class EchoServlet extends WebSocketServlet {
		@Override
		public void configure(WebSocketServletFactory factory) {
			factory.register(EchoSocket.class);
		}
	}

	public static void main(String[] args) {
		try {
			Server server = new Server(8080);
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
			context.addServlet(new ServletHolder(EchoServlet.class), "/echo");
			server.start();
			context.dumpStdErr();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}