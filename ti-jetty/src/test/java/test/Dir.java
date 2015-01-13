package test;

import java.io.File;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年1月14日 上午12:30:24
 */
public class Dir {

	public static void main(String[] args) throws Exception {
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		System.out.println(Dir.class.getClassLoader().getResource(""));
		System.out.println(ClassLoader.getSystemResource(""));
		System.out.println(Dir.class.getResource(""));
		System.out.println(Dir.class.getResource("/"));// Class文件所在路径
		System.out.println(new File("/").getAbsolutePath());
		System.out.println(new File("/").getCanonicalPath());
		System.out.println(System.getProperty("user.dir"));
		System.out.println(System.getProperty("java.class.path"));
	}
}
