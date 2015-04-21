package me.jetty.ti.server.handler;

import me.jetty.ti.server.Server;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月21日 下午10:09:49
 */
public interface StartedEventHandler extends EventHandler {
	void started(Server server) throws Exception;
}