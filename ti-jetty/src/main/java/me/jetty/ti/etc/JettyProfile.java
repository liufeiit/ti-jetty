package me.jetty.ti.etc;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年2月4日 上午12:02:40
 */
@XStreamAlias("server")
public class JettyProfile {

	@XStreamImplicit(itemFieldName = "connector")
	private List<Connector> connectors;

	@XStreamImplicit(itemFieldName = "ssl-connector")
	private List<SslConnector> sslConnectors;

	@XStreamAlias("queued-pool")
	private QueuedPool queuedPool;

	@XStreamAlias("session")
	private Session session;

	@XStreamAlias("redis")
	private Redis redis;

	@XStreamImplicit(itemFieldName = "context-mapping")
	private List<ContextMapping> mappings;

	@XStreamAsAttribute
	@XStreamAlias("redis-session-enable")
	private boolean redisSessionEnable;

	public List<Connector> getConnectors() {
		return connectors;
	}

	public void setConnectors(List<Connector> connectors) {
		this.connectors = connectors;
	}

	public List<SslConnector> getSslConnectors() {
		return sslConnectors;
	}

	public void setSslConnectors(List<SslConnector> sslConnectors) {
		this.sslConnectors = sslConnectors;
	}

	public QueuedPool getQueuedPool() {
		return queuedPool;
	}

	public void setQueuedPool(QueuedPool queuedPool) {
		this.queuedPool = queuedPool;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Redis getRedis() {
		return redis;
	}

	public void setRedis(Redis redis) {
		this.redis = redis;
	}

	public List<ContextMapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<ContextMapping> mappings) {
		this.mappings = mappings;
	}

	public boolean isRedisSessionEnable() {
		return redisSessionEnable;
	}

	public void setRedisSessionEnable(boolean redisSessionEnable) {
		this.redisSessionEnable = redisSessionEnable;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}