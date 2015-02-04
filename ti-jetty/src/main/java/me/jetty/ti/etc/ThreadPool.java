package me.jetty.ti.etc;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年2月4日 下午4:47:28
 */
@XStreamAlias("thread-pool")
public class ThreadPool {

	@XStreamAsAttribute
	@XStreamAlias("name")
	private String name;

	@XStreamAsAttribute
	@XStreamAlias("daemon")
	private boolean daemon;
	
	@XStreamAlias("min-threads")
	private int minThreads;
	
	@XStreamAlias("max-threads")
	private int maxThreads;
	
	@XStreamAlias("max-queued")
	private int maxQueued;
	
	@XStreamAlias("max-idle-time-ms")
	private int maxIdleTimeMs;
	
	@XStreamAlias("max-stop-time-ms")
	private int maxStopTimeMs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinThreads() {
		return minThreads;
	}

	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getMaxQueued() {
		return maxQueued;
	}

	public void setMaxQueued(int maxQueued) {
		this.maxQueued = maxQueued;
	}

	public int getMaxIdleTimeMs() {
		return maxIdleTimeMs;
	}

	public void setMaxIdleTimeMs(int maxIdleTimeMs) {
		this.maxIdleTimeMs = maxIdleTimeMs;
	}

	public int getMaxStopTimeMs() {
		return maxStopTimeMs;
	}

	public void setMaxStopTimeMs(int maxStopTimeMs) {
		this.maxStopTimeMs = maxStopTimeMs;
	}

	public boolean isDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}