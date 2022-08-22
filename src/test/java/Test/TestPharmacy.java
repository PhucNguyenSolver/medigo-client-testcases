package Test;

import Common.*;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestPharmacy extends BaseClass {

    private boolean currentlyInMainPage() {
        final By mainPageTitle = By.id("xyz.medigo.pharmacy:id/tv_title");
        Assert.assertTrue(driver.findElements(mainPageTitle).size() != 0);
        return true;
    }

    @Test
    public void login() {
        final String USER_NAME = "automation";
        final String USER_MAIL = "automation@gmail.com";
        final String USER_PASS = "123123";

        final By tabHome = By.id("xyz.medigo.pharmacy:id/tab_home");
        final By nameField = By.id("xyz.medigo.pharmacy:id/login_phar_code");
        final By emailField = By.id("xyz.medigo.pharmacy:id/login_email");
        final By passwordField = By.id("xyz.medigo.pharmacy:id/login_password");
        final By loginButton = By.id("xyz.medigo.pharmacy:id/login_button");

        driver.findElement(tabHome).click();
        driver.findElement(nameField).sendKeys(USER_NAME);
        driver.findElement(By.id("xyz.medigo.pharmacy:id/continue_button")).click();
        driver.findElement(emailField).sendKeys(USER_MAIL);
        driver.findElement(passwordField).sendKeys(USER_PASS);
        driver.findElement(loginButton).click();
        Assert.assertTrue(currentlyInMainPage());
    }

    public boolean checkOrderFinallyReceived(long maxTimeoutInSecond) {
        WebDriverWait waiter = new WebDriverWait(driver, maxTimeoutInSecond);
        By incomingOrderPopup = By.id("xyz.medigo.pharmacy:id/btn_accept");
        By incomingOrderCard = By.id("xyz.medigo.pharmacy:id/card_accept");
        try {
            waiter.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(incomingOrderCard),
                    ExpectedConditions.presenceOfElementLocated(incomingOrderPopup)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void acceptAfterOrderReceived() {
        final By buttonCreateOrder = By.id("xyz.medigo.pharmacy:id/order_create");
        final By productName = By.id("xyz.medigo.pharmacy:id/tvProductName");
        final By swiperAccept = By.xpath(
                "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.ImageView");

        _processAfterOrderReceive();
        driver.findElement(buttonCreateOrder).click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(productName));

        Utils.swipeElementAndroid(
                driver.findElement(swiperAccept),
                "RIGHT", driver, 10000);

        final By buttonConfirm = By.id("xyz.medigo.pharmacy:id/btn_yes");
        final By successViewPrompt = By.id("xyz.medigo.pharmacy:id/lnButtonYes");
        driver.findElement(buttonConfirm).click();
        driver.findElement(successViewPrompt).click();
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

        disableImplicitlyWait();
        try {
            driver.findElement(incomingOrderPopup).click();
        } catch (Exception e) {
            System.out.print("incomingOrderPopup not found");
            try {
                driver.findElement(incomingOrderCard).click();
            } catch (Exception e2) {
                System.out.print("incomingOrderCard not found");
            }
        } finally {
            enableImplicitlyWait();
        }
    }

    // public void acceptAndModifyOrder() {
    // _processAfterOrderReceive();

    // driver.findElementById("xyz.medigo.pharmacy:id/order_create").click();
    // driver.findElementById("xyz.medigo.pharmacy:id/tvPlusOne").click();
    // driver.findElementById("xyz.medigo.pharmacy:id/btnAddMedicine").click();

    // Utils.trySleep(1500);
    // By swiper = By.id("xyz.medigo.pharmacy:id/button_send");
    // Utils.swipeElementAndroid(driver.findElement(swiper), "RIGHT", driver, 1000);
    // driver.findElementById("xyz.medigo.pharmacy:id/btn_yes").click();
    // }
}
