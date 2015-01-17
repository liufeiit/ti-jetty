package me.jetty.ti.etc;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import me.jetty.ti.utils.StreamUtils;

import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author john.liu E-mail:fei.liu@yeepay.com
 * @version 1.0.0
 * @since 2015年1月12日 下午2:25:49
 */
public class JettyProfile {

	final static Logger log = Log.getLogger(JettyProfile.class);

	public static int 		Server_Port;
	
	public static String 	App_ContextPath;
	public static String 	App_War;
	
	public static boolean 	App_Use_Sessions;
	
	/**每个请求被accept前允许等待的连接数**/
	public static int 		Connector_AcceptQueueSize;
	/**连接最大空闲时间，默认是200000，-1表示一直连接**/
	public static int 		Connector_MaxIdleTime;

	public static String 	Thread_Name;
	public static int 		Thread_MinThreads;
	public static int 		Thread_MaxThreads;
	public static int 		Thread_MaxQueued;
	public static int 		Thread_MaxIdleTimeMs;
	public static int 		Thread_MaxStopTimeMs;
	
	public static String 	Redis_Host;
	public static int 		Redis_Port;
	public static int 		Redis_MaxActive;
	public static int 		Redis_MinIdle;
	public static int 		Redis_MaxIdle;
	public static long 		Redis_MaxWait;
	public static int 		Redis_Timeout;
	
	static {
		try {
			File etc = new File("../etc/profile.xml");
			String xml = StreamUtils.copyToString(new FileInputStream(etc), Charset.forName("UTF-8"));
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			
			JettyProfile.Server_Port = NumberUtils.toInt(root.elementTextTrim("port"), 8080);
			
			JettyProfile.App_ContextPath = root.elementTextTrim("context-path");
			JettyProfile.App_War = root.elementTextTrim("war");
			
			JettyProfile.App_Use_Sessions = Boolean.parseBoolean(root.elementTextTrim("use-session"));
			
			Element connector = root.element("connector");
			JettyProfile.Connector_AcceptQueueSize = NumberUtils.toInt(connector.elementTextTrim("accept-queue-size"), 100);
			JettyProfile.Connector_MaxIdleTime = NumberUtils.toInt(connector.elementTextTrim("max-idle-time"), 5000);

			Element threadPool = root.element("thread-pool");
			JettyProfile.Thread_Name = threadPool.elementTextTrim("name");
			JettyProfile.Thread_MinThreads = NumberUtils.toInt(threadPool.elementTextTrim("min-threads"), 5);
			JettyProfile.Thread_MaxThreads = NumberUtils.toInt(threadPool.elementTextTrim("max-threads"), 200);
			JettyProfile.Thread_MaxQueued = NumberUtils.toInt(threadPool.elementTextTrim("max-queued"), 50);
			JettyProfile.Thread_MaxIdleTimeMs = NumberUtils.toInt(threadPool.elementTextTrim("max-idle-time-ms"), 5000);
			JettyProfile.Thread_MaxStopTimeMs = NumberUtils.toInt(threadPool.elementTextTrim("max-stop-time-ms"), 5000);

			Element cluster = root.element("redis");
			JettyProfile.Redis_Host = cluster.elementTextTrim("redis-host");
			JettyProfile.Redis_Port = NumberUtils.toInt(cluster.elementTextTrim("redis-port"), 6379);
			JettyProfile.Redis_MaxActive = NumberUtils.toInt(cluster.elementTextTrim("redis-max-active"), 500);
			JettyProfile.Redis_MinIdle = NumberUtils.toInt(cluster.elementTextTrim("redis-min-idle"), 5);
			JettyProfile.Redis_MaxIdle = NumberUtils.toInt(cluster.elementTextTrim("redis-max-idle"), 20);
			JettyProfile.Redis_MaxWait = NumberUtils.toLong(cluster.elementTextTrim("redis-max-wait"), 10000L);
			JettyProfile.Redis_Timeout = NumberUtils.toInt(cluster.elementTextTrim("redis-timeout"), 3000);
			
		} catch (Exception e) {
			log.warn("Parse Server Config Error.", e);
		}
	}
}