package me.jetty.ti.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import me.jetty.ti.etc.JettyProfile;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午11:11:54
 */
public class ProfileHolder {
	
	protected final static Logger log = Log.getLogger(ProfileHolder.class);
	
	private static JettyProfile profile;
	
	public static void main(String[] args) {
		try {
			JettyProfile profile = XmlUtils.toObj(JettyProfile.class, StreamUtils.copyToString(new FileInputStream("etc/profile.xml"), Charset.forName("UTF-8")), "server");
			System.out.println(profile);
		} catch (IOException e) {
			log.info("Reading Jetty Profile Error.", e);
			System.exit(-1);
		}
	}

	public static JettyProfile getProfile() {
		if(ProfileHolder.profile != null) {
			return ProfileHolder.profile;
		}
		try {
			ProfileHolder.profile = XmlUtils.toObj(JettyProfile.class, StreamUtils.copyToString(new FileInputStream("../etc/profile.xml"), Charset.forName("UTF-8")), "server");
		} catch (IOException e) {
			log.info("Reading Jetty Profile Error.", e);
			System.exit(-1);
		}
		return ProfileHolder.profile;
	}
}