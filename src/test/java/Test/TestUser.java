package Test;

import Common.*;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestUser extends BaseClass {

    @BeforeMethod
    private void goToMainPage() {
        launchUserApp();
    }

    public void login() {
        final String PHONE = "0111111111";
        final String DEFAULT_OTP = "123456";
        WebDriverWait wait = new WebDriverWait(driver, 10);

        final By phoneField = By.id("xyz.medigo.user:id/edtPhoneNumber");
        final By btnGetSMS = By.id("xyz.medigo.user:id/btnGetSMSCode");
        final By otpField = By.id("xyz.medigo.user:id/txt_pin_entry");
        final By btnVerifyOTP = By.id("xyz.medigo.user:id/btnVerifySMSCode");
        driver.findElement(phoneField).sendKeys(PHONE);
        driver.findElement(btnGetSMS).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(otpField));
        driver.findElement(otpField).sendKeys(DEFAULT_OTP);
        driver.findElement(btnVerifyOTP).click();
    }

    public void selectLocation() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        final By optionInputLocation = By.id("xyz.medigo.user:id/tv_input_location");
        final By savedLocation1 = By.id("xyz.medigo.user:id/tv_address");
        final By btnSubmitLocation = By.id("xyz.medigo.user:id/action_done");
        final By tabHome = By.id("xyz.medigo.user:id/tab_home");
        driver.findElement(optionInputLocation).click();
        driver.findElement(savedLocation1).click();
        driver.findElement(btnSubmitLocation).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(tabHome));
        Assert.assertTrue(currentlyInMainPage());
    }

    public boolean currentlyInMainPage() {
        final By tabHome = By.id("xyz.medigo.user:id/tab_home");
        return (driver.findElements(tabHome).size() > 0);
    }

    public void submitOrder() {
        createOrderBeforeCheckout();
        checkoutOrderAfterCreate();
        gotoMainPageAfterCheckout();
    }

    public void submitAndCancelOrder() {
        createOrderBeforeCheckout();
        checkoutOrderAfterCreate();
        final long maxTimeoutInSecond = 45;
        cancelDisplayingOrderAndBack(maxTimeoutInSecond);
    }

    public boolean checkRejectMessageFinallyReceived(long maxTimeoutInSecond) {
        WebDriverWait wait = new WebDriverWait(driver, maxTimeoutInSecond);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xyz.medigo.user:id/cancel_message")));
            driver.findElement(By.id("xyz.medigo.user:id/find_cancel")).click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void cancelDisplayingOrderAndBack(long maxTimeoutInSecond) {
        By swiper = By.id("xyz.medigo.user:id/tvPharmacyName");
        Utils.swipeElementAndroid(
                driver.findElement(swiper),
                "DOWN", driver, 3000);

        WebDriverWait wait = new WebDriverWait(driver, maxTimeoutInSecond);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("xyz.medigo.user:id/tvCancel"))).click();
            By aReason = By.xpath(
                    "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[1]");
            driver.findElement(aReason).click();
            driver.findElement(By.id("xyz.medigo.user:id/cancel_order_sent")).click();
        } catch (Exception e) {
            System.err.print(e.getMessage());
            Assert.fail(String.format("Cancel order button not shown within %d seconds", maxTimeoutInSecond));
        }
    }

    @Test
    private void createOrderBeforeCheckout() {
        final By tabMedicine = By.id("xyz.medigo.user:id/tab_medicine");
        final By searchField = By.id("xyz.medigo.user:id/tvSearch");
        driver.findElement(tabMedicine).click();
        driver.findElement(searchField).click();
        driver.findElement(By.id("xyz.medigo.user:id/edtSearch")).sendKeys("dong");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        By expectedProduct = MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"ng Tr\")");
        wait.until(ExpectedConditions.presenceOfElementLocated(expectedProduct));
        driver.findElement(expectedProduct).click();

        final By buyNowButton = By.id("xyz.medigo.user:id/tvBuyNow");
        driver.findElement(buyNowButton).click();
        final By checkoutView = By.id("xyz.medigo.user:id/tvNext");
        wait.until(ExpectedConditions.presenceOfElementLocated(checkoutView));
    }

    private void checkoutOrderAfterCreate() {
        final By submitOrderButton = By.id("xyz.medigo.user:id/tvNext");
        final By orderSuccessView = By.id("xyz.medigo.user:id/tvOrderSucessView");

        Assert.assertFalse(driver.findElements(submitOrderButton).isEmpty());

        final By payByCOD = By.xpath(
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.ImageView[1]");
        driver.findElement(payByCOD).click();
        driver.findElement(submitOrderButton).click();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.presenceOfElementLocated(orderSuccessView));
        driver.findElement(orderSuccessView).click();
    }

    private void gotoMainPageAfterCheckout() {
        driver.navigate().back();
        By tabOrder = By.id("xyz.medigo.user:id/tab_user_order");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabOrder));
    }

    // // "com.android.permissioncontroller:id/permission_message"
    // // Assert text == "Allow Medigo Dev to record audio?"
    // // handle these case, vary depend on android os version
    // //
    // "com.android.permissioncontroller:id/permission_allow_foreground_only_button"
    // // "com.android.permissioncontroller:id/permission_allow_one_time_button"
    // // "com.android.permissioncontroller:id/permission_allow_button"

    // // todo: handle call
    // xyz.medigo.user:id/audio_btn_answer
    // xyz.medigo.user:id/audio_btn_end
    // wait.until(ExpectedConditions.presenceOfElementLocated(By.id()));
    // MobileElement user23 =
    // driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button"));
    // user23.click();
    // MobileElement user24 =
    // driver.findElement(By.id("xyz.medigo.user:id/audio_btn_answer"));
    // user24.click();

    // // // todo: handle
    // wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xyz.medigo.user:id/shipper_number")));
    // driver.navigate().back();
    // MobileElement user26 =
    // driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.view.ViewGroup");
    // user26.click();

}
