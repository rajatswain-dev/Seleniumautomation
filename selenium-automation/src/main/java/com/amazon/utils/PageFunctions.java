package com.amazon.utils;

import com.amazon.driver.DriverFactory;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PageFunctions {

	public static final int WAIT_LOOP_COUNT = 3;
	public static final int WAIT_LOOP_TIME = 1000;
	public static final int FORM_VISIBLE_WAIT_TIME = 2000;

	public static final String WAITING = "Waiting... ";
	public static final String ELEMENT_FOUND = "Element found ";
	public static final String ELEMENT_NOT_FOUND = "Element not found !!";
	public static final String ELEMENT_VISIBLE = "Element visible ";
	public static final String EXCEPTION_OCCURED = "Exception occured ";
	public static final String INVALID_LOCATOR = "Invalid Locator: ";
	public static final String INTERRUPTED_THREAD = "Interrupted thread ";

	private static final Duration DEFAULT_ELEMENT_WAIT = Duration.ofSeconds(60);

	public static WebDriver driver;

	public PageFunctions() {
		driver = DriverFactory.getDriver();
	}

	private WebDriver getDriver() {
		return DriverFactory.getDriver();
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to tap on the screen or web page
	 * @param xpath
	 */

	public void blur(String xpath) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		String script = "function getElementByXpath(path) {\n"
				+ "  return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n"
				+ "}\n" + "el = getElementByXpath(\"" + xpath + "\");\n" + "el.blur()";
		log.debug("Executing JS : \r\n " + script);
		js.executeScript(script);
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to check alert is present or not
	 * @return boolean
	 */

	public boolean isAlertPresent() {
		try {
			getDriver().switchTo().alert();
			log.info("ALERT IS PRESENT !! ");
			return true;
		} catch (Exception e) {
			log.info("ALERT IS NOT PRESENT !! ");
			return false;
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to handle the alert popUp
	 */

	public void webAlertHandle() {
		if (isAlertPresent()) {
			try {
				Alert alert = getDriver().switchTo().alert();
				alert.accept();
			} catch (UnhandledAlertException e) {
				Alert alert = getDriver().switchTo().alert();
				alert.accept();
				log.info("Found unhandled alert exception !!");
			}
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to verify text is present or not
	 * @param textToIdentify
	 * @return boolean false if not present
	 */

	public boolean isTextPresent(String textToIdentify) {
		boolean isPresent = false;
		try {
			if (getDriver().findElement(By.xpath("//*[text()='" + textToIdentify + "']")).isDisplayed())
				isPresent = true;
			else if (getDriver().findElement(
					By.xpath("//*[translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
							+ textToIdentify.toLowerCase() + "']"))
					.isDisplayed())
				isPresent = true;
		} catch (Exception e) {
			log.error("Text is not present !!", e);
		}
		return isPresent;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to verify text contains or not
	 * @param textToIdentify
	 * @return boolean false if not present
	 */

	public boolean isTextContains(String textToIdentify) {
		boolean isPresent = false;
		try {
			if (getDriver().findElement(By.xpath("//*[contains(text(),'" + textToIdentify + "')]")).isDisplayed())
				isPresent = true;
			else if (getDriver().findElement(By
					.xpath("//*[translate(contains(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
							+ textToIdentify.toLowerCase() + "')]"))
					.isDisplayed())
				isPresent = true;
		} catch (Exception e) {
			log.error("Text is not present !!", e);
		}
		return isPresent;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to click element using XPATH
	 * @param xpath
	 */

	public void clickElementByXPath(String xpath) {
		for (int i = 0; i < WAIT_LOOP_COUNT; i++) {
			log.debug("waiting... " + i + "/" + WAIT_LOOP_COUNT);
			if ((getDriver().findElements(By.xpath(xpath)).size() > 0)) {
				log.debug("Element found " + xpath);
				break;
			} else {
				log.debug("not found " + xpath);
			}
			timetoWaitUntil(WAIT_LOOP_COUNT);
		}
		try {
			getElement(xpath).click();
		} catch (Exception e) {
			Actions actions = new Actions(getDriver());
			actions.moveToElement(getElement(xpath)).click().perform();
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to send the text using XPATH
	 * @param xpath
	 * @param text
	 */

	public void setText(String xpath, String text) {
		WebElement el = getElementFromXpath(xpath);
		if (el != null) {
			clear(xpath);
			el.sendKeys(text);
		} else {
			Assert.fail("Element not found for setText: " + xpath);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: This method returns the element after waiting for it
	 * @param xpath
	 * @return WebElement
	 */

	public WebElement getElementFromXpath(String xpath) {
		for (int i = 0; i < WAIT_LOOP_COUNT; i++) {
			log.debug(WAITING + i + "/" + WAIT_LOOP_COUNT);
			if (!(getDriver().findElements(By.xpath(xpath)).isEmpty())) {
				log.debug(ELEMENT_FOUND + xpath);
				if (getDriver().findElements(By.xpath(xpath)).get(0).isDisplayed()) {
					log.debug(ELEMENT_VISIBLE + xpath);
					return getDriver().findElement(By.xpath(xpath));
				}
			} else {
				log.debug(ELEMENT_NOT_FOUND + xpath);
			}
			try {
				Thread.sleep(WAIT_LOOP_TIME);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error(EXCEPTION_OCCURED, e);
				Assert.fail(INTERRUPTED_THREAD);
			}
		}
		return null;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Get the element using ID
	 * @param id
	 * @return WebElement
	 */

	public WebElement getElementFromID(String id) {
		for (int i = 0; i < WAIT_LOOP_COUNT; i++) {
			log.debug(WAITING + i + "/" + WAIT_LOOP_COUNT);
			if (!(getDriver().findElements(By.id(id)).isEmpty())) {
				log.debug(ELEMENT_FOUND + id);
				if (getDriver().findElements(By.id(id)).get(0).isDisplayed()) {
					log.debug(ELEMENT_VISIBLE + id);
					return getDriver().findElement(By.id(id));
				}
			} else {
				log.debug(ELEMENT_NOT_FOUND + id);
			}
			try {
				Thread.sleep(WAIT_LOOP_TIME);
			} catch (InterruptedException e) {
				log.error(EXCEPTION_OCCURED, e);
				Assert.fail(INTERRUPTED_THREAD);
				Thread.currentThread().interrupt();
			}
		}
		return null;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to select the text based on ID
	 * @param id
	 * @param value
	 */

	public void selectByVisibleTextByID(String id, String value) {
		WebElement el = getElementFromID(id);
		Select sel = new Select(el);
		sel.selectByVisibleText(value);
	}

	/**
	 * @Author: Rajat
	 * @Desc: This method will select the dropDown value by the visible text
	 * @param xpath
	 * @param value
	 */

	public void selectByVisibleText(String xpath, String value) {
		WebElement el = getElementFromXpath(xpath);
		Select sel = new Select(el);
		sel.selectByVisibleText(value);
	}

	/**
	 * @Author: Rajat
	 * @Desc: This method will select the dropDown list value by index
	 * @param xpath
	 * @param index
	 */

	public void selectByIndex(String xpath, int index) {
		WebElement el = getElementFromXpath(xpath);
		Select sel = new Select(el);
		sel.selectByIndex(index);
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to select dropdown by value
	 * @param xpath
	 * @param value
	 */

	public void selectByValue(String xpath, String value) {
		WebElement el = getElementFromXpath(xpath);
		Select sel = new Select(el);
		sel.selectByValue(value);
	}

	/**
	 * @Author: Rajat
	 * @Desc: This method will inject javascript to click the element
	 * @param xpath
	 */

	public void jsClickElementByXpath(String xpath) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		String script = "function getElementByXpath(path) {\n"
				+ "  return document.evaluate(path, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n"
				+ "}\n" + "el = getElementByXpath(\"" + xpath + "\");\n" + "el.click();";
		log.debug("Executing JS : \r\n " + script);
		js.executeScript(script);
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to scroll to top of the page
	 */

	public void scrollToTop() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("window.scrollTo(0, 0)");
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to scroll to bottom of the page
	 */

	public void scrollToBottom() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * @Author: Rajat
	 * @Desc: Scroll till the element is visible
	 * @param el
	 */

	public void scrollTillElement(WebElement el) {
		JavascriptExecutor je = (JavascriptExecutor) getDriver();
		je.executeScript("arguments[0].scrollIntoView(true);", el);
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to check the field is dropDown or not
	 * @param xpath
	 * @return boolean true if field is dropDown
	 */

	public boolean checkFieldIsDropdown(String xpath) {
		boolean bFlag = false;
		try {
			if (getOptionsFromDropdown(xpath).size() > 1) {
				bFlag = true;
			}
		} catch (Exception e) {
			log.info("Field is not dropdown !!");
		}
		return bFlag;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Wait until element is visible by WebElement
	 * @param xpath
	 */

	public void waitUntilVisibleByElement(String xpath) {
		WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_ELEMENT_WAIT);
		wait.ignoring(ElementNotInteractableException.class);
		wait.until(ExpectedConditions.visibilityOf(getElement(xpath)));
	}

	/**
	 * @Author: Rajat
	 * @Desc: Wait until element is visible by locator
	 * @param xpath
	 */

	public void waitUntilVisibleByLocator(String xpath) {
		WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_ELEMENT_WAIT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	}

	/**
	 * @Author: Rajat
	 * @Desc: Wait until element is invisible by locator
	 * @param xpath
	 */

	public void waitUntilInVisibleByLocator(String xpath) {
		WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_ELEMENT_WAIT);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to wait for element to be clickable using web element
	 * @param xpath
	 */

	public void waitUntilClickableByElement(String xpath) {
		WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_ELEMENT_WAIT);
		wait.ignoring(ElementNotInteractableException.class);
		wait.until(ExpectedConditions.elementToBeClickable(getElement(xpath)));
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to wait for the element to be clickable using locator
	 * @param xpath
	 */

	public void waitUntilClickableByLocator(String xpath) {
		WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_ELEMENT_WAIT);
		wait.ignoring(ElementNotInteractableException.class);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method is used to check element is displayed or not
	 * @param xpath
	 * @return boolean true/false
	 */

	public boolean isElementPresent(String xpath) {
		try {
			waitUntilVisibleByElement(xpath);
			return getElement(xpath).isDisplayed();
		} catch (NoSuchElementException exception) {
			log.info("ELEMENT IS NOT PRESENT !! " + xpath);
			return false;
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method is used to wait until a specific attribute value changes for
	 *        the given element located by XPath.
	 *
	 * @param xpath     - XPath of the target element
	 * @param attribute - Attribute name to monitor
	 * @param oldValue  - Previous attribute value
	 * @return boolean - true if attribute changes within timeout, else false
	 */

	public boolean waitForAttributeToChange(String xpath, String attribute, String oldValue) {

		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_ELEMENT_WAIT);

			wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(By.xpath(xpath), attribute, oldValue)));

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method is used to check element is enabled or not
	 * @param xpath
	 * @return boolean true/false
	 */

	public boolean isElementEnabled(String xpath) {
		try {
			waitUntilVisibleByElement(xpath);
			return getElement(xpath).isEnabled();
		} catch (Exception e) {
			log.info("ELEMENT IS NOT ENABLED !! " + xpath);
			return false;
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method is used to check element is selected or not
	 * @param xpath
	 * @return boolean true/false
	 */

	public boolean isElementSelected(String xpath) {
		try {
			waitUntilVisibleByElement(xpath);
			return getElement(xpath).isSelected();
		} catch (Exception e) {
			log.info("ELEMENT IS NOT SELECTED !! " + xpath);
			return false;
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to clear the element value
	 * @param locator
	 */

	public void clear(String locator) {
		WebElement el = getElement(locator);
		if (el != null) {
			el.clear();
		} else {
			Assert.fail("Element not found " + locator);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: This method will select the drop down menu
	 * @param locator
	 * @param selectBy
	 */

	public void select(String locator, String selectBy) {
		WebElement el = getElement(locator);
		Select sel = new Select(el);
		String by = "";
		String value = "";
		Pattern p = Pattern.compile("^([A-Za-z]+)=(.+)");
		Matcher m = p.matcher(selectBy);
		if (m.find()) {
			log.debug(m.group(0));
			log.debug(m.group(1));
			log.debug(m.group(2));
			by = m.group(1).toLowerCase();
			value = m.group(2);
		} else {
			Assert.fail("Invalid select By" + selectBy);
		}
		switch (by) {
		case "label":
			sel.selectByVisibleText(value);
			break;
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Generic element finder. Locator should consist of locator=value e.g:
	 *        xpath=//[div] or css=css.ff
	 * @param locator
	 * @return WebElement
	 */

	public WebElement getElement(String locator) {
		String by = null;
		String value = null;
		Pattern p = Pattern.compile("^([A-Za-z]+)=(.+)");
		Matcher m = p.matcher(locator);
		if (m.find()) {
			log.debug(m.group(0));
			log.debug(m.group(1));
			log.debug(m.group(2));
			by = m.group(1).toLowerCase();
			value = m.group(2);
		} else if (locator.startsWith("//") || locator.startsWith("(//")) {
			by = "xpath";
			value = locator;
		} else {
			try {
				throw new Exception("invalid locator " + locator);
			} catch (Exception e) {
				log.error(EXCEPTION_OCCURED, e);
				Assert.fail(INVALID_LOCATOR + locator);
			}
		}
		for (int i = 0; i < WAIT_LOOP_COUNT; i++) {
			switch (by) {
			case "id":
				if (!(getDriver().findElements(By.id(value)).isEmpty())) {
					if (getDriver().findElements(By.id(value)).get(0).isDisplayed()) {
						return getDriver().findElement(By.id(value));
					}
				}
				break;
			case "xpath":
				if (!(getDriver().findElements(By.xpath(value)).isEmpty())) {
					if (getDriver().findElements(By.xpath(value)).get(0).isDisplayed()) {
						return getDriver().findElement(By.xpath(value));
					}
				}
				break;
			case "name":
				if (!(getDriver().findElements(By.name(value)).isEmpty())) {
					if (getDriver().findElements(By.name(value)).get(0).isDisplayed()) {
						return getDriver().findElement(By.name(value));
					}
				}
				break;
			case "css":
				if (!(getDriver().findElements(By.cssSelector(value)).isEmpty())) {
					if (getDriver().findElements(By.cssSelector(value)).get(0).isDisplayed()) {
						return getDriver().findElement(By.cssSelector(value));
					}
				}
				break;
			case "link":
				if (!(getDriver().findElements(By.linkText(value)).isEmpty())) {
					if (getDriver().findElements(By.linkText(value)).get(0).isDisplayed()) {
						return getDriver().findElement(By.linkText(value));
					}
				}
				break;
			}
			try {
				Thread.sleep(WAIT_LOOP_TIME);
			} catch (InterruptedException e) {
				log.error(EXCEPTION_OCCURED, e);
				Assert.fail(INTERRUPTED_THREAD);
				Thread.currentThread().interrupt();
			}
		}
		log.debug("element not found " + locator);
		return null;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method will click element using Selenium explicit wait
	 * @param xpath
	 */

	public void clickElementUsingXPath(String xpath) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
		wait.until(ExpectedConditions
				.refreshed(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpath)))));
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(By.xpath(xpath))));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		timetoWaitUntil(2000);
		try {
			getDriver().findElement(By.xpath(xpath)).click();
			log.info("Element clicked using-->" + xpath);
		} catch (ElementNotInteractableException e1) {
			log.info(ELEMENT_NOT_FOUND, e1);
		} catch (StaleElementReferenceException e3) {
			log.info(ELEMENT_NOT_FOUND, e3);
		} catch (NoSuchElementException e4) {
			log.info(ELEMENT_NOT_FOUND, e4);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method will return element using Selenium explicit wait
	 * @param xpath
	 * @return WebElement
	 */

	public WebElement getElementUsingXPath(String xpath) {
		WebElement ele = null;
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
		wait.until(ExpectedConditions
				.refreshed(ExpectedConditions.visibilityOf(getDriver().findElement(By.xpath(xpath)))));
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(By.xpath(xpath))));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		try {
			ele = getDriver().findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			log.error(ELEMENT_NOT_FOUND + xpath, e);
		}
		return ele;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to find elements by xpath expression
	 * @param xpathExpression
	 */

	public void findElements(String xpathExpression) {
		try {
			getDriver().findElements(By.xpath(xpathExpression));
		} catch (Exception e) {
			log.debug("Not found");
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Get WebElements using xpath without checking isDisplayed()
	 * @param xpath
	 * @return List<WebElement>
	 */

	public List<WebElement> findElementsByXpath(String xpath) {
		List<WebElement> elements = null;
		try {
			elements = getDriver().findElements(By.xpath(xpath));
		} catch (Exception e) {
			log.debug("Elements Not Found");
		}
		return elements;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Get WebElement using xpath without checking isDisplayed()
	 * @param xpath
	 * @return WebElement
	 */
	public WebElement findElementByXpath(String xpath) {
		WebElement ele = null;
		try {
			ele = getDriver().findElement(By.xpath(xpath));
		} catch (Exception e) {
			log.debug("Not Found");
		}
		return ele;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Function will refresh the page
	 */

	public void pageRefresh() {
		getDriver().navigate().refresh();
	}

	/**
	 * @Author: Rajat
	 * @Desc: Sleep wait — should be used for random failures
	 * @param timeInMilliSec
	 */

	public void timetoWaitUntil(long timeInMilliSec) {
		try {
			Thread.sleep(timeInMilliSec);
			log.info("Waiting Time provided - " + (timeInMilliSec / 1000) + " - Sec");
		} catch (InterruptedException e) {
			log.error("Timeout Exception Found !!", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to wait until page is getting loaded
	 * @param minutes
	 * @return boolean false if page not loaded
	 */

	public boolean waitUntilPageIsGettingLoaded(int minutes) {
		try {
			return getDriver().manage().timeouts().pageLoadTimeout(Duration.ofMinutes(minutes)) != null;
		} catch (Exception e) {
			log.info("Page is not loaded !!");
			return false;
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: It will return list of webElements by xpath
	 * @param xpath
	 * @return List<WebElement>
	 */

	public List<WebElement> getElementsFromXpath(String xpath) {
		try {
			List<WebElement> elements = getDriver().findElements(By.xpath(xpath));
			log.debug("Elements found By " + xpath);
			return elements;
		} catch (NoSuchElementException e) {
			log.error("Elements Not Found " + xpath);
			throw new NoSuchElementException("Elements Not Found ");
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Function will return current home directory path
	 * @return String
	 */

	public String getCurrentHomeDirectoryPath() {
		return System.getProperty("user.dir") + "/";
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method will return all options present inside dropdown
	 * @param xpath
	 * @return List<WebElement>
	 */

	public List<WebElement> getOptionsFromDropdown(String xpath) {
		WebElement ele = getElement(xpath);
		Select sel = new Select(ele);
		return sel.getOptions();
	}

	/**
	 * @Author: Rajat
	 * @Desc: Function will return attribute value of element
	 * @param xpath
	 * @param attribute
	 * @return String
	 */

	public String getAttributeValue(String xpath, String attribute) {
		return getElement(xpath).getAttribute(attribute);
	}

	/**
	 * @Author: Rajat
	 * @Desc: Function will return attribute value using JavaScript
	 * @param xpath
	 * @param attribute
	 * @return String
	 */

	public String getAttributeByJs(String xpath, String attribute) {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		return (String) js.executeScript("return document.evaluate(\"" + xpath
				+ "\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.getAttribute(\""
				+ attribute + "\")");
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method will return text of element using xpath
	 * @param xpath
	 * @return String
	 */

	public String getTextFromXpath(String xpath) {
		waitUntilVisibleByElement(xpath);
		return getElement(xpath).getText();
	}

	/**
	 * @Author: Rajat
	 * @Desc: Method is used for wait using fluent wait
	 * @param by
	 * @param pollingPeriod
	 * @param totalTimeout
	 * @return boolean false if not present
	 */

	public boolean fluentwaitClick(By by, int pollingPeriod, int totalTimeout) {
		int grad = totalTimeout / pollingPeriod;
		boolean isVisible = false;
		while (grad > 0) {
			try {
				if (getDriver().findElement(by).isDisplayed()) {
					isVisible = true;
					break;
				}
			} catch (Exception e) {
				isVisible = false;
			}
			grad--;
		}
		return isVisible;
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to scroll up by window size
	 */

	public void scrollUp() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("window.scrollBy(0,-5000)");
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to scroll down by window size
	 */

	public void scrollDown() {
		JavascriptExecutor js = (JavascriptExecutor) getDriver();
		js.executeScript("window.scrollBy(0,5000)");
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to verify text from page resources
	 * @param asssertionText
	 * @return boolean false if not present
	 */

	public boolean verifyFromPageSource(String asssertionText) {
		return getDriver().getPageSource().contains(asssertionText);
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to perform mouse over action
	 * @param xpath
	 */

	public void mouseHover(String xpath) {
		Actions actions = new Actions(getDriver());
		actions.moveToElement(getElement(xpath)).perform();
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to perform right click operation
	 * @param xpath
	 */

	public void rightClick(String xpath) {
		Actions action = new Actions(getDriver());
		action.contextClick(getElement(xpath)).build().perform();
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to press enter after entering value
	 * @param xpath
	 */

	public void clickEnter(String xpath) {
		timetoWaitUntil(300);
		WebElement ele = getDriver().findElement(By.xpath(xpath));
		ele.sendKeys(Keys.ENTER);
	}

	/**
	 * @Author: Rajat
	 * @Desc: Used to switch to parent Frame
	 */

	public void switchToParentFrame() {
		getDriver().switchTo().parentFrame();
	}

	/**
	 * @Author: Rajat
	 * @Desc: Switch to frame by index
	 * @param frameIndex
	 */

	public void switchToFrameByIndex(int frameIndex) {
		try {
			getDriver().switchTo().frame(frameIndex);
			log.info("Navigated to frame with id: " + frameIndex);
		} catch (NoSuchFrameException e) {
			log.error("Unable to locate frame with id: " + frameIndex);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Switch to frame by string (name or id)
	 * @param frameNameOrId
	 */

	public void switchToFrameByString(String frameNameOrId) {
		try {
			getDriver().switchTo().frame(frameNameOrId);
			log.info("Navigated to frame with name: " + frameNameOrId);
		} catch (NoSuchFrameException e) {
			log.error("Unable to locate frame with name: " + frameNameOrId);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Switch to frame by WebElement
	 * @param frameElement
	 */

	public void switchToFrameByElement(WebElement frameElement) {
		try {
			if (frameElement.isDisplayed()) {
				getDriver().switchTo().frame(frameElement);
				log.info("Navigated to frame with element: " + frameElement);
			} else {
				log.info("Unable to navigate to frame with element: " + frameElement);
			}
		} catch (NoSuchFrameException e) {
			log.error("Unable to locate frame with element: " + frameElement);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Switch to multiple frames by parent and child frame
	 * @param parentFrame
	 * @param childFrame
	 */

	public void switchToMultipleFrame(String parentFrame, String childFrame) {
		try {
			getDriver().switchTo().frame(parentFrame).switchTo().frame(childFrame);
			log.info("Navigated to innerframe with id: " + childFrame + " which is present on frame with id: "
					+ parentFrame);
		} catch (NoSuchFrameException e) {
			log.info("Unable to locate frame with id: " + parentFrame + " or " + childFrame);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Switch to webpage from frame
	 */

	public void switchToDefaultFrame() {
		try {
			getDriver().switchTo().defaultContent();
			log.info("Navigated back to webpage from frame");
		} catch (Exception e) {
			log.debug("unable to navigate back to main webpage from frame" + e);
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: This method is used to get the CSS property of an element
	 * @param locator
	 * @param propertyName
	 * @return String
	 */

	@Step("Get {propertyName} value of an element")
	public String getCssValue(String locator, String propertyName) {
		WebElement element = getElement(locator);
		return element.getCssValue(propertyName);
	}
}