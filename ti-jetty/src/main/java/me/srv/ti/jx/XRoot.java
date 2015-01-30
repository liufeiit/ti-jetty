package me.srv.ti.jx;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 指定JXPath对应的XML等资源文件路径.
 * 
 * @author fei.liu E-mail:fei.liu@andpay.me
 * 
 * @version 1.0.0
 * @since 2015年1月30日 下午12:57:20
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface XRoot {
	String value();
}