package com.java.pageobjects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.java.abstractclass.AbstractPortalPage;

public class DashboardPage extends AbstractPortalPage {

	private final String HEADER_PROFILE_PIC_X = "//a[contains(@id, 'header_profile_link')]//img";
	private final String LEFT_NAVIGATION_BAR_X = "//nav[contains(@id, 'sidenav')]";

	private final String MENU_LINK_X = "//div[contains(@class, 'menu-body')]//li//a[contains(. , 'Menu_Link_Name')]";

	public enum MenuLinks {

		View_Profile("View Profile"), Account_Settings("Account Settings"), Training_Setup("Training Setup"),
		Upgrade_to_Red("Upgrade to Red"), Sign_out("Sign out");

		String value;

		MenuLinks(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@FindBy(xpath = HEADER_PROFILE_PIC_X)
	private WebElement profilePicture;

	public DashboardPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected void waitForPageLoadComplete() {
		waitForXPathVisibility("Header profile pic", HEADER_PROFILE_PIC_X);
		waitForXPathVisibility("Navigation bar on left side", LEFT_NAVIGATION_BAR_X);
	}

	/**
	 * Clicking on the menu link passed and lands on the expected page.
	 * 
	 * @param menuLink
	 * @param proxy
	 * @throws Exception
	 */
	public <T> T clickMenuLink(MenuLinks menuLink, Class<T> proxy) throws Exception {
		hover("Header profile picture", profilePicture);

		String menuLinkXpath = StringUtils.replace(MENU_LINK_X, "Menu_Link_Name", menuLink.getValue());
		WebElement menuLinkEle = waitForXPathVisibility("Menu link " + menuLink.getValue(), menuLinkXpath);

		return click("Menu link " + menuLink.getValue(), menuLinkEle, proxy);
	}

}
