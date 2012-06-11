package com.tiempodevelopment.sharepoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

public class HttpNtlmEnv {
	private HttpContext context;
	private HttpHost host;
	private DefaultHttpClient client;
	private String user;
	
	public HttpNtlmEnv(String hostName, String user, String password) {
		Credentials userPassword = new NTCredentials(user, password, "", "TIEMPODEV");
		
		client = new DefaultHttpClient();
		client.getCredentialsProvider().setCredentials(AuthScope.ANY, userPassword);
		context = new BasicHttpContext();
		host = new HttpHost(hostName);
		this.user = user;
	}
	
	public Map<String, String> loginUser() {
		LinkedHashMap<String, String> userInfo = new LinkedHashMap<String, String>();
		
		try {
			HttpResponse response = request(new HttpGet("/"));
			if(response.getStatusLine().getStatusCode() == 200) {
				EntityUtils.consume(response.getEntity());
			
				SOAPMessage soapResponse = callWs("_vti_bin/UserGroup.asmx", getUserInfoMessage());
			
				soapResponse.writeTo(System.out);
			
				NamedNodeMap map = soapResponse.getSOAPBody().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getAttributes();
				for(int i = 0; i < map.getLength(); i++) {
					Attr attr = (Attr) map.item(i);
					userInfo.put(attr.getName(), attr.getValue());
				}
			}
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch(Exception e) {
			return null;
		}
		
		return userInfo;
	}
	
	private HttpResponse request(HttpRequest request) throws ClientProtocolException, IOException {
		client.getConnectionManager().closeExpiredConnections();
		return client.execute(host, request, context);
	}
	
	private SOAPMessage callWs(String wsUrl, SOAPMessage call) throws SOAPException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		call.writeTo(baos);
		
		HttpPost post = new HttpPost("/" + wsUrl);
		post.addHeader(new BasicHeader("SOAPAction", "http://schemas.microsoft.com/sharepoint/soap/directory/GetCurrentUserInfo"));
		post.addHeader(new BasicHeader("Host", "sharepoint.tiempodevelopment.com"));
		post.addHeader(new BasicHeader("Content-Type", "text/xml; charset=utf-8"));
		post.setEntity(new StringEntity(baos.toString()));
		
		HttpResponse response = request(post);
		
		baos.reset();
		response.getEntity().writeTo(baos);
		EntityUtils.consume(response.getEntity());
		return MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(baos.toByteArray()));
	}
	
	private SOAPMessage getUserInfoMessage() throws SOAPException {
		QName info = new QName("http://schemas.microsoft.com/sharepoint/soap/directory/", "GetUserInfo");
		SOAPMessage message = MessageFactory.newInstance().createMessage();
		
		message.getSOAPBody().addBodyElement(info)
			.addChildElement("userLoginName")
			.addTextNode(user);
		
		return message;
	}
}
