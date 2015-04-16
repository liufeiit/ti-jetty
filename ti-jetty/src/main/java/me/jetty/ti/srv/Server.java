package me.jetty.ti.srv;

import java.io.File;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月30日 下午5:23:51
 */
public interface Server {
	File 		Temp_Directory 			= 		new File("../.tmp/.vfs/");
	File 		Log_Directory 			= 		new File("../logs/");
	File 		Apps_Directory 			= 		new File("../apps/");
	String 		GMT 					= 		"GMT";
	String 		REQUEST_LOG_FORMAT 		= 		"yyyy_MM_dd";
	String 		JETTY_REQUEST_LOG 		= 		"jetty-yyyy_mm_dd.request.log";
	
	String		ROOT_APP_PATH 			= 		"/";
	String 		WAR_LOWERCASE_SUFFIX 	= 		".war";
	
	void start() throws Exception;
	
	void stop() throws Exception;
	
	boolean isStarted();
}