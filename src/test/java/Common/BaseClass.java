package Common;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BaseClass {
    private static final String deviceId = "emulator-5554";
    private static final String localPath = "http://127.0.0.1:4723/wd/hub";
    private static final String userAppPackage = "xyz.medigo.user";
    private static final String pharAppPackage = "xyz.medigo.pharmacy";

    public static AndroidDriver<MobileElement> driver = null;

    @BeforeSuite
    public void setup() {
        // clearAppsData();
        // setupLocalAppiumDriver();
        setupAWSDriver();
    }

    @AfterSuite
    public void tearDown() {
        driver.quit();
    }

    private void setupAWSDriver() {
        try {
            URL url = new URL(localPath);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            driver = new AndroidDriver<>(url, capabilities);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupLocalAppiumDriver() {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("deviceName", "Android Emulator");
            cap.setCapability("platformName", "Android");
            cap.setCapability("udid", deviceId);
            URL url = new URL(localPath);
            driver = new AndroidDriver<>(url, cap);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void launchUserApp() {
        driver.activateApp(userAppPackage);
        waitForPackage(userAppPackage, 15);
    }

    public void launchPharmacyApp() {
        driver.activateApp(pharAppPackage);
        waitForPackage(pharAppPackage, 15);
    }

    public void waitForPackage(String desiredPackage, int timeoutInSeconds) {
        int counter = 0;
        do {
            String currentPackage = driver.getCurrentPackage();
            System.out.print(counter);
            System.out.println(" secs: " + currentPackage);
            if (currentPackage.equals(desiredPackage)) {
                return;
            }
            Utils.trySleep(1000);
            counter++;
        } while (counter < timeoutInSeconds);
        Assert.fail("waitForPackage:Timeout");
    }

    public void clearAppsData() {
        final String pharAppActivity = "xyz.medigo.pharmacy.ui.login.loginpharcode.LoginPharCodeActivity";
        final String userAppActivity = "xyz.medigo.user.ui.login.LoginActivity";
        try {
            AndroidDriver<MobileElement> _driver;
            _driver = makeTemporaryAppiumDriver(userAppPackage, userAppActivity);
            _driver.resetApp();
            _driver = makeTemporaryAppiumDriver(pharAppPackage, pharAppActivity);
            _driver.resetApp();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private AndroidDriver<MobileElement> makeTemporaryAppiumDriver(String appPackage, String appActivity)
            throws MalformedURLException {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("deviceName", "Android Emulator");
        cap.setCapability("platformName", "Android");
        cap.setCapability("udid", deviceId);
        cap.setCapability("appPackage", appPackage);
        cap.setCapability("appActivity", appActivity);
        URL url = new URL(localPath);
        return new AndroidDriver<>(url, cap);
    }
}
