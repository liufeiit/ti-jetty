package me.jetty.ti.auth;

import me.jetty.ti.auth.request.PrivilegedRequest;
import me.jetty.ti.auth.request.LoginRequest;
import me.jetty.ti.auth.response.PrivilegedResponse;
import me.jetty.ti.auth.response.LoginResponse;
import me.jetty.ti.utils.XmlUtils;

/**
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:45:47
 */
public class RedisAuthorizationServiceAdapter extends AbstractRedisAuthorizationService implements AuthorizationService {

	@Override
	public String doLogin(String loginRequest) {
		LoginRequest request = XmlUtils.toObj(LoginRequest.class, loginRequest, Alias.Login_Request);
		if(request == null) {
			return LoginResponse.DEFAULT_RESPONSE_XML;
		}
		return XmlUtils.toXML(super.doLogin(request), Alias.Login_Response);
	}

	@Override
	public String doPrivileged(String checkRequest) {
		PrivilegedRequest request = XmlUtils.toObj(PrivilegedRequest.class, checkRequest, Alias.Privileged_Request);
		if(request == null) {
			return PrivilegedResponse.DEFAULT_RESPONSE_XML;
		}
		return XmlUtils.toXML(super.doPrivileged(request), Alias.Privileged_Response);
	}
}