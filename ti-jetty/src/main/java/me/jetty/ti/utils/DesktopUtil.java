package me.jetty.ti.utils;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月19日 下午4:11:21
 */
public class DesktopUtil {
	private static final Logger log = Logger.getLogger(DesktopUtil.class.getName());

	private static final String OS_MACOS = "Mac OS";
	private static final String OS_WINDOWS = "Windows";

	private static final String[] UNIX_BROWSE_CMDS = { "www-browser", "firefox", "opera", "konqueror", "epiphany",
			"mozilla", "netscape", "w3m", "lynx" };

	private static final String[] UNIX_OPEN_CMDS = { "run-mailcap", "pager", "less", "more" };

	private DesktopUtil() {
	}

	/**
	 * 使用系统默认浏览器启动指定URL指向的地址
	 * 
	 * @param url
	 *            the URL to open
	 * @throws IOException
	 *             if a browser couldn't be found or if the URL failed to launch
	 */
	public static void browse(final URL url) throws IOException {
		// Try Java 1.6 Desktop class if supported
		if (browseDesktop(url))
			return;

		final String osName = System.getProperty("os.name");
		log.finer("Launching " + url + " for OS " + osName);

		if (osName.startsWith(OS_MACOS)) {
			browseMac(url);
		} else if (osName.startsWith(OS_WINDOWS)) {
			browseWindows(url);
		} else {
			// assume Unix or Linux
			browseUnix(url);
		}
	}

