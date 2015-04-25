package me.jetty.ti.etc;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年4月25日 下午1:00:31
 */
@XStreamAlias("resource")
public class Resource {
	
	@XStreamAsAttribute
	@XStreamAlias("directories-listed")
	private boolean directoriesListed = true;
	
	@XStreamAsAttribute
	@XStreamAlias("welcome-files")
	private String welcomeFiles;
	
	@XStreamAsAttribute
	@XStreamAlias("base-resource")
	private String baseResource;
	
	@XStreamAsAttribute
	@XStreamAlias("stylesheet")
	private String stylesheet;
	
	@XStreamAsAttribute
	@XStreamAlias("etags")
	private boolean etags = true;
	
	@XStreamAsAttribute
	@XStreamAlias("aliases")
	private boolean aliases = true;
	
	@XStreamAsAttribute
	@XStreamAlias("cache-control")
	private String cacheControl;
}