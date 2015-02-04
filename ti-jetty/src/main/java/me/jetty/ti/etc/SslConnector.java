package me.jetty.ti.etc;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年2月4日 上午12:04:12
 */
@XStreamAlias("ssl-connector")
public class SslConnector extends Connector {
	@XStreamAlias("key-store-path")
	private String keyStorePath;
	
	@XStreamAlias("key-store-password")
	private String keyStorePassword;
	
	@XStreamAlias("key-manager-password")
	private String keyManagerPassword;
	
	@XStreamAlias("trust-store-path")
	private String trustStorePath;
	
	@XStreamAlias("trust-store-password")
	private String trustStorePassword;

	@XStreamAsAttribute
	@XStreamAlias("client-auth")
	private boolean clientAuth;

	@XStreamAsAttribute
	@XStreamAlias("cert-alias")
	private String certAlias;

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