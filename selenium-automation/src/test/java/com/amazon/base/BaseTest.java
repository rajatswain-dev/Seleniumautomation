package com.amazon.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.amazon.config.Config;
import com.amazon.driver.DriverFactory;
import com.amazon.objectregistry.*;

public class BaseTest extends PageObjectRegistry {

	@BeforeClass
	public void initSteps() {
		initPageObjects();
	}

	@BeforeMethod
	public void setUp() {
		WebDriver driver = DriverFactory.createDriver();
		driver.get(Config.get(Config.ConfigProperty.BASE_URL));
		driver.manage().window().maximize();
	}

	@AfterMethod
	public void tearDown() {
		DriverFactory.quitDriver();
	}
}