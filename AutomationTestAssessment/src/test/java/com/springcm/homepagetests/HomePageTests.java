package com.springcm.homepagetests;

import static org.testng.Assert.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.java.pageobjects.AccountSettingsPage;
import com.java.pageobjects.DashboardPage;
import com.java.pageobjects.DashboardPage.MenuLinks;
import com.java.pageobjects.HomePage;
import com.java.pageobjects.LoginPage;

public class HomePageTests {

	WebDriver driver;

	/**
	 * Testing the Login functionality and verifying the user was able to login
	 * successfully.
	 */
	@Test
	public void testLogin() throws Exception {

		// landing on codewars homepage
		HomePage homePage = HomePage.getPage(driver);

		// clicking on login link and landing on login page
		LoginPage loginPage = homePage.clickLoginLink();
		Assert.assertTrue(loginPage.isEmailInputDisplayed(), "Email input is not visible. ");

		// entering email and password and clicking sign in to complete sign in process
		loginPage.setEmail(loginPage.loginCredentials.get("Email"));
		loginPage.setPassword(loginPage.loginCredentials.get("Password"));
		DashboardPage dashboardPage = loginPage.clickSignInButton();

		// going to the Account Settings page to verify correct user logged in
		AccountSettingsPage accountSettingPage = dashboardPage.clickMenuLink(MenuLinks.Account_Settings,
				AccountSettingsPage.class);
		assertEquals(accountSettingPage.getEmailInputFieldText(), loginPage.loginCredentials.get("Email"),
				"Login failed, user didn't login as expected user. ");
	}
	
	

	/**
	 * Project contains chrome drivers for both windows and mac. Based on the system
	 * that the test is running on, it runs the correct chromedriver.
	 */
	@BeforeTest
	public void runCorrectChromeDriver() {
		String os = System.getProperty("os.name");

		String propertyVal = System.getProperty("user.dir");
		if (StringUtils.containsIgnoreCase(os, "mac")) {
			propertyVal = StringUtils.join(propertyVal, "/chromedriver_mac");
		} else {
			propertyVal = StringUtils.join(propertyVal, "\\chromedriver.exe");
		}
		System.setProperty("webdriver.chrome.driver", propertyVal);

		// expanding the chrome window to full screen
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
	}

	/**
	 * After the test has ran, closes chrome driver.
	 */
	@AfterTest
	public void closeDriver() {
		driver.quit();
	}
}
