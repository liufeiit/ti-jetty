package me.jetty.ti.etc;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年2月4日 下午5:03:13
 */
@XStreamAlias("session")
public class Session {
	
	@XStreamAlias("domain")
	private String sessionDomain;
	
	@XStreamAlias("path")
	private String sessionPath;
	
	@XStreamAlias("max-age")
	private int sessionMaxAge;
	
	@XStreamAlias("refresh-cookie-age")
	private int sessionAgeInSeconds;
	
	@XStreamAlias("worker-name")
	private String sessionWorkerName;
	
	@XStreamAlias("save-interval")
	private long sessionSaveInterval;
	
	@XStreamAlias("scavenger-interval")
	private long sessionScavengerInterval;

	public String getSessionDomain() {
		return sessionDomain;
	}

	public void setSessionDomain(String sessionDomain) {
		this.sessionDomain = sessionDomain;
	}

	public String getSessionPath() {
		return sessionPath;
	}

	public void setSessionPath(String sessionPath) {
		this.sessionPath = sessionPath;
	}

	public int getSessionMaxAge() {
		return sessionMaxAge;
	}

	public void setSessionMaxAge(int sessionMaxAge) {
		this.sessionMaxAge = sessionMaxAge;
	}

	public int getSessionAgeInSeconds() {
		return sessionAgeInSeconds;
	}

	public void setSessionAgeInSeconds(int sessionAgeInSeconds) {
		this.sessionAgeInSeconds = sessionAgeInSeconds;
	}

	public String getSessionWorkerName() {
		return sessionWorkerName;
	}

	public void setSessionWorkerName(String sessionWorkerName) {
		this.sessionWorkerName = sessionWorkerName;
	}

	public long getSessionSaveInterval() {
		return sessionSaveInterval;
	}

	public void setSessionSaveInterval(long sessionSaveInterval) {
		this.sessionSaveInterval = sessionSaveInterval;
	}

	public long getSessionScavengerInterval() {
		return sessionScavengerInterval;
	}

	public void setSessionScavengerInterval(long sessionScavengerInterval) {
		this.sessionScavengerInterval = sessionScavengerInterval;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}