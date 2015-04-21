package me.jetty.ti.server.handler;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月21日 下午10:10:12
 */
public interface StopedEventHandler extends EventHandler {
	void stoped() throws Exception;
}