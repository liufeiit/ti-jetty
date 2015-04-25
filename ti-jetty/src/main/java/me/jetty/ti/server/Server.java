package me.jetty.ti.server;

import java.io.File;

import me.jetty.ti.server.handler.StartedEventHandler;
import me.jetty.ti.server.handler.StartingEventHandler;
import me.jetty.ti.server.handler.StopedEventHandler;

/**
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午6:20:34
 */
public interface Server {
	File TEMP_DIRECTORY = new File("../.tmp/.vfs/");
	File LOG_DIRECTORY = new File("../logs/");
	File APPS_DIRECTORY = new File("../apps/");
	String GMT = "GMT";
	String REQUEST_LOG_FORMAT = "yyyy_MM_dd";
	String JETTY_REQUEST_LOG = "ti-jetty-yyyy_mm_dd.request.log";

	String ROOT_APP_PATH = "/";
	String WAR_LOWERCASE_SUFFIX = ".war";

	String DOT = ",";

	void start() throws Exception;

	void stop() throws Exception;

	boolean isStarted();

	Server addStartingEventHandler(StartingEventHandler startingEventHandler);

	Server addStartedEventHandler(StartedEventHandler startedEventHandler);

	Server addStopedEventHandler(StopedEventHandler stopedEventHandler);
}