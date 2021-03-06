package com.java.pageobjects;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.java.abstractclass.AbstractPortalPage;

public class DashboardPage extends AbstractPortalPage {

	private final String HEADER_PROFILE_PIC_X = "//a[contains(@id, 'header_profile_link')]//img";
	private final String SIDE_BAR_X = "//nav[contains(@id, 'sidenav')]";
	private final String TOP_HEADER_X = "//header[contains(@id, 'main_header')]";
	private final String BOOKMARK_ICON_X = TOP_HEADER_X + "//li[ .//i[contains(@class , 'bookmark')]]";

	private final String MENU_LINK_X = "//div[contains(@class, 'menu-body')]//li//a[contains(. , 'Menu_Link_Name')]";
	private final String SIDE_BAR_LINK_NAME_X = SIDE_BAR_X + "//li//a[contains(. , 'Sidebar_Link_Name')]";

	public enum MenuLinks {

		View_Profile("View Profile"), 
		Account_Settings("Account Settings"), 
		Training_Setup("Training Setup"),
		Upgrade_to_Red("Upgrade to Red"), 
		Sign_out("Sign out");

		String text;

		MenuLinks(String value) {
			this.text = value;
		}

		public String getValue() {
			return text;
		}
	}

	public enum SidebarLinks {

		Dashboard("Dashboard"), 
		Kata("Kata"), 
		Docs("Docs"), 
		Blog("Blog"), 
		Kumite("Kumite"), 
		Forum("Forum"),
		Leaders("Leaders");

		String text;

		SidebarLinks(String value) {
			this.text = value;
		}

		public String getValue() {
			return text;
		}
	}

	@FindBy(xpath = HEADER_PROFILE_PIC_X)
	private WebElement profilePicture;

	@FindBy(xpath = SIDE_BAR_X)
	private WebElement sideBar;

	@FindBy(xpath = BOOKMARK_ICON_X)
	private WebElement bookmarkIcon;

	public DashboardPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected void waitForPageLoadComplete() {
		waitForXPathVisibility("Header profile pic", HEADER_PROFILE_PIC_X);
		waitForXPathVisibility("Navigation bar on left side", SIDE_BAR_X);
	}

	/**
	 * Clicking on the menu link passed for the menu items on top right corner of the screen after hovering on user's profile picture and lands on the expected page.
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

	/**
	 * Clicking on the side bar link passed after hovering over the side bar and lands on the expected page.
	 * 
	 * @param sideLink
	 * @param proxy
	 * @throws Exception
	 */
	public <T> T clickSidebarMenuLink(SidebarLinks sidebarLink, Class<T> proxy) throws Exception {
		hover("Side navigation bar", sideBar);

		String sidebarLinkXpath = StringUtils.replace(SIDE_BAR_LINK_NAME_X, "Sidebar_Link_Name",
				sidebarLink.getValue());
		WebElement sidebarLinkEle = waitForXPathVisibility("Sidebar link " + sidebarLink.getValue(), sidebarLinkXpath);

		return click("Side bar nav link " + sidebarLink.getValue(), sidebarLinkEle, proxy);
	}

	/**
	 * Returns list of enrolled courses after hovering over the bookmark icon.
	 * 
	 * @return List<String>
	 */
	public List<String> getEnrolledCourses() {
		refresh();
		hover("Bookmark icon on top header", bookmarkIcon);
		waitForXPathVisibility("Enrolled section div", BOOKMARK_ICON_X + "//div[contains(@class, 'menu-body')]");

		List<WebElement> enrolledCoursesEles = driver
				.findElements(By.xpath(BOOKMARK_ICON_X + "//a[contains(@href, 'kata')]"));
		return Arrays.asList(getText(enrolledCoursesEles));
	}
}
