package tests;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import static tests.Config.host;

public class AppiumAndroidEmuAppTest {

    private static ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<AndroidDriver>();

    String usernameID = "test-Username";
    String passwordID = "test-Password";
    String submitButtonID = "test-LOGIN";
    By ProductTitle = By.xpath("//android.widget.TextView[@text='PRODUCTS']");


    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        System.out.println("Preparing Android App Test");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String methodName = method.getName();
        String appName = "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
        URL url;


        String SAUCE_REMOTE_URL = "yourRemoteURL here";
        url = new URL(SAUCE_REMOTE_URL);

        capabilities.setCapability("deviceName", "Google Pixel 3 XL GoogleAPI Emulator");
        //capabilities.setCapability("deviceName", "Samsung_Galaxy_S9_free");
        capabilities.setCapability("platformVersion", "11.0");
        capabilities.setCapability("app", "storage:filename=" + appName);
        capabilities.setCapability("name", methodName);
        capabilities.setCapability("appiumVersion", "1.18.1");
        capabilities.setCapability("appActivity", "com.swaglabsmobileapp.MainActivity");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");

        try {
            androidDriver.set(new AndroidDriver(url, capabilities));
        } catch (Exception e) {
            System.out.println("Cannot create Android Driver " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        try {
            if (host.equals("saucelabs")) {
                ((JavascriptExecutor) getAndroidDriver()).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
            }
        } finally {
            System.out.println("Closing Android Driver...");
            getAndroidDriver().quit();
        }
    }

    public AndroidDriver getAndroidDriver() {
        return androidDriver.get();
    }

    @Test
    public void loginAppTestValid() {
        System.out.println("Starting LoginAppTest...");
        login("standard_user", "secret_sauce");
        Assert.assertTrue(isOnProductsPage());
    }
    public void login(String user, String pass){
        AndroidDriver driver = getAndroidDriver();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        final WebElement usernameEdit = wait.until(ExpectedConditions.visibilityOfElementLocated(new MobileBy.ByAccessibilityId(usernameID)));

        usernameEdit.click();
        usernameEdit.sendKeys(user);

        WebElement passwordEdit = driver.findElementByAccessibilityId(passwordID);
        passwordEdit.click();
        passwordEdit.sendKeys(pass);

        WebElement submitButton = driver.findElementByAccessibilityId(submitButtonID);
        submitButton.click();
    }
    public boolean isOnProductsPage() {
        AndroidDriver driver = getAndroidDriver();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(ProductTitle));
        } catch (TimeoutException e){
            return false;
        }
        return true;
    }
}