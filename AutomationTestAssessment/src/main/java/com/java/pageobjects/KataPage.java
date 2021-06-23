package com.java.pageobjects;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.beust.jcommander.internal.Lists;

public class KataPage extends DashboardPage {

	private final String SEARCH_INPUT_X = "//input[contains(@id, 'search-input')]";
	private final String SEARCH_BUTTON_X = "//button[contains(@id, 'search')]";
	private final String LANGUAGE_DROPDOWN_X = "//select[contains(@id, 'language_filter')]";

	private final String SEARCH_RESULTS_X = "//div[contains(@class, 'list-item-kata')]";
	private final String NUM_OF_RESULTS_X = "//p[contains(. , 'Kata Found')]";

	private final String BOOKMARK_COURSE_ICON_X = "//div[contains(@class, 'list-item-kata') and .//a[contains(text(), 'Course_Name')]]//a[contains(@class, 'add-code-challenge')]//i";

	@FindBy(xpath = SEARCH_INPUT_X)
	private WebElement searchInput;

	@FindBy(xpath = SEARCH_BUTTON_X)
	private WebElement searchButton;

	@FindBy(xpath = LANGUAGE_DROPDOWN_X)
	private WebElement languageSelect;

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

	/**
	 * Searches for the text passed by entering the value and clicking search button
	 * for the results to show up.
	 * 
	 * @param text
	 */
	public void search(String text) {
		WebElement numResultsEle = driver.findElement(By.xpath(NUM_OF_RESULTS_X));
		click("Number of results element", numResultsEle);
		String numOfResults = getText(numResultsEle);
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

	/**
	 * Selects the language dropdown value passed as parameter.
	 * 
	 * @param value
	 */
	public void selectLanguage(String value) {
		selectDropDownOption("Language dropdown", languageSelect, value);
	}

	/**
	 * Returns list of course names from the search results.
	 * 
	 * @return List<String>
	 */
	public List<String> getCourseNames() {

		String xpath = SEARCH_RESULTS_X + "//div[contains(@class, 'item-title')]//a[contains(@href, 'kata')]";
		refresh();
		waitForXPathVisibility("Number of results", NUM_OF_RESULTS_X);
		List<WebElement> courseNamesEles = driver.findElements(By.xpath(xpath));
		scrollPageDown();
		scrollPageUp();

		return Arrays.asList(getText(courseNamesEles));
	}

	/**
	 * Enrolls into the course name passed as parameter.
	 * 
	 * @param courseName
	 */
	public void enrollIntoCourse(String courseName) {
		String enrollIconXpath = StringUtils.replace(BOOKMARK_COURSE_ICON_X, "Course_Name", courseName);
		WebElement ele = driver.findElement(By.xpath(enrollIconXpath));
		hover("Enroll into " + courseName + " course icon", ele);
		click(courseName + " enroll icon", ele);
		scrollPageUp();
	}
}
