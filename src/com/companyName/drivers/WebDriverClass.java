package com.companyName.drivers;

import com.companyName.utils.FrameworkVariables;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;

import static java.util.Objects.isNull;

public class WebDriverClass {

        private static ThreadLocal<RemoteWebDriver> driver = null;

        /**
         * Get the respective driver given in frameworkvariables.properties
         * @return ThreadLocal<RemoteWebDriver>
         */
        public ThreadLocal<RemoteWebDriver> getDriver() {
            driver = new ThreadLocal<>();
            String browser = System.getenv("browser");
            if(isNull(browser)) {
                browser = FrameworkVariables.browser;
            }
            createBrowserInstance(browser);
            maximizeBrowserWindow();
            return driver;
        }

    private void maximizeBrowserWindow() {
        driver.get().manage().window().maximize();
    }

    private void createBrowserInstance(String browser) {
        if(browser.equalsIgnoreCase("chrome")){
            createChromeInstance();
        } else if(browser.equalsIgnoreCase("firefox")){
            createFirefoxInstance();
        } else if(browser.equalsIgnoreCase("safari")) {
            createSafariInstance();
        }
    }

    /**
         * Set chrome driver respective to OS
         */
        private void createChromeInstance(){
            WebDriverManager.chromedriver().setup();

            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setExperimentalOption("prefs", chromePrefs);
            options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            options.setCapability(ChromeOptions.CAPABILITY, options);

            if (FrameworkVariables.jenkinsCheck != null) {
                options.setHeadless(true);
            }

            driver.set(new ChromeDriver(options));

        }

        /**
         * Set firefox driver respective to OS
         */
        private void createFirefoxInstance() {
            WebDriverManager.firefoxdriver().setup();

            HashMap<String, Object> preferences = new HashMap<>();
            preferences.put("profile.default_content_settings.popups", 0);
            FirefoxOptions options = new FirefoxOptions();
            options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

            if (FrameworkVariables.jenkinsCheck != null) {
                options.setHeadless(true);
            }

            driver.set(new FirefoxDriver(options));
        }

        /**
         * Set safari browser for MAC
         */
        private void createSafariInstance(){
            WebDriverManager.safaridriver().setup();
            SafariOptions options = new SafariOptions();
            options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            driver.set(new SafariDriver());
        }
}
