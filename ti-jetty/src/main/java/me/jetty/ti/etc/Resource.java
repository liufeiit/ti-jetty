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
	@XStreamAlias("context")
	private String context;
	
	@XStreamAsAttribute
	@XStreamAlias("enable")
	private boolean enable;

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
	private String cacheControl = "no-store,no-cache,must-revalidate";

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isDirectoriesListed() {
		return directoriesListed;
	}

	public void setDirectoriesListed(boolean directoriesListed) {
		this.directoriesListed = directoriesListed;
	}

	public String getWelcomeFiles() {
		return welcomeFiles;
	}

	public void setWelcomeFiles(String welcomeFiles) {
		this.welcomeFiles = welcomeFiles;
	}

	public String getBaseResource() {
		return baseResource;
	}

	public void setBaseResource(String baseResource) {
		this.baseResource = baseResource;
	}

	public String getStylesheet() {
		return stylesheet;
	}

	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}

	public boolean isEtags() {
		return etags;
	}

	public void setEtags(boolean etags) {
		this.etags = etags;
	}

	public boolean isAliases() {
		return aliases;
	}

	public void setAliases(boolean aliases) {
		this.aliases = aliases;
	}

	public String getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}
}