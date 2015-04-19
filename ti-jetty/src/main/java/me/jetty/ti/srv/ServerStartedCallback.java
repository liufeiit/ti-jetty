package me.jetty.ti.srv;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午6:29:49
 */
public interface ServerStartedCallback {
	void call(Server server) throws Exception;
}