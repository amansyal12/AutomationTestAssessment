package com.benchprep.sampletests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.java.pageobjects.AccountSettingsPage;
import com.java.pageobjects.DashboardPage;
import com.java.pageobjects.DashboardPage.MenuLinks;
import com.java.pageobjects.DashboardPage.SidebarLinks;
import com.java.pageobjects.HomePage;
import com.java.pageobjects.KataPage;
import com.java.pageobjects.LoginPage;

public class CodeWarsTests {

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
				"Login failed, expected user didn't login. ");
	}

	/**
	 * Testing the Search functionality.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearch() throws Exception {

		/*
		 * landing on codewars homepage, clicking on login link and landing on login
		 * page
		 */
		HomePage homePage = HomePage.getPage(driver);
		LoginPage loginPage = homePage.clickLoginLink();
		DashboardPage dashboardPage = loginPage.login();

		// landing on the search page
		KataPage kataPage = dashboardPage.clickSidebarMenuLink(SidebarLinks.Kata, KataPage.class);

		int numOfResultsBefore = kataPage.getNumberOfResults();

		// entering random 2 alphabets and searching
		kataPage.refresh();
		kataPage.selectLanguage("All");
		kataPage.search(RandomStringUtils.randomAlphabetic(2));
		assertTrue(kataPage.areResultsDisplayed(), "Results are not displayed. ");

		int numOfResultsAfter = kataPage.getNumberOfResults();
		assertTrue(numOfResultsAfter != numOfResultsBefore,
				"Search didn't happen properly, number of results is the same. ");

	}

	/**
	 * Testing the functionality to enroll in a course and verifying it.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEnrollInCourse() throws Exception {

		/*
		 * landing on codewars homepage, clicking on login link and landing on login
		 * page
		 */
		HomePage homePage = HomePage.getPage(driver);
		LoginPage loginPage = homePage.clickLoginLink();
		DashboardPage dashboardPage = loginPage.login();

		// landing on the search page
		KataPage kataPage = dashboardPage.clickSidebarMenuLink(SidebarLinks.Kata, KataPage.class);

		// entering random 2 alphabets and searching
		kataPage.refresh();
		kataPage.selectLanguage("All");
		kataPage.search(RandomStringUtils.randomAlphabetic(2));
		kataPage.scrollPageDown();
		kataPage.scrollPageUp();

		// getting a list of all courses on the page and selecting a random course to
		// enroll
		List<String> courseNamesList = kataPage.getCourseNames();
		Random randomizer = new Random();
		String randomCourse = courseNamesList.get(randomizer.nextInt(courseNamesList.size()));
		kataPage.enrollIntoCourse(randomCourse);

		dashboardPage = kataPage.clickSidebarMenuLink(SidebarLinks.Dashboard, DashboardPage.class);
		List<String> enrolledCourses = dashboardPage.getEnrolledCourses();

		boolean courseFound = false;
		for (String enrolledCourse : enrolledCourses) {
			if (StringUtils.contains(enrolledCourse, randomCourse)) {
				courseFound = true;
				break;
			}
		}
		assertTrue(courseFound, "User was not enrolled in " + randomCourse);
	}

	/**
	 * Negative test where we check if login error message shows up, when trying to
	 * login with invalid values.
	 */
	@Test
	public void testLoginError() throws Exception {

		/*
		 * landing on codewars homepage, clicking on login link and landing on login
		 * page
		 */
		HomePage homePage = HomePage.getPage(driver);
		LoginPage loginPage = homePage.clickLoginLink();

		// entering just email, clicking Sign In and verifying error message shows up
		loginPage.setEmail(loginPage.loginCredentials.get("Email"));
		loginPage.clickSignInButtonExpectError();
		assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed, when password field is empty. ");

		// entering just password, clicking Sign In and verifying error message shows up
		loginPage.refresh();
		loginPage.setPassword(loginPage.loginCredentials.get("Password"));
		loginPage.clickSignInButtonExpectError();
		assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed, when email field is empty. ");

		/*
		 * keeping both fields empty, clicking Sign In and verifying error message shows
		 * up
		 */
		loginPage.refresh();
		loginPage.clickSignInButtonExpectError();
		assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed, when both fields are empty. ");

		/*
		 * Entering correct email but incorrect password, clicking Sign In and verifying
		 * error message shows up
		 */
		loginPage.refresh();
		loginPage.setEmail(loginPage.loginCredentials.get("Email"));
		loginPage.setPassword(loginPage.loginCredentials.get("Password") + RandomStringUtils.randomAlphabetic(2));
		loginPage.clickSignInButtonExpectError();
		assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not displayed, when both fields are empty. ");

	}

	/**
	 * Project contains chrome drivers for both windows and mac. Based on the system
	 * that the test is running on, it runs the correct chromedriver.
	 */
	@BeforeMethod
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
	@AfterMethod
	public void closeDriver() {
		driver.quit();
	}
}
