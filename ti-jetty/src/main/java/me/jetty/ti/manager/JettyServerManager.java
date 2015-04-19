package me.jetty.ti.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import me.jetty.ti.srv.Server;
import me.jetty.ti.srv.ServerStartedCallback;
import me.jetty.ti.srv.ServerStopedCallback;
import me.jetty.ti.utils.DesktopUtil;
import me.jetty.ti.utils.ProfileHolder;
import me.jetty.ti.utils.ResourceUtils;

import com.sun.awt.AWTUtilities;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午3:39:48
 */
public class JettyServerManager extends Window implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "服务器管理";

	private static final JettyServerManager JETTY_SERVER_MANAGER = new JettyServerManager();

	private JButton open = new JButton("打开");
	private JButton start = new JButton("启动");
	private JButton restart = new JButton("重启");
	private JButton min = new JButton("最小化");

	private final PopupMenu pop = new PopupMenu();
	private MenuItem openItem = new MenuItem("Open");
	private MenuItem restartItem = new MenuItem("Restart");
	private MenuItem exitItem = new MenuItem("Exit");

	private TrayIcon trayicon;

	public static void main(String[] args) {
		JettyServerManager.showJettyServer(true);
	}

	private JettyServerManager() throws HeadlessException {
		this(TITLE);
	}

	private JettyServerManager(String title) throws HeadlessException {
		super(title);
		AWTUtilities.setWindowOpacity(this, 0.85F);
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		initComponents();
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		setResizable(false);
		setVisible(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setCursor(new Cursor(0));
		setUndecorated(true);
		setAlwaysOnTop(true);
		Toolkit tool = getToolkit();
		Dimension dim = tool.getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		int taskBarHeight = screenInsets.bottom;
		setBounds(dim.width - 320 - 1, dim.height - taskBarHeight - 100 - 1, 320, 100);
		Image image = tool.getImage(ResourceUtils.getResource("/image/server.png"));
		setIconImage(image);
		container.add(initButton(), BorderLayout.CENTER);
		setCursor(Cursor.getPredefinedCursor(12));
		setContentPane(container);
		validate();
	}

	private void initComponents() {
		openItem.addActionListener(this);
		restartItem.addActionListener(this);
		exitItem.addActionListener(this);
		openItem.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		restartItem.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		exitItem.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		pop.add(openItem);
		pop.addSeparator();
		pop.add(restartItem);
		pop.addSeparator();
		pop.add(exitItem);
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			Image icon = getToolkit().getImage(ResourceUtils.getResource("/image/server.png"));
			trayicon = new TrayIcon(icon, TITLE, pop);
			trayicon.addMouseListener(this);
			try {
				tray.add(trayicon);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "系统不支持托盘！", "系统提示", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "系统不支持托盘！", "系统提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	private Box initButton() {
		open.addActionListener(this);
		start.addActionListener(this);
		restart.addActionListener(this);
		min.addActionListener(this);
		start.setCursor(Cursor.getPredefinedCursor(12));
		restart.setCursor(Cursor.getPredefinedCursor(12));
		min.setCursor(Cursor.getPredefinedCursor(12));
		Box box = Box.createHorizontalBox();
		box.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.red), "服务器管理", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 10), Color.BLACK));
		box.add(Box.createHorizontalStrut(6));
		box.add(open);
		box.add(Box.createHorizontalStrut(15));
		box.add(start);
		box.add(Box.createHorizontalStrut(15));
		box.add(restart);
		box.add(Box.createHorizontalStrut(15));
		box.add(min);
		box.validate();
		return box;
	}

	public static void showJettyServer(final boolean start) {
		try {
			if (!JettyServerManager.JETTY_SERVER_MANAGER.isVisible()) {
				JettyServerManager.JETTY_SERVER_MANAGER.setVisible(true);
			}
			if (start) {
				if (!JettyStarterUtils.isStarted()) {
					JettyStarterUtils.startJetty(new ServerStartedCallback() {
						@Override
						public void call(Server server) throws Exception {
							JettyServerManager.JETTY_SERVER_MANAGER.disnabledStartButton();
							JettyServerManager.JETTY_SERVER_MANAGER.hideJettyServer();
							String url = "http://localhost:" + ProfileHolder.getPort() + "/";
							DesktopUtil.browseAndWarn(url, JettyServerManager.JETTY_SERVER_MANAGER);
						}
					}, new ServerStopedCallback() {
						@Override
						public void call() {
							JettyServerManager.JETTY_SERVER_MANAGER.enabledStartButton();
						}
					});
				} else {
					JettyStarterUtils.stopJetty();
					JettyStarterUtils.startJetty(new ServerStartedCallback() {
						@Override
						public void call(Server server) throws Exception {
							JettyServerManager.JETTY_SERVER_MANAGER.disnabledStartButton();
							JettyServerManager.JETTY_SERVER_MANAGER.hideJettyServer();
							String url = "http://localhost:" + ProfileHolder.getPort() + "/";
							DesktopUtil.browseAndWarn(url, JettyServerManager.JETTY_SERVER_MANAGER);
						}
					}, new ServerStopedCallback() {
						@Override
						public void call() {
							JettyServerManager.JETTY_SERVER_MANAGER.enabledStartButton();
						}
					});
				}
			}
		} catch (Exception e) {
			log.warn("Jetty ServerManager Init Error.", e);
			JOptionPane.showMessageDialog(JettyServerManager.JETTY_SERVER_MANAGER, "系统初始化失败！", "系统提示", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void disnabledStartButton() {
		start.setEnabled(false);
	}

	private void enabledStartButton() {
		start.setEnabled(true);
	}

	private void hideJettyServer() {
		if (JettyServerManager.JETTY_SERVER_MANAGER.isVisible()) {
			JettyServerManager.JETTY_SERVER_MANAGER.setVisible(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventObject = e.getSource();
		if (eventObject == open) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String url = "http://localhost:" + ProfileHolder.getPort() + "/";
					DesktopUtil.browseAndWarn(url, JettyServerManager.JETTY_SERVER_MANAGER);
				}
			}).start();
		} else if (eventObject == start) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					JettyServerManager.showJettyServer(true);
				}
			}).start();
		} else if (eventObject == restart) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					JettyStarterUtils.stopJetty();
					JettyServerManager.showJettyServer(true);
				}
			}).start();
		} else if (eventObject == min) {
			hideJettyServer();
		} else if (eventObject == openItem) {
			JettyServerManager.showJettyServer(false);
		} else if (eventObject == restartItem) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					JettyStarterUtils.stopJetty();
					JettyServerManager.showJettyServer(true);
				}
			}).start();
		} else if (eventObject == exitItem) {
			int n = JOptionPane.showConfirmDialog(null, "你确定要退出系统吗？", "系统提示", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(ResourceUtils.getResource("/image/help.gif")));
			if (n == JOptionPane.YES_OPTION) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						log.warn("正在关闭服务器...");
						JettyStarterUtils.stopJetty();
						log.warn("服务器已经正常关闭...");
					}
				}).start();
				System.exit(0);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JettyServerManager.showJettyServer(false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JettyServerManager.showJettyServer(false);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JettyServerManager.showJettyServer(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JettyServerManager.showJettyServer(false);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JettyServerManager.showJettyServer(false);
		}
	}
}