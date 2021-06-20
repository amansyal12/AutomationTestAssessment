package com.java.pageobjects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class KataPage extends DashboardPage {

	private final String SEARCH_INPUT_X = "//input[contains(@id, 'search-input')]";
	private final String SEARCH_BUTTON_X = "//button[contains(@id, 'search')]";

	private final String SEARCH_RESULTS_X = "//div[contains(@class, 'list-item-kata')]";
	private final String NUM_OF_RESULTS_X = "//p[contains(. , 'Kata Found')]";

	@FindBy(xpath = SEARCH_INPUT_X)
	private WebElement searchInput;

	@FindBy(xpath = SEARCH_BUTTON_X)
	private WebElement searchButton;

	@Override
	protected void waitForPageLoadComplete() {
		waitForXPathVisibility("Search input", SEARCH_INPUT_X);
		waitForXPathVisibility("Email input field", SEARCH_BUTTON_X);
	}

	public KataPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * Enters text in search input field.
	 * 
	 * @param value
	 */
	public void enterTextInSearchInput(String text) {
		enterTextInInputField(searchInput, text);
	}

	/**
	 * Clicks search button
	 */
	public void clickSearchButton() {
		click("Search button", searchButton);
		waitForXPathVisibility("Search results", SEARCH_RESULTS_X);
	}

	public void search(String text) {
		String numOfResults= getText(waitForXPathVisibility("Number of results section", NUM_OF_RESULTS_X));
		enterTextInSearchInput(text);
		clickSearchButton();
		waitForElementTextToChange(NUM_OF_RESULTS_X, "Number of results", numOfResults);
	}
	/**
	 * Gets the number of results displayed on top of the page.
	 * 
	 * @return number of results
	 */
	public int getNumberOfResults() {
		WebElement numResultsEle = driver.findElement(By.xpath(NUM_OF_RESULTS_X));

		String numResultsStr = StringUtils.replace(getText(numResultsEle), "Kata Found", "").trim();
		return new Integer(numResultsStr).intValue();
	}

	/**
	 * Checks if results are displayed.
	 * 
	 * @return true or false
	 */
	public boolean areResultsDisplayed() {
		return isXPathDisplayed(SEARCH_RESULTS_X);
	}
}
