package me.jetty.ti.ns;

/**
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月30日 下午1:03:02
 */
public interface NsRegistry {
	
	NsRegistry DEFAULT_NS_REGISTRY = new XNsRegistry();
	
	String GET_PREFIX = "get";
    String SET_PREFIX = "set";
    String IS_PREFIX = "is";
    
	<T> T newInstance(T beanObj);
}