package Test;

import Common.*;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

public class TestPharmacy extends BaseClass {
    private static WebDriverWait wait = new WebDriverWait(driver, 6);
    private static final By mainPageTitle = By.id("xyz.medigo.pharmacy:id/tv_title");

    @BeforeMethod
    public void gotoPage() {
        launchPharmacyApp();
    }

    @Test
    public void login() {
        // final String USER_NAME = "automation";
        // final String USER_MAIL = "automation@gmail.com";
        // final String USER_PASS = "123123";
        final String USER_NAME = "cuongmedi";
        final String USER_MAIL = "lekubin00@gmail.com";
        final String USER_PASS = "lekubin00";

        final By nameField = By.id("xyz.medigo.pharmacy:id/login_phar_code");
        final By emailField = By.id("xyz.medigo.pharmacy:id/login_email");
        final By passwordField = By.id("xyz.medigo.pharmacy:id/login_password");
        final By loginButton = By.id("xyz.medigo.pharmacy:id/login_button");

        Utils.trySleep(1000);
        if (driver.findElements(nameField).isEmpty()) {
            // skip username screen
        } else {
            driver.findElement(nameField).sendKeys(USER_NAME);
            driver.findElement(By.id("xyz.medigo.pharmacy:id/continue_button")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(emailField));
        }

        if (driver.findElements(emailField).isEmpty()) {
            // skip email and password screen
        } else {
            driver.findElement(emailField).sendKeys(USER_MAIL);
            driver.findElement(passwordField).sendKeys(USER_PASS);
            wait.until(ExpectedConditions.presenceOfElementLocated(loginButton));
            driver.findElement(loginButton).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(mainPageTitle));
        }
    }
    
    public boolean checkOrderFinallyReceived(long maxTimeoutInSecond) {
        WebDriverWait waiter = new WebDriverWait(driver, maxTimeoutInSecond);
        By incomingOrderPopup = By.id("xyz.medigo.pharmacy:id/btn_accept");
        By incomingOrderCard = By.id("xyz.medigo.pharmacy:id/card_accept");
        try {
            waiter.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(incomingOrderCard),
                ExpectedConditions.presenceOfElementLocated(incomingOrderPopup)
            ));
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public void acceptAfterOrderReceived() {
        final By buttonCreateOrder = By.id("xyz.medigo.pharmacy:id/order_create");
        final By productName = By.id("xyz.medigo.pharmacy:id/tvProductName");
        final By swiperAccept = By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.ImageView");
        
        _processAfterOrderReceive();
        wait.until(ExpectedConditions.presenceOfElementLocated(buttonCreateOrder)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(productName));
        Utils.swipeElementAndroid(
            driver.findElement(swiperAccept),
            "RIGHT", driver, 1500);
    
        final By buttonConfirm = By.id("xyz.medigo.pharmacy:id/btn_yes");
        final By successViewPrompt = By.id("xyz.medigo.pharmacy:id/lnButtonYes");
        wait.until(ExpectedConditions.presenceOfElementLocated(buttonConfirm)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(successViewPrompt)).click();
    }

    @Test
    public void rejectAfterOrderReceived() {
        final By cancelOrderButton = By.id("xyz.medigo.pharmacy:id/button_exit");
        final By aReason = By.xpath(
            "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.RadioGroup/android.widget.RadioButton[1]");
        final By confirmCancelOrderButton = By.id("xyz.medigo.pharmacy:id/btn_send_cancel");
        
        _processAfterOrderReceive();
        driver.findElement(cancelOrderButton).click();
        driver.findElement(aReason).click();
        driver.findElement(confirmCancelOrderButton).click();    
    }

    private void _processAfterOrderReceive() {
        final By incomingOrderPopup = By.id("xyz.medigo.pharmacy:id/btn_accept");
        final By incomingOrderCard = By.id("xyz.medigo.pharmacy:id/card_accept");
        try {
            driver.findElement(incomingOrderPopup).click();
        } catch(Exception e) {
            driver.findElement(incomingOrderCard).click();
        }
    }

    public void acceptAndModifyOrder() {
        driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().textContains(\"NHẬN ĐƠN\"))"))
                .click();
        driver.findElementById("xyz.medigo.pharmacy:id/order_create").click();
        driver.findElementById("xyz.medigo.pharmacy:id/lnAddMoreProduct").click();
        driver.findElementByAccessibilityId("Từ kho hàng").click();
        driver.findElementById("xyz.medigo.pharmacy:id/edtSearch").sendKeys("dong");
        driver.findElementByAndroidUIAutomator("new UiSelector().textContains(\"Tăng cường sức khỏe\")").click();
        driver.findElementById("xyz.medigo.pharmacy:id/tvPlusOne").click();
        driver.findElementById("xyz.medigo.pharmacy:id/btnAddMedicine").click();

        Utils.trySleep(1500);
        By swiper = By.id("xyz.medigo.pharmacy:id/button_send");
        Utils.swipeElementAndroid(driver.findElement(swiper), "RIGHT", driver, 1000);
        driver.findElementById("xyz.medigo.pharmacy:id/btn_yes").click();
    }
}