	/**
	 * 使用系统默认浏览器启动指定URL指向的地址, 错误时给出提示对话框。
	 * 
	 * @param url
	 *            the URL to open
	 * @param parentComponent
	 *            The parent Component over which the error dialog should be
	 *            shown
	 */
	public static void browseAndWarn(final URL url, final Component parentComponent) {
		try {
			browse(url);
		} catch (final IOException e) {
			log.log(Level.SEVERE, "Unable to browse to " + url, e);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(parentComponent,
							"Couldn't open a web browser:\n" + e.getLocalizedMessage(), "Unable to launch web browser",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}
	}

	/**
	 * 使用系统默认浏览器启动指定URL指向的地址, 错误时给出提示对话框。
	 * 
	 * @param url
	 *            the URL to open
	 * @param parentComponent
	 *            The parent Component over which the error dialog should be
	 *            shown
	 */
	public static void browseAndWarn(final String url, final Component parentComponent) {
		try {
			browse(new URL(url));
		} catch (final IOException e) {
			log.log(Level.SEVERE, "Unable to browse to " + url, e);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(parentComponent,
							"Couldn't open a web browser:\n" + e.getLocalizedMessage(), "Unable to launch web browser",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}
	}

	/**
	 * 使用系统默认程序启动指定文件。
	 * 
	 * @param file
	 *            the File to open
	 * @throws IOException
	 *             if an application couldn't be found or if the File failed to
	 *             launch
	 */
	public static void open(final File file) throws IOException {
		// Try Java 1.6 Desktop class if supported
		if (openDesktop(file))
			return;

		final String osName = System.getProperty("os.name");
		log.finer("Opening " + file + " for OS " + osName);

		if (osName.startsWith(OS_MACOS)) {
			openMac(file);
		} else if (osName.startsWith(OS_WINDOWS)) {
			openWindows(file);
		} else {
			// assume Unix or Linux
			openUnix(file);
		}
	}

	/**
	 * 打开指定文件, 失败时在parentComponent指定的AWT组件前面弹出消息对话框。
	 * 
	 * @param file
	 *            the File to open
	 * @param parentComponent
	 *            The parent Component over which the error dialog should be
	 *            shown
	 */
	public static void openAndWarn(final File file, final Component parentComponent) {
		try {
			open(file);
		} catch (final IOException e) {
			log.log(Level.SEVERE, "Unable to open " + file, e);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(parentComponent, "不能打开文件 " + file + ":\n" + e.getLocalizedMessage(),
							"不能打开文件", JOptionPane.ERROR_MESSAGE);
				}
			});
		}
	}

	/**
	 * 不直接调用java.awt.Desktop, 而是使用反射去加载java.awt.Desktop类，这样当jre版本小于1.6时就不会执行,
	 * true表示执行成功, false表示执行不成劄1.7, 即jre版本小于1.6
	 * 
	 * @param url
	 *            the URL to launch
	 * @return true if launch successful, false if we should fall back to other
	 *         methods
	 * @throws IOException
	 *             if Desktop was found, but the browse() call failed.
	 */
	private static boolean browseDesktop(final URL url) throws IOException {
		final Class<?> desktopClass = getDesktopClass();
		if (desktopClass == null)
			return false;

		final Object desktopInstance = getDesktopInstance(desktopClass);
		if (desktopInstance == null)
			return false;

		log.finer("Launching " + url + " using Desktop.browse()");

		try {
			final Method browseMethod = desktopClass.getDeclaredMethod("browse", URI.class);
			browseMethod.invoke(desktopInstance, new URI(url.toExternalForm()));
			return true;
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof IOException) {
				throw (IOException) e.getCause();
			} else {
				log.log(Level.FINE, "Exception in Desktop operation", e);
				return false;
			}
		} catch (Exception e) {
			log.log(Level.FINE, "Exception in Desktop operation", e);
			return false;
		}
	}

	/**
	 * 在windows操作系统下使用url.dll打开指定URL地址.
	 * 
	 * @param url
	 *            the URL to launch
	 * @throws IOException
	 *             if the launch failed
	 */
	private static void browseWindows(final URL url) throws IOException {
		log.finer("Windows invoking rundll32");
		Runtime.getRuntime().exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", url.toString() });
	}

	/**
	 * 在Unix系统下打弄1.7指定URL地址.
	 * 
	 * @param url
	 *            the URL to launch
	 * @throws IOException
	 *             if the launch failed
	 */
	private static void browseUnix(final URL url) throws IOException {
		for (final String cmd : UNIX_BROWSE_CMDS) {
			log.finest("Unix looking for " + cmd);
			if (unixCommandExists(cmd)) {
				log.finer("Unix found " + cmd);
				Runtime.getRuntime().exec(new String[] { cmd, url.toString() });
				return;
			}
		}
		throw new IOException("Could not find a suitable web browser");
	}

	/**
	 * 
	 * 通过反射使用com.apple.eio.FileManager.
	 * 
	 * @param url
	 *            the URL to launch
	 * @throws IOException
	 *             if the launch failed
	 */
	private static void browseMac(final URL url) throws IOException {
		try {
			final Class<?> fileMgr = getAppleFileManagerClass();
			final Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);

			log.finer("Mac invoking");
			openURL.invoke(null, url.toString());
		} catch (Exception e) {
			log.log(Level.WARNING, "Couldn't launch Mac URL", e);
			throw new IOException("Could not launch Mac URL: " + e.getLocalizedMessage());
		}
	}

	/**
	 * 不直接调用java.awt.Desktop., 而是使用反射去加载java.awt.Desktop类，这样当jre版本小于1.6时就不会执行,
	 * true表示执行成功, false表示执行不成劄1.7, 即jre版本小于1.6
	 * 
	 * @param file
	 *            the File to open
	 * @return true if open successful, false if we should fall back to other
	 *         methods
	 * @throws IOException
	 *             if Desktop was found, but the open() call failed.
	 */
	private static boolean openDesktop(final File file) throws IOException {
		final Class<?> desktopClass = getDesktopClass();
		if (desktopClass == null)
			return false;

		final Object desktopInstance = getDesktopInstance(desktopClass);
		if (desktopInstance == null)
			return false;

		log.finer("Opening " + file + " using Desktop.open()");

		try {
			final Method browseMethod = desktopClass.getDeclaredMethod("open", File.class);
			browseMethod.invoke(desktopInstance, file);
			return true;
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof IOException) {
				throw (IOException) e.getCause();
			} else if (e.getCause() instanceof IllegalArgumentException) {
				throw new FileNotFoundException(e.getCause().getLocalizedMessage());
			} else {
				log.log(Level.FINE, "Exception in Desktop operation", e);
				return false;
			}
		} catch (Exception e) {
			log.log(Level.FINE, "Exception in Desktop operation", e);
			return false;
		}
	}

	/**
	 * 在windows操作系统下使用shell32.dll打开指定文件.
	 * 
	 * @param file
	 *            the File to open
	 * @throws IOException
	 *             if the open failed
	 */
	private static void openWindows(final File file) throws IOException {
		log.finer("Windows invoking rundll32");
		Runtime.getRuntime().exec(new String[] { "rundll32", "shell32.dll,ShellExec_RunDLL", file.getAbsolutePath() });
	}

	/**
	 * 通过反射使用com.apple.eio.FileManager.
	 * 
	 * @param file
	 *            the File to open
	 * @throws IOException
	 *             if the open failed
	 */
	@SuppressWarnings("deprecation")
	private static void openMac(final File file) throws IOException {
		// we use openURL() on the file's URL form since openURL supports
		// file:// protocol
		browseMac(file.getAbsoluteFile().toURL());
	}

	/**
	 * 在Unix系统下打弄1.7指定文件.
	 * 
	 * @param file
	 *            the File to open
	 * @throws IOException
	 *             if the open failed
	 */
	private static void openUnix(final File file) throws IOException {
		for (final String cmd : UNIX_OPEN_CMDS) {
			log.finest("Unix looking for " + cmd);
			if (unixCommandExists(cmd)) {
				log.finer("Unix found " + cmd);
				Runtime.getRuntime().exec(new String[] { cmd, file.getAbsolutePath() });
				return;
			}
		}
		throw new IOException("Could not find a suitable viewer");
	}

	/**
	 * 判断当前系统的JRE是否有java.awt.Desktop类库, 即是否是jre1.6或其以上版本.
	 * 
	 * @return the Desktop class object, or null if it could not be found
	 */
	private static Class<?> getDesktopClass() {
		// NB The following String is intentionally not inlined to prevent
		// ProGuard trying to locate the unknown class.
		final String desktopClassName = "java.awt.Desktop";
		try {
			return Class.forName(desktopClassName);
		} catch (ClassNotFoundException e) {
			log.fine("Desktop class not found");
			return null;
		}
	}

	/**
	 * 
	 * 获取java.awt.Desktop类的实例，如果支持1.7我们检查isDesktopSupported()
	 * 但为了方便，我们也懒得去检查1.7查isSupported(方法)，是 调用者处理任何UnsupportedOperationExceptions
	 * 
	 * @param desktopClass
	 *            the Desktop Class object
	 * @return the Desktop instance, or null if it is not supported
	 */
	private static Object getDesktopInstance(final Class<?> desktopClass) {
		try {
			final Method isDesktopSupportedMethod = desktopClass.getDeclaredMethod("isDesktopSupported");
			log.finest("invoking isDesktopSupported");
			final boolean isDesktopSupported = (Boolean) isDesktopSupportedMethod.invoke(null);

			if (!isDesktopSupported) {
				log.finer("isDesktopSupported: no");
				return null;
			}

			final Method getDesktopMethod = desktopClass.getDeclaredMethod("getDesktop");
			return getDesktopMethod.invoke(null);
		} catch (Exception e) {
			log.log(Level.FINE, "Exception in Desktop operation", e);
			return null;
		}
	}

	/**
	 * 在MAC系统下找到com.apple.eio.FileManager.
	 * 
	 * @return the FileManager instance
	 * @throws ClassNotFoundException
	 *             if FileManager was not found
	 */
	private static Class<?> getAppleFileManagerClass() throws ClassNotFoundException {
		log.finest("Mac looking for com.apple.eio.FileManager");

		// NB The following String is intentionally not inlined to prevent
		// ProGuard trying to locate the unknown class.
		final String appleClass = "com.apple.eio.FileManager";
		return Class.forName(appleClass);
	}

	/**
	 * 是否存在一个给定的可执行文件，通过"which"指命令.
	 * 
	 * @param cmd
	 *            the executable to locate
	 * @return true if the executable was found
	 * @throws IOException
	 *             if Runtime.exec() throws an IOException
	 */
	private static boolean unixCommandExists(final String cmd) throws IOException {
		final Process whichProcess = Runtime.getRuntime().exec(new String[] { "which", cmd });

		boolean finished = false;
		do {
			try {
				whichProcess.waitFor();
				finished = true;
			} catch (InterruptedException e) {
				log.log(Level.WARNING, "Interrupted waiting for which to complete", e);
			}
		} while (!finished);

		return whichProcess.exitValue() == 0;
	}
}