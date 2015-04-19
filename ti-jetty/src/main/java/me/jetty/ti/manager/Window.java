package me.jetty.ti.manager;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年4月18日 下午3:36:01
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	
	protected static final Logger log = Log.getLogger(Window.class);

	private int x;// 接受鼠标事件
	private int y;

	private boolean isDraging;// 判断鼠标是否正在移动

	public Window() throws HeadlessException {
		super();
		mouseMoveAdapter();
	}

	public Window(GraphicsConfiguration gc) {
		super(gc);
		mouseMoveAdapter();
	}

	public Window(String title, GraphicsConfiguration gc) {
		super(title, gc);
		mouseMoveAdapter();
	}

	public Window(String title) throws HeadlessException {
		super(title);
		mouseMoveAdapter();
	}

	protected void mouseMoveAdapter() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				x = e.getX();
				y = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				isDraging = false;
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - x, top + e.getY() - y);
				}
			}
		});
	}
}