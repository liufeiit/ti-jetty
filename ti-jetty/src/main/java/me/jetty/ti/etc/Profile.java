package me.jetty.ti.etc;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年2月4日 上午12:02:40
 */
@XStreamAlias("server")
public class Profile {
	
	@XStreamImplicit(itemFieldName = "connector")
	private List<HttpConnector> httpConnectors;

	@XStreamImplicit(itemFieldName = "ssl-connector")
	private List<HttpsConnector> httpsConnectors;
	
	@XStreamAlias("name")
	private String name;
}