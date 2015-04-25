package me.jetty.ti.starter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

import me.jetty.ti.server.JettyServer;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午6:20:11
 */
public class ServerStarter {

	private final static Logger log = Log.getLogger(ServerStarter.class);

	private static final String DEFAULT_LIB_DIR = "../lib";

	public static void main(String[] args) {
		try {
			Class<?> jettyServerClass = new ServerStarter().loader(JettyServer.class.getName());
			JettyServer jettyServer = (JettyServer) jettyServerClass.newInstance();
			jettyServer.start();
		} catch (Exception e) {
			log.warn("Jetty Server Started Error.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> loader(String className) throws Exception {
		final ClassLoader parent = findParentClassLoader();
		File libDir = new File(DEFAULT_LIB_DIR);
		unpackArchives(libDir, true);
		ClassLoader loader = new JettyClassLoader(parent, libDir);
		Thread.currentThread().setContextClassLoader(loader);
		return (Class<T>) loader.loadClass(className);
	}

	private ClassLoader findParentClassLoader() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = getClass().getClassLoader();
			if (parent == null) {
				parent = ClassLoader.getSystemClassLoader();
			}
		}
		return parent;
	}

	private void unpackArchives(File libDir, boolean printStatus) {
		File[] packedFiles = libDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".pack");
			}
		});
		if (packedFiles == null) {
			return;
		}
		boolean unpacked = false;
		Pack200.Unpacker unpacker = Pack200.newUnpacker();
		for (File packedFile : packedFiles) {
			InputStream in = null;
			JarOutputStream out = null;
			try {
				String jarName = packedFile.getName().substring(0, packedFile.getName().length() - ".pack".length());
				File jarFile = new File(libDir, jarName);
				if (jarFile.exists()) {
					jarFile.delete();
				}
				in = new BufferedInputStream(new FileInputStream(packedFile));
				out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(new File(libDir, jarName))));
				if (printStatus) {
					System.out.print(".");
				}
				unpacker.unpack(in, out);
				packedFile.delete();
				unpacked = true;
			} catch (Exception e) {
				log.warn("Unpack Error.\nPackedFile : " + packedFile, e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						log.warn("close stream Error.", e);
					}
					in = null;
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						log.warn("close stream Error.", e);
					}
					out = null;
				}
			}
		}
		if (unpacked && printStatus) {
			System.out.println();
		}
	}
}