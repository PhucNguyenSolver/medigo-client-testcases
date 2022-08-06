package Common;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

public class Utils {

    public static void trySleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            System.out.println("Cannot sleep");
            e.printStackTrace();
        }
    }

    protected void waitForElement(AppiumDriver<MobileElement> driver, By byCondition) throws TimeoutException {
        final int MAX_ATTEMPTS = 30;
        final int TICK = 200;

        for (int trial = 0; trial < MAX_ATTEMPTS; trial++) {
            if (driver.findElement(byCondition) != null) {
                return;
            }
            try {
                Thread.sleep(TICK);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        String msg = "Element not found: " + byCondition.toString();
        throw new TimeoutException(msg);
    }

    /**
     * Performs swipe inside an element
     *
     * @param el  the element to swipe
     * @param dir the direction of swipe
     * @version java-client: 7.3.0
     **/
    public static void swipeElementAndroid(MobileElement el, String dir, AppiumDriver<MobileElement> driver, int s) {
        final int ANIMATION_TIME = 500; // ms
        final int PRESS_TIME = 500; // ms

        Rectangle rect = el.getRect();
        PointOption pointOptionStart, pointOptionEnd;
        pointOptionStart = PointOption.point(rect.x, rect.y);
        switch (dir) {
            case "DOWN": // from up to down
                pointOptionEnd = PointOption.point(rect.x, rect.y - s);
                break;
            case "UP": // from down to up
                pointOptionEnd = PointOption.point(rect.x, rect.y + s);
                break;
            case "LEFT": // from right to left
                pointOptionEnd = PointOption.point(rect.x - s, rect.y);
                break;
            case "RIGHT": // from left to right
                pointOptionEnd = PointOption.point(rect.x + s, rect.y);
                break;
            default:
                throw new IllegalArgumentException("swipeElementAndroid(): dir: '" + dir + "' NOT supported");
        }

        // execute swipe using TouchAction
        try {
            new TouchAction(driver)
                    .press(pointOptionStart)
                    // a bit more reliable when we add small wait
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            System.err.println("swipeElementAndroid(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

        // always allow swipe action to complete
        try {
            Thread.sleep(ANIMATION_TIME);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
