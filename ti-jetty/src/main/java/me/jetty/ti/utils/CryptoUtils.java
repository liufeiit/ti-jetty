package me.jetty.ti.utils;

import java.nio.charset.Charset;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 下午2:39:38
 */
public class CryptoUtils {
	
	protected static final Logger log = Log.getLogger(CryptoUtils.class);

	private static final Charset UTF_8 = Charset.forName("UTF-8");
	private static final String HMAC_256 = "HmacSHA256";
	
	public static String sign(String sources, String secret) {
		String result = sources;
		try {
			Mac sha256_HMAC = Mac.getInstance(HMAC_256);
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(UTF_8), HMAC_256);
			sha256_HMAC.init(secret_key);
			byte sig[] = sha256_HMAC.doFinal(sources.getBytes(UTF_8));
			result = Base64.encodeBase64URLSafeString(sig);
		} catch (Exception e) {
			log.warn("Sign Error.", e);
		}
		return result;
	}
}