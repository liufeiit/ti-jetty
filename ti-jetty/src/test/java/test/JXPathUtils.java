package test;

import org.apache.commons.jxpath.Container;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.xml.DocumentContainer;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月28日 下午8:01:23
 */
public class JXPathUtils {
	
	public int port;

	public static void main(String[] args) throws Exception {
		Container container = new DocumentContainer(JXPathUtils.class.getResource("/profile.xml"));
		JXPathContext context = JXPathContext.newContext(container);
		System.out.println(context.getValue("/server/port"));
	}
}