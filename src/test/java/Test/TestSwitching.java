package Test;

import Common.*;

import org.testng.Assert;
import org.testng.annotations.*;

public class TestSwitching extends BaseClass {

    final static long sendingOrderDelay = 10;
    final static long sendingRejectMessageDelay = 5;

    @Test
    public void testLaunchUserApp() {
        launchUserApp();
    }

    @Test
    public void testLaunchPharmacyApp() {
        launchPharmacyApp();
    }

    @Test
    public void testEcomOrderSuccess() {
        launchPharmacyApp();
        new TestPharmacy().login();
        
        launchUserApp();
        new TestUser().submitOrder();
        
        launchPharmacyApp();
        Assert.assertTrue(new TestPharmacy().checkOrderFinallyReceived(sendingOrderDelay));

        new TestPharmacy().acceptAfterOrderReceived();
    }

    @Test
    public void testEcomOrderCanceled() {
        launchPharmacyApp();
        new TestPharmacy().login();

        launchUserApp();
        new TestUser().submitAndCancelOrder();

        launchPharmacyApp();
        Assert.assertFalse(new TestPharmacy().checkOrderFinallyReceived(sendingOrderDelay));        
    }

    @Test
    public void testEcomOrderRejected() {
        launchPharmacyApp();
        new TestPharmacy().login();
        
        launchUserApp();
        new TestUser().submitOrder();
        
        launchPharmacyApp();
        Assert.assertTrue(new TestPharmacy().checkOrderFinallyReceived(sendingOrderDelay));
        new TestPharmacy().rejectAfterOrderReceived();
        
        launchUserApp();
        Assert.assertTrue(new TestUser().checkRejectMessageFinallyReceived(sendingRejectMessageDelay));
    }

    @Test
    public void testEcomOrderModified() {
        launchPharmacyApp();
        new TestPharmacy().login();
        
        launchUserApp();
        new TestUser().submitOrder();
        
        launchPharmacyApp();
        Assert.assertTrue(new TestPharmacy().checkOrderFinallyReceived(sendingOrderDelay));
        new TestPharmacy().acceptAndModifyOrder();
    }
}
