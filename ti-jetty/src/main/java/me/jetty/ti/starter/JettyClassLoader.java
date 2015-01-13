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

	public JettyClassLoader(ClassLoader parent, File libDir) throws MalformedURLException {
		super(new URL[] { libDir.toURI().toURL() }, parent);
		File[] jars = libDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				boolean accept = false;
				String smallName = name.toLowerCase();
				if (smallName.endsWith(".jar")) {
					accept = true;
				} else if (smallName.endsWith(".zip")) {
					accept = true;
				}
				return accept;
			}
		});

		if (jars == null) {
			return;
		}

		for (int i = 0; i < jars.length; i++) {
			if (jars[i].isFile()) {
				addURL(jars[i].toURI().toURL());
			}
		}
	}
}