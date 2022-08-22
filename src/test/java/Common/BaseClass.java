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
        System.out.println("local: " + System.getProperty("local"));
        if ("true".equals(System.getProperty("local"))) {
            // clearAppsData();
            setupLocalAppiumDriver();
            enableImplicitlyWait();
        } else {
            setupAWSDriver();
            enableImplicitlyWait();
        }
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
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupLocalAppiumDriver() {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("deviceName", "Android Emulator");
            cap.setCapability("platformName", "Android");
            cap.setCapability("udid", deviceId);
            URL url = new URL(localPath);
            driver = new AndroidDriver<>(url, cap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void enableImplicitlyWait() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void disableImplicitlyWait() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    public void launchUserApp() {
        launchApp(userAppPackage);
    }

    public void launchPharmacyApp() {
        launchApp(pharAppPackage);
    }

    private void launchApp(String appPackage) {
        final int timeoutInSeconds = 15;
        Assert.assertTrue(driver.isAppInstalled(appPackage));
        driver.activateApp(appPackage);
        waitForPackage(appPackage, timeoutInSeconds);
    }

    private void waitForPackage(String desiredPackage, int timeoutInSeconds) {
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
        Assert.fail(String.format("Timeout (%d) while waitForPackage %s", desiredPackage, timeoutInSeconds));
    }

    private void clearAppsData() {
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
