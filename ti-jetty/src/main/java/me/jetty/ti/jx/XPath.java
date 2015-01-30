package me.jetty.ti.jx;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 指定JXPath对应的属性值取值路径.
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月30日 上午11:59:33
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface XPath {
	String value();
}