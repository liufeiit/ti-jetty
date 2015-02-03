package me.srv.ti.etc;

import me.srv.ti.jx.XPath;
import me.srv.ti.jx.XRoot;
import me.srv.ti.ns.NsRegistry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月30日 下午1:55:34
 */
@XRoot("../etc/profile.xml")
//@XRoot("/Users/yp/workspace/ti-jetty/ti-jetty/etc/profile.xml")
public class JettyProfile {

	@XPath("/server/@port")
	private int port;

	@XPath("/server/@context-path")
	private String contextPath;
	@XPath("/server/@war")
	private String war;
	
	@XPath("/server/connector/@ssl-enable")
	private boolean sslEnable;
	
	@XPath("/server/connector/key-store-path")
	private String keyStorePath;
	@XPath("/server/connector/key-store-password")
	private String keyStorePassword;
	@XPath("/server/connector/key-manager-password")
	private String keyManagerPassword;

	@XPath("/server/connector/trust-store-path")
	private String trustStorePath;
	@XPath("/server/connector/trust-store-password")
	private String trustStorePassword;
	
	@XPath("/server/connector/@client-auth")
	private boolean clientAuth;
	@XPath("/server/connector/cert-alias")
	private String certAlias;

	/** 每个请求被accept前允许等待的连接数 **/
	@XPath("/server/connector/@accept-queue-size")
	private int acceptQueueSize;
	/** 连接最大空闲时间，默认是200000，-1表示一直连接 **/
	@XPath("/server/connector/@max-idle-time")
	private int maxIdleTime;

	@XPath("/server/queued/name")
	private String queuedName;
	@XPath("/server/queued/min-threads")
	private int queuedMinThreads;
	@XPath("/server/queued/max-threads")
	private int queuedMaxThreads;
	@XPath("/server/queued/max-queued")
	private int queuedMaxQueued;
	@XPath("/server/queued/max-idle-time-ms")
	private int queuedMaxIdleTimeMs;
	@XPath("/server/queued/max-stop-time-ms")
	private int queuedMaxStopTimeMs;

	@XPath("/server/redis/@sessions")
	private boolean redisSession;

	@XPath("/server/redis/host")
	private String redisHost;
	@XPath("/server/redis/port")
	private int redisPort;
	@XPath("/server/redis/max-active")
	private int redisMaxActive;
	@XPath("/server/redis/min-idle")
	private int redisMinIdle;
	@XPath("/server/redis/max-idle")
	private int redisMaxIdle;
	@XPath("/server/redis/max-wait")
	private long redisMaxWait;
	@XPath("/server/redis/timeout")
	private int redisTimeout;

	@XPath("/server/session/domain")
	private String sessionDomain;
	@XPath("/server/session/path")
	private String sessionPath;
	@XPath("/server/session/max-age")
	private int sessionMaxAge;
	@XPath("/server/session/refresh-cookie-age")
	private int sessionAgeInSeconds;
	@XPath("/server/session/worker-name")
	private String sessionWorkerName;
	@XPath("/server/session/save-interval")
	private long sessionSaveInterval;
	@XPath("/server/session/scavenger-interval")
	private long sessionScavengerInterval;
	
	public static void main(String[] args) {
		System.out.println(NsRegistry.DEFAULT_NS_REGISTRY.newInstance(new JettyProfile()));
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getWar() {
		return war;
	}

	public void setWar(String war) {
		this.war = war;
	}

	public boolean isRedisSession() {
		return redisSession;
	}

	public void setRedisSession(boolean redisSession) {
		this.redisSession = redisSession;
	}

	public boolean isSslEnable() {
		return sslEnable;
	}

	public void setSslEnable(boolean sslEnable) {
		this.sslEnable = sslEnable;
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

	public String getQueuedName() {
		return queuedName;
	}

	public void setQueuedName(String queuedName) {
		this.queuedName = queuedName;
	}

	public int getQueuedMinThreads() {
		return queuedMinThreads;
	}

	public void setQueuedMinThreads(int queuedMinThreads) {
		this.queuedMinThreads = queuedMinThreads;
	}

	public int getQueuedMaxThreads() {
		return queuedMaxThreads;
	}

	public void setQueuedMaxThreads(int queuedMaxThreads) {
		this.queuedMaxThreads = queuedMaxThreads;
	}

	public int getQueuedMaxQueued() {
		return queuedMaxQueued;
	}

	public void setQueuedMaxQueued(int queuedMaxQueued) {
		this.queuedMaxQueued = queuedMaxQueued;
	}

	public int getQueuedMaxIdleTimeMs() {
		return queuedMaxIdleTimeMs;
	}

	public void setQueuedMaxIdleTimeMs(int queuedMaxIdleTimeMs) {
		this.queuedMaxIdleTimeMs = queuedMaxIdleTimeMs;
	}

	public int getQueuedMaxStopTimeMs() {
		return queuedMaxStopTimeMs;
	}

	public void setQueuedMaxStopTimeMs(int queuedMaxStopTimeMs) {
		this.queuedMaxStopTimeMs = queuedMaxStopTimeMs;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public int getRedisMaxActive() {
		return redisMaxActive;
	}

	public void setRedisMaxActive(int redisMaxActive) {
		this.redisMaxActive = redisMaxActive;
	}

	public int getRedisMinIdle() {
		return redisMinIdle;
	}

	public void setRedisMinIdle(int redisMinIdle) {
		this.redisMinIdle = redisMinIdle;
	}

	public int getRedisMaxIdle() {
		return redisMaxIdle;
	}

	public void setRedisMaxIdle(int redisMaxIdle) {
		this.redisMaxIdle = redisMaxIdle;
	}

	public long getRedisMaxWait() {
		return redisMaxWait;
	}

	public void setRedisMaxWait(long redisMaxWait) {
		this.redisMaxWait = redisMaxWait;
	}

	public int getRedisTimeout() {
		return redisTimeout;
	}

	public void setRedisTimeout(int redisTimeout) {
		this.redisTimeout = redisTimeout;
	}

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

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getKeyManagerPassword() {
		return keyManagerPassword;
	}

	public void setKeyManagerPassword(String keyManagerPassword) {
		this.keyManagerPassword = keyManagerPassword;
	}

	public String getTrustStorePath() {
		return trustStorePath;
	}

	public void setTrustStorePath(String trustStorePath) {
		this.trustStorePath = trustStorePath;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public boolean isClientAuth() {
		return clientAuth;
	}

	public void setClientAuth(boolean clientAuth) {
		this.clientAuth = clientAuth;
	}

	public String getCertAlias() {
		return certAlias;
	}

	public void setCertAlias(String certAlias) {
		this.certAlias = certAlias;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}