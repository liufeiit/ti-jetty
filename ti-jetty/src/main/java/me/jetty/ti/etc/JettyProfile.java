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
	
	@XStreamImplicit(itemFieldName = "resource")
	private List<Resource> resources;

	@XStreamAlias("thread-pool")
	private ThreadPool threadPool;

	@XStreamAlias("session")
	private Session session;

	@XStreamAlias("redis")
	private Redis redis;

	@XStreamImplicit(itemFieldName = "context-mapping")
	private List<ContextMapping> mappings;

	@XStreamAsAttribute
	@XStreamAlias("redis-session-enable")
	private boolean redisSessionEnable = false;

	@XStreamAsAttribute
	@XStreamAlias("dump-std-err")
	private boolean dumpStdErr = false;
	
	@XStreamAsAttribute
	@XStreamAlias("rollback-enable")
	private boolean rollback = false;
	
	@XStreamAsAttribute
	@XStreamAlias("backup-log")
	private boolean backup = false;
	
	@XStreamAsAttribute
	@XStreamAlias("single-app-root")
	private boolean singleAppRoot = true;
	
	@XStreamAsAttribute
	@XStreamAlias("token-expires-in-sec")
	private int tokenExpiresInSec = 5184000;

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public boolean isSingleAppRoot() {
		return singleAppRoot;
	}

	public void setSingleAppRoot(boolean singleAppRoot) {
		this.singleAppRoot = singleAppRoot;
	}

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

	public ThreadPool getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
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

	public boolean isDumpStdErr() {
		return dumpStdErr;
	}

	public void setDumpStdErr(boolean dumpStdErr) {
		this.dumpStdErr = dumpStdErr;
	}

	public int getTokenExpiresInSec() {
		if(tokenExpiresInSec <= 0) {
			return 60 * 60 * 24 * 30;
		}
		return tokenExpiresInSec;
	}

	public void setTokenExpiresInSec(int tokenExpiresInSec) {
		this.tokenExpiresInSec = tokenExpiresInSec;
	}

	public boolean isRollback() {
		return rollback;
	}

	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}

	public boolean isBackup() {
		return backup;
	}

	public void setBackup(boolean backup) {
		this.backup = backup;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}