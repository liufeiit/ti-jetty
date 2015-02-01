package me.srv.ti.err;

/**
 * 便于跟踪异常错误类型的异常.
 * 
 * @author 刘飞 E-mail:liufei_it@126.com
 * @version 1.0.0
 * @since 2015年1月30日 下午11:46:04
 */
public class SrvException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final ErrorCode code;

	public SrvException(ErrorCode code) {
		super("SrvException" + code.code);
		this.code = code;
	}

	public SrvException(ErrorCode code, String message, Throwable cause) {
		super("SrvException" + code.code + " : " + message, cause);
		this.code = code;
	}

	public SrvException(ErrorCode code, String message) {
		super("SrvException" + code.code + " : " + message);
		this.code = code;
	}
	
	public ErrorCode getErrorCode() {
		return code;
	}
}