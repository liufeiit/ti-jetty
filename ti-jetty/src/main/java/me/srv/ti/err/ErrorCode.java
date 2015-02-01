package me.srv.ti.err;

/**
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年1月30日 下午11:49:17
 */
public enum ErrorCode {

	Srv_Bean_Resolver_Error("[S.B.R.E-0001].", "Srv服务器Bean服务解析异常.")
	
	;

	public final String code;
	public final String message;

	private ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}