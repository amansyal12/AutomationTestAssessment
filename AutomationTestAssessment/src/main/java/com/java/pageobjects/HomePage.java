package com.java.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.java.abstractclass.AbstractPortalPage;

public class HomePage extends AbstractPortalPage {

	// xpaths for elements on page
	private final String PAGE_DIV_X = "//div[contains(@id, 'shell')]";
	private final String HEADER_SECTION_X = PAGE_DIV_X + "//section[contains(@id, 'header_section')]";
	private final String LOGIN_LINK_X = HEADER_SECTION_X + "//a[contains(., 'Log In')]";

	private final String SIGN_UP_BUTTON_X = PAGE_DIV_X + "//a[contains(@id, 'sign_up_button')]";

	@FindBy(xpath = LOGIN_LINK_X)
	private WebElement loginLink;

	@FindBy(xpath = SIGN_UP_BUTTON_X)
	private WebElement signUpButton;

	public HomePage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected void waitForPageLoadComplete() {
		waitForXPathVisibility("Headers section", HEADER_SECTION_X);
		waitForXPathVisibility("Login link", LOGIN_LINK_X);
		waitForXPathVisibility("Sign up button", SIGN_UP_BUTTON_X);
	}

	/**
	 * Navigate to the codewars homepage.
	 * 
	 * @param driver
	 * @throws Exception
	 */
	public static HomePage getPage(WebDriver driver) throws Exception {
		driver.get("https://www.codewars.com");
		return AbstractPortalPage.getPage(driver, HomePage.class);
	}

	/**
	 * Clicks on the login link and user lands on the login page.
	 * 
	 * @return LoginPage
	 */
	public LoginPage clickLoginLink() throws Exception {
		return click("Login link", loginLink, LoginPage.class);
	}

}
