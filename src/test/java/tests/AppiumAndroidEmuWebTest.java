package tests;


import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;



public class AppiumAndroidEmuWebTest {

    private static ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<AndroidDriver>();

    String url = "https://www.saucedemo.com/";

    By usernameInput = By.id("user-name");
    By passwordInput = By.id("password");
    By submitButton = By.className("btn_action");
    By productTitle = By.className("inventory_list");


    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {

        System.out.println("Starting LoginWebTest...");
        String SAUCE_REMOTE_URL = "yourRemoteURL here";

        String methodName = method.getName();
        URL url = new URL(SAUCE_REMOTE_URL);

        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.setCapability("deviceName", "Google Pixel 3 XL GoogleAPI Emulator");
        chromeOptions.setCapability("deviceName", "Samsung_Galaxy_S9_free");
        chromeOptions.setCapability("platformVersion", "11.0");
        chromeOptions.setCapability("platformName", "Android");
        chromeOptions.setCapability("automationName", "UiAutomator2");
        chromeOptions.setCapability("browserName", "Chrome");
        chromeOptions.setCapability("name", methodName);
        chromeOptions.setExperimentalOption("w3c", false);
        try {
            androidDriver.set(new AndroidDriver(url, chromeOptions));
        } catch (Exception e) {
            System.out.println("Cannot create Android Driver " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
    @AfterMethod
    public void teardown(ITestResult result) {
        try {
            ((JavascriptExecutor) getAndroidDriver()).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        } finally {
            System.out.println("Closing Android Driver...");
            getAndroidDriver().quit();
        }
    }

    public  AndroidDriver getAndroidDriver() {
        return androidDriver.get();
    }

    @Test
    public void loginWebTestValid() {
        System.out.println("Starting LoginWebTest...");
        AndroidDriver driver = getAndroidDriver();
        driver.get(url);
        login("standard_user", "secret_sauce");
        Assert.assertTrue(isOnProductsPage());
    }
    public void login(String user, String pass){
        AndroidDriver driver = getAndroidDriver();
        driver.findElement(usernameInput).sendKeys(user);
        driver.findElement(passwordInput).sendKeys(pass);
        driver.hideKeyboard();
        driver.findElement(submitButton).click();
    }
    public boolean isOnProductsPage() {
        AndroidDriver driver = getAndroidDriver();
        return driver.findElement(productTitle).isDisplayed();
    }
}