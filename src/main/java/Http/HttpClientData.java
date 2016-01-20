package Http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientData {
	private static DefaultHttpClient client;
	private static HttpClientData httpClientData;
	private static String time ;
	private static int nonstr ;
	
	public HttpClientData(){
		// 创建HttpClient实例
		if (client == null) {
			// Create HttpClient Object
			client = new DefaultHttpClient();
			enableSSL(client);
		}
		time =   HttpClientGetTime.sendXMLDataByGet(
				"https://hlms.yeeuu.com/api/timestamp", null);
		nonstr = (new Random()).nextInt(999999);
		if (nonstr < 100000) {
			nonstr += 100000;
		}
	}
	
	 public synchronized static HttpClientData getInstance() {  
	       if (httpClientData == null) {  
	    	   httpClientData = new HttpClientData();  
	       }  
	       return httpClientData;  
	 }  
	/**
	 * 访问https的网站
	 * 
	 * @param httpclient
	 */
	private static void enableSSL(DefaultHttpClient httpclient) {
		// 调用ssl
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry()
					.register(https);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写验证方法，取消检测ssl
	 */
	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	public static String login( String hotel_id, String secret,String phone) {
		String sign = DigestUtils.shaHex(time + secret + hotel_id + nonstr+phone);
		String xml = "timestamp=" + time + "&nonstr=" + nonstr + "&hotel_id="
				+ hotel_id + "&sign=" + sign+ "&phone="+phone;

		System.out.println("https://hlms.yeeuu.com/weixin/"+"?"+ xml);
		return "https://hlms.yeeuu.com/weixin/"+"?"+ xml;
		
	}

	public static String updateRoom(String room, String hotel_id, String secret,String phone,String start,String end) throws UnsupportedEncodingException {
		String sign = DigestUtils.shaHex(time + secret + hotel_id + nonstr);
		String xml = "timestamp=" + time + "&nonstr=" + nonstr + "&hotel_id="
				+ hotel_id + "&sign=" + sign;
		List<NameValuePair>  pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("phone",phone));
		pairs.add(new BasicNameValuePair("start",start));
		pairs.add(new BasicNameValuePair("end",end));
		pairs.add(new BasicNameValuePair("type","0"));
		return sendXMLDataByPost("https://lms.yeeuu.com/api/auth/" + room, xml,pairs);
	}
	
	public static String deleteRoom(String room, String hotel_id, String secret,String phone) {
		String sign = DigestUtils.shaHex(time + secret + hotel_id + nonstr);
		String xml = "timestamp=" + time + "&nonstr=" + nonstr + "&hotel_id="
				+ hotel_id + "&sign=" + sign + "&phone="+phone;

		return sendXMLDataByDelete("https://lms.yeeuu.com/api/auth/" + room, xml);
	}

	/**
	 * HTTP Client Object,used HttpClient Class before(version 3.x),but now the
	 * HttpClient is an interface
	 */
	public static String sendXMLDataByGet(String url, String xml) {
		StringBuilder urlString = new StringBuilder();
		urlString.append(url);
//		if (xml != null) {
//			urlString.append("?");
//			System.out.println("getUTF8XMLString(xml):" + getUTF8XMLString(xml));
//			urlString.append(xml);
//		}
		String urlReq = urlString.toString();
		// 创建Get方法实例
		HttpGet httpsgets = new HttpGet(urlReq);

		String strRep = "";
		try {
			HttpResponse response = client.execute(httpsgets);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				strRep = EntityUtils.toString(response.getEntity());
				System.out.println("strRep:" + strRep);
				// Do not need the rest
				httpsgets.abort();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if (url == "https://hlms.yeeuu.com/api/timestamp") {
			strRep = strRep.substring(strRep.lastIndexOf(" ") + 1);
			strRep = strRep.substring(0, strRep.length() - 1);
//		}
		return strRep;
	}
	
	public static String sendXMLDataByPost(String url, String xml,List<NameValuePair> pairs) throws UnsupportedEncodingException {
		StringBuilder urlString = new StringBuilder();
		urlString.append(url);
		urlString.append("?");
		System.out.println("getUTF8XMLString(xml):" + getUTF8XMLString(xml));
		urlString.append(xml);
		String urlReq = urlString.toString();
		// 创建Post方法实例
		HttpPost httpsposts = new HttpPost(urlReq);
		httpsposts.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		String strRep = "";
		try {
			HttpResponse response = client.execute(httpsposts);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				strRep = EntityUtils.toString(response.getEntity());
				System.out.println("strRep:" + strRep);
				// Do not need the rest
				httpsposts.abort();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		strRep = convert(strRep) +"\"}";
		System.out.println("strRep:" + strRep);
		return strRep;
	}
	
	public static String sendXMLDataByDelete(String url, String xml) {
		// 创建HttpClient实例
//		if (client == null) {
//			// Create HttpClient Object
//			client = new DefaultHttpClient();
//			enableSSL(client);
//		}
		StringBuilder urlString = new StringBuilder();
		urlString.append(url);
		urlString.append("?");
		System.out.println("getUTF8XMLString(xml):" + getUTF8XMLString(xml));
		urlString.append(xml);
		String urlReq = urlString.toString();
		// 创建Delete方法实例
		HttpDelete httpsdeletes = new HttpDelete(urlReq);

		String strRep = "";
		try {
			HttpResponse response = client.execute(httpsdeletes);
			HttpEntity entity = response.getEntity();
			strRep = EntityUtils.toString(response.getEntity());
			System.out.println("strRep:" + strRep);
			// Do not need the rest
			httpsdeletes.abort();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		strRep = convert(strRep) +"\"}";
		System.out.println("strRep:" + strRep);
		return strRep;
	}
	
	public static String convert(String utfString){
	    StringBuilder sb = new StringBuilder();
	    int i = -1;
	    int pos = 0;
	     
	    while((i=utfString.indexOf("\\u", pos)) != -1){
	        sb.append(utfString.substring(pos, i));
	        if(i+5 < utfString.length()){
	            pos = i+6;
	            sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
	        }
	    }
	     
	    return sb.toString();
	}

	/**
	 * Get XML String of utf-8
	 * 
	 * @return XML-Formed string
	 */
	public static String getUTF8XMLString(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return to String Formed
		return xmString.toString();
	}

}
