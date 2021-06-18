package com.java.abstractclass;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPortalPage {

	protected static final int DEFAULT_VISIBILITY_TIMEOUT = 60;

	// protected Actions actions;
	protected WebDriver driver;

	protected AbstractPortalPage(WebDriver driver) {
		this.driver = driver;
		waitForPageLoadComplete();
	}

	/**
	 * Wait for page to completely load.
	 */
	protected abstract void waitForPageLoadComplete();

	/**
	 * Wrapper method for PageFactory.initElements. Simplifies error message
	 * displayed.
	 * 
	 * @param driver
	 * @param proxy
	 * @return
	 */
	protected static <T> T getPage(WebDriver driver, Class<T> proxy) throws Exception {
		T t = null;

		try {
			t = PageFactory.initElements(driver, proxy);
		} catch (Exception e) {
			StringBuffer b = new StringBuffer();
			b.append("An error occured while navigating to the ");
			b.append(proxy.getSimpleName());
			b.append(" page.\n");
			throw new RuntimeException(b.toString(), e);
		}

		return t;
	}

	/**
	 * Waits for provided xpath to be visible.
	 * 
	 * @param xpathDescription
	 * @param xpath
	 */
	protected WebElement waitForXPathVisibility(String xpathDescription, String xpath) {
		return waitForXPathVisibility(xpathDescription, driver, xpath, DEFAULT_VISIBILITY_TIMEOUT, getClass());
	}

	/**
	 * Waits for provided xpath to be visible.
	 * 
	 * @param xpathDescription
	 * @param driver
	 * @param xpath
	 * @param timeout
	 */
	protected WebElement waitForXPathVisibility(String xpathDescription, WebDriver driver, String xpath, int timeout,
			Class<?> cls) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		WebElement element = null;

		try {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		} catch (TimeoutException e) {
			StringBuffer error = new StringBuffer();
			error.append("EXPECTED element [name: ");
			error.append(getDescription(xpathDescription));
			error.append(", XPATH: ");
			error.append(xpath);
			error.append(" ] was NOT VISIBLE on ");
			error.append(cls.getSimpleName());
			error.append(" after ").append(timeout).append(" seconds. ");
			throw new AssertionError(error.toString(), e);
		}
		return element;
	}

	/**
	 * Waits for provided xpath to be invisible.
	 * 
	 * @param xpathDescription
	 * @param xpath
	 */
	protected void waitForXPathInvisibility(String xpathDescription, String xpath) {
		waitForXPathInvisibility(xpathDescription, driver, xpath, DEFAULT_VISIBILITY_TIMEOUT, getClass());
	}

	/**
	 * Waits for provided xpath to be invisible.
	 * 
	 * @param xpathDescription
	 * @param driver
	 * @param xpath
	 * @param timeout
	 */
	protected void waitForXPathInvisibility(String xpathDescription, WebDriver driver, String xpath, int timeout,
			Class<?> cls) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
		} catch (TimeoutException e) {
			StringBuffer error = new StringBuffer();
			error.append("UNEXPECTED element [name: ");
			error.append(getDescription(xpathDescription));
			error.append(", XPATH: ");
			error.append(xpath);
			error.append(" ] was STILL VISIBLE on ");
			error.append(cls.getSimpleName());
			error.append(" after ").append(timeout).append(" seconds. ");
			throw new AssertionError(error.toString(), e);
		}
	}

	/**
	 * Waits for the text passed as parameter to be displayed on the page.
	 * 
	 * @param text
	 */
	public void waitForDisplayTextVisibility(String text) {
		waitForXPathVisibility("Text: " + text, String.format("//*[contains(text(), '%s')]", text));
	}

	protected static String getDescription(String value) {
		String description = "<unspecified>";
		if (value != null && !value.isEmpty()) {
			description = value;
		}
		return description;
	}

	protected static void click(String elementDescription, WebDriver driver, WebElement element, Class<?> cls) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_VISIBILITY_TIMEOUT);
		try {
			//
			wait.until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(WebDriver arg0) {
					boolean result;
					try {
						// expectation for checking an element is visible and
						// enabled such that we can
						// click it.
						WebElement e = ExpectedConditions.elementToBeClickable(element).apply(driver);
						if (e != null) {
							Actions action = new Actions(driver);
							action.click(element).perform();
							result = true;
						} else {
							result = false;
						}
					} catch (StaleElementReferenceException e) {
						result = false;
					}
					return result;
				}
			});
		} catch (TimeoutException e) {
			StringBuffer error = new StringBuffer("Element [name: ");
			error.append(getDescription(elementDescription)).append("] was not clickable on ");
			error.append(cls.getSimpleName()).append(" after ");
			error.append(DEFAULT_VISIBILITY_TIMEOUT).append(" seconds.");
			throw new AssertionError(error.toString(), e);
		}
	}

	/**
	 * A click implementation that will return an instance of a class.
	 * 
	 * @param buttonDescription
	 * @param driver
	 * @param button
	 * @param proxy
	 * @return
	 * @throws Exception
	 */
	protected <T> T click(String buttonDescription, WebElement button, Class<T> proxy) throws Exception {
		click(buttonDescription, button);
		return getPage(driver, proxy);
	}

	/**
	 * Click on an element.
	 * 
	 * @param elementDescription
	 * @param element
	 */
	protected void click(String elementDescription, WebElement element) {
		click(elementDescription, driver, element, getClass());
	}

	/**
	 * Enter text in an input field.
	 * <ul>
	 * <li>If newValue is null no action is taken..
	 * <li>If newValue is empty the input is cleared.
	 * <li>if newValue is equal to currentValue no action is taken.
	 * <li>If newValue is not equal to currentValue the input value is cleared and
	 * replaced.
	 * 
	 * @param input
	 * @param newValue
	 */

	protected void enterTextInInputField(WebElement input, final String newValue) {
		String currentValue;

		if (newValue != null) {
			// getting current value in input field
			currentValue = getInputFieldText(input);
			/*
			 * if current value is different from the value passed in argument then enter it
			 * in the input field
			 */
			if (!StringUtils.equals(newValue, currentValue)) {
				click("An input field.", input);

				// if input input field is not empty, then clear it
				if (!StringUtils.isEmpty(currentValue)) {
					input.clear();
				}

				input.sendKeys(newValue);
			}
		}
	}

	/**
	 * @param e
	 * @return the value from input field 'e'
	 */
	protected String getInputFieldText(WebElement e) {
		return e.getAttribute("value").replaceAll("\r", "");
	}

	/**
	 * Gets text for the element passed.
	 * 
	 * @param e the web element
	 * @return the text
	 */
	protected static String getText(WebElement element) {

		String resultsText = element.getText();
		if (resultsText.isEmpty()) {
			// Get the value attribute if one exists or empty if it doesn't.
			resultsText = StringUtils.trimToEmpty(element.getAttribute("value"));
		}
		// to avoid getting non-breaking space in element text
		resultsText = resultsText.replace("&nbsp;", " ");
		return resultsText.trim();
	}

	/**
	 * Gets a string array of the text from a list of web elements.
	 * 
	 * @param elements
	 * @return
	 */
	protected static String[] getText(List<WebElement> elements) {
		String[] elementsText = new String[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			elementsText[i] = getText(elements.get(i));
		}
		return elementsText;
	}

	/**
	 * Verifies if an element is displayed. If an element can not be found in the
	 * DOM this method will return "Not Displayed".
	 * 
	 * @param element
	 * @return
	 */
	protected boolean isElementDisplayed(WebElement element) {

		boolean isDisplayed = false;
		try {
			isDisplayed = element.isDisplayed();
		} catch (NoSuchElementException e) {
			;// No Worries
		}
		return isDisplayed;
	}

	/**
	 * Scrolls the page down.
	 */
	public void scrollPageDown() {
		Long max = getMax();
		JavascriptExecutor jsx = (JavascriptExecutor) driver;
		jsx.executeScript(String.format("window.scrollBy(0, %s)", String.valueOf((long) max)), "");
	}

	/**
	 * Scrolls the page up.
	 */
	public void scrollPageUp() {
		Long max = getMax();
		JavascriptExecutor jsx = (JavascriptExecutor) driver;
		jsx.executeScript(String.format("window.scrollBy(0, -%s)", String.valueOf((long) max)), "");
	}

	/**
	 * returns max page size for scrolling web page.
	 * 
	 * @return long
	 */
	public Long getMax() {
		JavascriptExecutor jsx = (JavascriptExecutor) driver;
		Long winH = (Long) jsx.executeScript("return window.innerHeight;");
		Long docOff = (Long) jsx.executeScript("return document.body.offsetHeight;");
		Long scrollOff = (Long) jsx.executeScript("return document.body.scrollHeight;");
		Long cliH = (Long) jsx.executeScript("return document.documentElement.clientHeight;");
		Long scrollH = (Long) jsx.executeScript("return document.documentElement.scrollHeight;");
		Long offH = (Long) jsx.executeScript("return document.documentElement.offsetHeight;");
		Long[] longs = new Long[] { winH, docOff, scrollOff, cliH, scrollH, offH };
		Long L = (long) 0;
		for (int i = 0; i < longs.length; i++) {
			Long l = java.lang.Math.max(longs[i], L);
			L = l;
		}
		return L;
	}

	/**
	 * Verifies if an element is displayed. If an element can not be found in the
	 * DOM this method will return false.
	 * 
	 * @param element
	 * @return
	 */
	protected boolean isXPathDisplayed(final String xpath) {
		return !driver.findElements(By.xpath(xpath)).isEmpty();
	}

	/**
	 * Waits for the number of driver windows to equal the selected.
	 * 
	 * @param driver
	 * @param numberOfWindows
	 */
	protected static void waitForNumberOfWindowsToEqual(WebDriver driver, final int numberOfWindows) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_VISIBILITY_TIMEOUT);
		try {
			wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
		} catch (TimeoutException e) {
			StringBuffer error = new StringBuffer("The expected minimum number of browser windows was not met after ");
			error.append(DEFAULT_VISIBILITY_TIMEOUT);
			error.append(" seconds. Expected: <").append(numberOfWindows);
			error.append(">, Actual: <").append(driver.getWindowHandles().size()).append('>');
			throw new AssertionError(error.toString(), e);
		}
	}

	/**
	 * Waits for the provided element is visible.
	 * 
	 * @param elementDescription
	 * @param element
	 */
	protected void waitForElementVisibility(String elementDescription, WebElement element) {
		waitForElementVisibility(elementDescription, driver, element, getClass());
	}

	/**
	 * Waits for the provided element is visible.
	 * 
	 * @param elementDescription
	 * @param driver
	 * @param element
	 * 
	 */
	public static void waitForElementVisibility(String elementDescription, WebDriver driver, WebElement element,
			Class<?> cls) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_VISIBILITY_TIMEOUT);

		try {
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException e) {
			StringBuffer error = new StringBuffer();
			error.append("An EXPECTED element [name:").append(getDescription(elementDescription));
			error.append("] was NOT VISIBLE on ").append(cls.getSimpleName());
			error.append(" after ").append(DEFAULT_VISIBILITY_TIMEOUT).append(" seconds.");
			throw new AssertionError(error.toString(), e);
		}
	}

	/**
	 * Hovers over the element passed in parameters.
	 * 
	 * @param elementDescription
	 * @param element
	 */
	protected void hover(String elementDescription, WebElement element) {
		Actions action = new Actions(driver);

		waitForElementVisibility(elementDescription, element);
		action.moveToElement(element).build().perform();
	}
}
