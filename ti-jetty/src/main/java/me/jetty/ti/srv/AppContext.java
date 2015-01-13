package me.jetty.ti.srv;

import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月13日 上午10:48:58
 */
public class AppContext extends WebAppContext {
	
	public AppContext(int options) {
		super();
		this._options = options;
	}

	public AppContext(HandlerContainer parent, String webApp, String contextPath, int options) {
		super(parent, webApp, contextPath);
		this._options = options;
	}

	public AppContext(SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler, int options) {
		super(sessionHandler, securityHandler, servletHandler, errorHandler);
		this._options = options;
	}

	public AppContext(String webApp, String contextPath, int options) {
		super(webApp, contextPath);
		this._options = options;
	}
}