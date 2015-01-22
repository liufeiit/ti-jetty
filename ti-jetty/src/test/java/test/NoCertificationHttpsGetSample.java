/*package test;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;

public class NoCertificationHttpsGetSample {
	public static void main(String[] args) {
		Protocol myhttps = new Protocol("https",
				new MySecureProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("https://yourhttps.com/");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			byte[] responseBody = getMethod.getResponseBody();
			System.out.println(new String(responseBody));
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

	}
}
*/