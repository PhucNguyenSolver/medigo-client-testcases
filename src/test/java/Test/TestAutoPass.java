package Test;

import org.testng.Assert;
import org.testng.annotations.*;

import Common.BaseClass;

public class TestAutoPass extends BaseClass {
    @Test
    public void login() {
        launchPharmacyApp();
        new TestPharmacy().login();
        launchUserApp();
        new TestUser().login();
    }

    @Test
    public void testAutoPass() {
        launchPharmacyApp();
        launchUserApp();
    }

    @Test
    public void testAutoFail() {
        Assert.fail();
    }
}
