package me.jetty.ti.auth;

import me.jetty.ti.auth.request.CheckRequest;
import me.jetty.ti.auth.request.LoginRequest;
import me.jetty.ti.auth.request.RequestAlias;
import me.jetty.ti.auth.response.CheckResponse;
import me.jetty.ti.auth.response.LoginResponse;
import me.jetty.ti.auth.response.ResponseAlias;
import me.jetty.ti.utils.XmlUtils;

/**
 * @author 刘飞
 * 
 * @version 1.0.0
 * @since 2015年2月5日 上午10:45:47
 */
public class RedisAuthorizationServiceAdapter extends AbstractRedisAuthorizationService implements AuthorizationService {

	@Override
	public String login(String loginRequest) {
		LoginRequest request = XmlUtils.toObj(LoginRequest.class, loginRequest, RequestAlias.Login);
		if(request == null) {
			return LoginResponse.DEFAULT_RESPONSE_XML;
		}
		return XmlUtils.toXML(login(request), ResponseAlias.Login);
	}

	@Override
	public String check(String checkRequest) {
		CheckRequest request = XmlUtils.toObj(CheckRequest.class, checkRequest, RequestAlias.Check);
		if(request == null) {
			return CheckResponse.DEFAULT_RESPONSE_XML;
		}
		return XmlUtils.toXML(check(request), ResponseAlias.Check);
	}
}