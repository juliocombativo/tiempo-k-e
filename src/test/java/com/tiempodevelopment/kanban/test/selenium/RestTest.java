package com.tiempodevelopment.kanban.test.selenium;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;

import com.tiempodevelopment.kanban.test.infra.Deployments;

@RunWith(Arquillian.class)
public class RestTest {
	@Deployment(testable=false)
	public static WebArchive war() {
		return Deployments.webApp();
	}
	
	@Drone
	ChromeDriver driver;
	
	@ArquillianResource
    URL deploymentUrl;
	
	@Test
	public void testLogin() {
		driver.navigate().to(url("/api/projects/search/user/1"));
		assertThat(driver.getPageSource().indexOf("404"), is(-1));
	}
	
	private String url(String url) {
		return deploymentUrl.toString().replaceFirst("/$", "") + url;
	}
}
