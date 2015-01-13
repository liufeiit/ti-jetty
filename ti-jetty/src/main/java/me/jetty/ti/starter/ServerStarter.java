package me.jetty.ti.starter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年1月14日 上午1:10:05
 */
public class ServerStarter {

	private final static Logger log = Log.getLogger(ServerStarter.class);

	private static final String DEFAULT_LIB_DIR = "../lib";

	private static final String DEFAULT_EXT_LIB_DIR = "../ext";

	public static void main(String[] args) {
		new ServerStarter().start();
	}

	private void start() {
		try {
			final ClassLoader parent = findParentClassLoader();
			File libDir = new File(DEFAULT_LIB_DIR);
			unpackArchives(libDir, true);
			File extLibDir = new File(DEFAULT_EXT_LIB_DIR);
			if (extLibDir.exists()) {
				unpackArchives(extLibDir, false);
			}
			ClassLoader loader = new JettyClassLoader(parent, libDir);
			Thread.currentThread().setContextClassLoader(loader);
			Class containerClass = loader.loadClass("me.jetty.ti.srv.JettyServer");
			containerClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ClassLoader findParentClassLoader() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = this.getClass().getClassLoader();
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
		for (File packedFile : packedFiles) {
			try {
				String jarName = packedFile.getName().substring(0, packedFile.getName().length() - ".pack".length());
				File jarFile = new File(libDir, jarName);
				if (jarFile.exists()) {
					jarFile.delete();
				}
				InputStream in = new BufferedInputStream(new FileInputStream(packedFile));
				JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(new File(
						libDir, jarName))));
				Pack200.Unpacker unpacker = Pack200.newUnpacker();
				if (printStatus) {
					System.out.print(".");
				}
				unpacker.unpack(in, out);
				in.close();
				out.close();
				packedFile.delete();
				unpacked = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (unpacked && printStatus) {
			System.out.println();
		}
	}
}