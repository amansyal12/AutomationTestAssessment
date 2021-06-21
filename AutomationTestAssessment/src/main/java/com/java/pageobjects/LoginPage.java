package com.java.pageobjects;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.java.abstractclass.AbstractPortalPage;

public class LoginPage extends AbstractPortalPage {

	private final String EMAIL_INPUT_X = "//input[contains(@id, 'user_email')]";
	private final String PASSWORD_INPUT_X = "//input[contains(@id, 'user_password')]";
	private final String SIGN_IN_BUTTON_X = "//button[. = 'Sign in']";
	private final String ERROR_MSG_X = "//div[contains(@class, 'error')]";

	@FindBy(xpath = EMAIL_INPUT_X)
	private WebElement emailInput;

	@FindBy(xpath = PASSWORD_INPUT_X)
	private WebElement passwordInput;

	@FindBy(xpath = SIGN_IN_BUTTON_X)
	private WebElement signInButton;

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public final static Map<String, String> loginCredentials;
	static {
		loginCredentials = new HashMap<>();
		loginCredentials.put("Email", "benchprep@sdet.com");
		loginCredentials.put("Password", "c0d3Ch@llenge21");
	}

	@Override
	protected void waitForPageLoadComplete() {
		waitForXPathVisibility("Email Input field", EMAIL_INPUT_X);
		waitForXPathVisibility("Password Input field", PASSWORD_INPUT_X);
		waitForXPathVisibility("Sign in button", SIGN_IN_BUTTON_X);
	}

	/**
	 * Enters value onto email input field.
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		enterTextInInputField(emailInput, email);
	}

	/**
	 * Enters value onto password input field.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		enterTextInInputField(passwordInput, password);
	}

	/**
	 * Clicks Sign in button and returns Dashboard page as user lands on it after
	 * successful login.
	 * 
	 * @return DashboardPage
	 */
	public DashboardPage clickSignInButton() throws Exception {
		return click("Sign in button", signInButton, DashboardPage.class);
	}

	/**
	 * Verifies if Sign in button is visible.
	 * 
	 * @return boolean
	 */
	public boolean isSignInButtonDisplayed() {
		return isElementDisplayed(signInButton);
	}

	/**
	 * Verifies if Email input is visible.
	 * 
	 * @return boolean
	 */
	public boolean isEmailInputDisplayed() {
		return isElementDisplayed(emailInput);
	}

	/**
	 * Verifies if error message is displayed.
	 * 
	 * @return boolean
	 */
	public boolean isErrorMessageDisplayed() {
		return isXPathDisplayed(ERROR_MSG_X);
	}

	/**
	 * Clicks Sign in button and expects error
	 * 
	 */
	public void clickSignInButtonExpectError() throws Exception {
		click("Sign in button", signInButton);
		waitForXPathVisibility("Error message ", ERROR_MSG_X);
	}

	/**
	 * Logins in with the sample login credentials
	 * @return DashboardPage
	 * 
	 * @throws Exception
	 */
	public DashboardPage login() throws Exception {
		refresh();
		setEmail(loginCredentials.get("Email"));
		setPassword(loginCredentials.get("Password"));
		return clickSignInButton();
	}

}
