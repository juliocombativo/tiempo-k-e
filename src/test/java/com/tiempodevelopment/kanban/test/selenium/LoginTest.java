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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.tiempodevelopment.kanban.test.infra.Deployments;

@RunWith(Arquillian.class)
public class LoginTest {
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
		login("jorge palacio", "Abc.1111");
			
		assertThat(driver.getCurrentUrl(), is(url("/kanban/")));
		
		WebElement upperBar = driver.findElement(By.id("upper-bar"));
		assertThat(upperBar.isDisplayed(), is(true));
		assertThat(upperBar.findElement(By.className("user-info")).getText(), is("Hi Jorge Luis Palacio Pineda"));
	}
	
	private void login(String userName, String password) {
		driver.navigate().to(url("/kanban/"));
		
		assertThat(driver.getCurrentUrl(), is(url("/login.jsp")));
			
		driver.findElement(By.id("userName")).sendKeys("jorge palacio");
		driver.findElement(By.id("password")).sendKeys("Abc.1111");
			
		driver.findElement(By.id("login")).click();
	}
	
	private String url(String url) {
		return deploymentUrl.toString().replaceFirst("/$", "") + url;
	}
}
