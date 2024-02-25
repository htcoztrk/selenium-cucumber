package com.testinium.Hook;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import groovy.util.logging.Slf4j;
import io.cucumber.java.Before;
import io.cucumber.java.After;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.getenv;

@Slf4j
public class Hooks {

    //public String URL = "http://hub.testinium.io/wd/hub";
    public String URL = "http://host.docker.internal:4444/wd/hub";

    String baseUrl = "http://yekg.konsorsiyum.io:3000/login";
    public static WebDriver webDriver;

    @Before
    public void beforeTest() {
        try {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();

            if (StringUtils.isNotEmpty(System.getenv("key"))) {
                System.out.println(getenv("**************************unutmaki dunya fani**************" +"key"));
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("w3c", false);
                options.addArguments("disable-translate");
                options.addArguments("--disable-notifications");
                options.addArguments("--start-fullscreen");
                //  options.addArguments("start-maximized");
                options.addArguments("test-type");
                options.addArguments("disable-popup-blocking");
                options.addArguments("ignore-certificate-errors");
                options.addArguments("--no-sandbox");
                Map<String, Object> prefs = new HashMap<>();
                options.setExperimentalOption("prefs", prefs);
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                capabilities.setCapability("key", System.getenv("key"));
                webDriver = new RemoteWebDriver(new URL(URL), capabilities);
                ((RemoteWebDriver) webDriver).setFileDetector(new LocalFileDetector());
            }

            else {
                System.out.println("*****************************key yok****************");
                System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-fullscreen");
                //options.addArguments("start-maximized");
                options.addArguments("test-type");
                options.addArguments("disable-popup-blocking");
                options.addArguments("ignore-certificate-errors");
                options.addArguments("disable-translate");
                options.addArguments("--no-sandbox");
                options.setExperimentalOption("w3c",false);
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                webDriver = new ChromeDriver(options);

            }


            webDriver.get(baseUrl);
            webDriver.manage().window().fullscreen();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @After
    public void afterTest() {
        if(webDriver != null) {
            webDriver.quit();
        }
    }

    public static WebDriver getWebDriver() {
        return webDriver;
    }
}