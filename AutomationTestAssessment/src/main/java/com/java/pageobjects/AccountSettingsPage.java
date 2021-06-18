package com.java.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountSettingsPage extends DashboardPage {

	private final String USER_INFORMATION_FORM_X = "//form[contains(@id, 'edit_user')]";
	private final String EMAIL_INPUT_FIELD_X = USER_INFORMATION_FORM_X + "//input[contains(@id, 'user_email')]";

	@FindBy(xpath = EMAIL_INPUT_FIELD_X)
	private WebElement emailInput;

	@Override
	protected void waitForPageLoadComplete() {
		waitForXPathVisibility("User Information section", USER_INFORMATION_FORM_X);
		waitForXPathVisibility("Email input field", EMAIL_INPUT_FIELD_X);
	}

	public AccountSettingsPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * Returns input field value.
	 * 
	 * @return input field text
	 */
	public String getEmailInputFieldText() {
		return getInputFieldText(emailInput);
	}

}
