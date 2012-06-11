package com.tiempodevelopment.kanban.test.sharepoint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.tiempodevelopment.sharepoint.NTLMLogin;

public class NtlmLoginTest {
	NTLMLogin login = new NTLMLogin();
	
	@Test
	public void testLogin() {
		assertThat(login, notNullValue());
		
		Map<String, String> result = login.login("sharepoint.tiempodevelopment.com",
				"jorge palacio", "Abc.1111");

		assertThat(result, notNullValue());
		assertThat(result.get("Name"), is("Jorge Luis Palacio Pineda"));
	}
	
	@Test
	public void testInvalidLogin() {
		assertThat(login, notNullValue());
		
		Map<String, String> result = login.login("sharepoint.tiempodevelopment.com",
				"jorge palacio", "Abc.11111");

		assertThat(result.size(), is(0));
	}
}
