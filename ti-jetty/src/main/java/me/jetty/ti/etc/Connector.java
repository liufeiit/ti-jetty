package me.jetty.ti.etc;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年2月4日 上午12:03:53
 */
@XStreamAlias("connector")
public class Connector {
	
	@XStreamAsAttribute
	@XStreamAlias("port")
	private int port;
	
	@XStreamAsAttribute
	@XStreamAlias("enable")
	private boolean enable;
	
	@XStreamAsAttribute
	@XStreamAlias("accept-queue-size")
	private int acceptQueueSize;
	
	@XStreamAsAttribute
	@XStreamAlias("max-idle-time")
	private int maxIdleTime;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getAcceptQueueSize() {
		return acceptQueueSize;
	}

	public void setAcceptQueueSize(int acceptQueueSize) {
		this.acceptQueueSize = acceptQueueSize;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}