package com.companyName.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class WaitUtils {

	/**
	 * Wait until element to be visible but element should be present on DOM
	 *
	 * @param ele
	 * @param timeOut
	 */
	public static boolean waitForElementVisiblity(RemoteWebDriver driver,WebElement ele, int timeOut) {
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.visibilityOf(ele));
			return true;
		}catch (Exception e){
			return false;
		}

	}

	/**
	 * Wait for nested element present on Dom
	 *
	 * @param by
	 */
	public static boolean waitForNestedEle(RemoteWebDriver driver,By parentLocator, By by) {
		try{
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentLocator, by));
			return true;
		}catch (Exception e){
			return false;
		}

	}

	/**
	 * Wait for element to be present on Dom
	 *
	 * @param by
	 * @param timeOut
	 */
	public static boolean waitForElementOnDom(RemoteWebDriver driver,By by, int timeOut) {
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			return true;
		}catch (Exception e){
			return false;
		}

	}

	/**
	 * Wait until element to be invisible
	 *
	 * @param ele
	 * @param timeOut
	 */
	public static boolean waitForElementInvisible(RemoteWebDriver driver,WebElement ele, int timeOut) {
		try{
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.invisibilityOf(ele));
			return true;
		}catch (Exception e){
			return false;
		}

	}

	/**
	 * Wait until element to be enable
	 *
	 * @param ele
	 * @param timeOut
	 */
	public static boolean waitForElementToEnable(RemoteWebDriver driver,WebElement ele, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			return true;
		}catch (Exception e){
			return false;
		}

	}

	/**
	 * Wait until respective text is visible
	 *
	 * @param ele
	 * @param timeOut
	 * @param expectedString
	 */
	public static boolean waitForTextVisible(RemoteWebDriver driver,WebElement ele, int timeOut, String expectedString) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.textToBePresentInElement(ele, expectedString));
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public static boolean waitListElements(RemoteWebDriver driver,By ele, int timeOut) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ele));
			return true;
		}catch (Exception e){
			return false;
		}
	}
}
