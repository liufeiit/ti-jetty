package me.jetty.ti.starter;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年1月14日 上午1:04:54
 */
public class JettyClassLoader extends URLClassLoader {

	JettyClassLoader(ClassLoader parent, File libDir) throws MalformedURLException {
		super(new URL[] { libDir.toURI().toURL() }, parent);
		File[] jars = libDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String smallName = name.toLowerCase();
				if (smallName.endsWith(".jar")) {
					return true;
				}
				if (smallName.endsWith(".zip")) {
					return true;
				}
				return false;
			}
		});
		if (jars == null) {
			return;
		}
		for (int i = 0; i < jars.length; i++) {
			if (!jars[i].isFile()) {
				continue;
			}
			URL jar = jars[i].toURI().toURL();
			System.err.println("JettyClassLoader loading Jar : " + jar);
			addURL(jar);
		}
	}
}