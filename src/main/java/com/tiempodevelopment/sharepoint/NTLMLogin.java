package com.tiempodevelopment.sharepoint;

import java.util.Map;


public class NTLMLogin {
	public Map<String, String> login(String url, String user, String password) {
		HttpNtlmEnv env = new HttpNtlmEnv(url, user, password);
		
		Map<String, String> userInfo = env.loginUser();
		return userInfo;
	}
}
