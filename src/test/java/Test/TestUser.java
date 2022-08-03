package Test;

import Common.*;
import io.appium.java_client.MobileBy;
import io.appium.java_client.functions.ExpectedCondition;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestUser extends BaseClass {

    @BeforeMethod
    private void gotoPage() {
        launchUserApp();
    }

    public void login() {
        final String PHONE = "0111111111";
        final String DEFAULT_OTP = "123456";
        WebDriverWait wait = new WebDriverWait(driver, 6);
        // final String INVALID_PHONE = "01111";
        // final String INVALID_OTP = "111111";

        final By phoneField = By.id("xyz.medigo.user:id/edtPhoneNumber");
        final By btnGetSMS = By.id("xyz.medigo.user:id/btnGetSMSCode");
        final By otpField = By.id("xyz.medigo.user:id/txt_pin_entry");
        final By btnVerifyOTP = By.id("xyz.medigo.user:id/btnVerifySMSCode");
        final By optionInputLocation = By.id("xyz.medigo.user:id/tv_input_location");
        // if (driver.findElements(phoneField).isEmpty()) {
        // // skip get phone number screen
        // } else {
        driver.findElement(phoneField).sendKeys(PHONE);
        driver.findElement(btnGetSMS).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(otpField));
        // }
        // if (driver.findElements(otpField).isEmpty()) {
        // // skip get OTP screen
        // } else {
        driver.findElement(otpField).sendKeys(DEFAULT_OTP);
        driver.findElement(btnVerifyOTP).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(optionInputLocation));
        // }

        // final By optionInputLocation = By.id("xyz.medigo.user:id/tv_input_location");
        final By savedLocation1 = By.id("xyz.medigo.user:id/tv_address");
        final By btnSubmitLocation = By.id("xyz.medigo.user:id/action_done");
        final By tabHome = By.id("xyz.medigo.user:id/tab_home");
        // if (driver.findElements(optionInputLocation).isEmpty()) {
        // // skip get location screen
        // } else {
        driver.findElement(optionInputLocation).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(savedLocation1));
        driver.findElement(savedLocation1).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(btnSubmitLocation));
        driver.findElement(btnSubmitLocation).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(tabHome));
        // }
        Assert.assertTrue(driver.findElement(tabHome).isDisplayed());
    }

    // // final By tabHome = By.id("xyz.medigo.user:id/tab_home");
    // final By tabMedicine = By.id("xyz.medigo.user:id/tab_medicine");
    // final By tabOrder = By.id("xyz.medigo.user:id/tab_user_order");
    // final By tabMessage = By.id("xyz.medigo.user:id/tab_message");
    // final By tabAccount = By.id("xyz.medigo.user:id/tab_account");

    public void submitOrder() {
        login();
        createOrderBeforeCheckout();
        checkoutOrderAfterCreate();
        gotoMainPageAfterCheckout();
    }

    public void submitAndCancelOrder() {
        login();
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

    // private void cancelOrder() {
    // driver.findElement(By.id("xyz.medigo.user:id/tab_user_order")).click();
    // By lastOrder = By.xpath(
    // "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.RelativeLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.LinearLayout");
    // driver.findElement(lastOrder).click();
    // }

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
        WebDriverWait wait = new WebDriverWait(driver, 5);
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
