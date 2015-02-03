package test;

import java.util.Properties;

import javax.naming.NamingException;

import me.jetty.ti.srv.WebApp;

import org.eclipse.jetty.jndi.factories.MailSessionReference;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月22日 下午5:12:24
 */
public class JettySslServer {

	/**
	 * keytool -keystore jettyKS -alias jetty -genkey -keyalg RSA
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		SslSelectChannelConnector connector = new SslSelectChannelConnector();
		connector.setPort(8443);
		SslContextFactory cf = connector.getSslContextFactory();
		cf.setKeyStorePath("/Users/yp/ssl_jetty/jettyKS");
		cf.setKeyStorePassword("lF0130lf");
		cf.setKeyManagerPassword("lF0130lf");
		server.addConnector(connector);
		server.start();
		server.join();
	}

	void setMailJndiResource(WebApp context) throws NamingException {
		MailSessionReference mailref = new MailSessionReference();
		mailref.setUser("CHANGE-ME");
		mailref.setPassword("CHANGE-ME");
		Properties props = new Properties();
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.host", "CHANGE-ME");
		props.put("mail.from", "CHANGE-ME");
		props.put("mail.debug", "false");
		mailref.setProperties(props);
		new org.eclipse.jetty.plus.jndi.Resource(context, "mail/Session", mailref);
	}
}