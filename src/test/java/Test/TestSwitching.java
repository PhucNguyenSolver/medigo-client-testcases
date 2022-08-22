package Test;

import Common.*;

import org.testng.Assert;
import org.testng.annotations.*;

public class TestSwitching extends BaseClass {

    final static long sendingOrderDelay = 10;
    final static long sendingRejectMessageDelay = 5;

    @Test
    public void login() {
        launchPharmacyApp();
        new TestPharmacy().login();
        launchUserApp();
        new TestUser().login();
        new TestUser().selectLocation();
    }

    @Test
    public void testEcomOrderSuccess() {
        launchUserApp();
        new TestUser().submitOrder();

        launchPharmacyApp();
        Assert.assertTrue(new TestPharmacy().checkOrderFinallyReceived(sendingOrderDelay));

        new TestPharmacy().acceptAfterOrderReceived();
    }

    @Test
    public void testEcomOrderCanceled() {
        launchUserApp();
        new TestUser().submitAndCancelOrder();

        launchPharmacyApp();
        Assert.assertFalse(new TestPharmacy().checkOrderFinallyReceived(sendingOrderDelay));
    }

    @Test
    public void testEcomOrderRejected() {
        launchUserApp();
        new TestUser().submitOrder();

        launchPharmacyApp();
        Assert.assertTrue(new TestPharmacy().checkOrderFinallyReceived(sendingOrderDelay));
        new TestPharmacy().rejectAfterOrderReceived();

        launchUserApp();
        Assert.assertTrue(new TestUser().checkRejectMessageFinallyReceived(sendingRejectMessageDelay));
    }
}
