package com.amazon.driver;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.amazon.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

    // ThreadLocal ensures each thread (parallel test) has its own driver
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @SuppressWarnings("deprecation")
    public static WebDriver createDriver() {
        String browser = Config.get(Config.ConfigProperty.BROWSER);
        boolean runLocally = Config.getBool(Config.ConfigProperty.RUN_LOCALLY);
        boolean headless = Config.getBool(Config.ConfigProperty.HEADLESS);

        WebDriver driver;

        if (runLocally) {
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-notifications");
                    if (headless) chromeOptions.addArguments("--headless=new");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) firefoxOptions.addArguments("--headless");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                default:
                    throw new RuntimeException("Browser not supported: " + browser);
            }
        } else {
            // Remote Grid Execution
            try {
                driver = new RemoteWebDriver(
                        new URL("http://" +
                                Config.get(Config.ConfigProperty.SELENIUM_HOST) +
                                ":" +
                                Config.get(Config.ConfigProperty.SELENIUM_PORT)),
                        new ChromeOptions()
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to create RemoteWebDriver", e);
            }
        }

        // Store driver in ThreadLocal
        driverThreadLocal.set(driver);
        return driver;
    }

    // Get the current thread's driver
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    // Quit and clean up driver
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove(); // prevent memory leaks
        }
    }
}