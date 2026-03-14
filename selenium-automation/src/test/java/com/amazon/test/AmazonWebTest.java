package com.amazon.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.amazon.base.BaseTest;

public class AmazonWebTest extends BaseTest{
	 
    @Test(priority = 1, description = "TC_ID: 001 =>Verify Promotional Banner Carousel", alwaysRun = true)
	public void verifyCarouselFunctionality() {
    	
		amazonLandingStep.waitForCarousel();
		
		String currentSlideImage =amazonLandingStep.getActiveBannerImage();
		amazonLandingStep.clickNextBanner();
		amazonLandingStep.waitForBannerImageToChange(currentSlideImage);
		
		String newCurrentSlideImage =amazonLandingStep.getActiveBannerImage();
		Assert.assertNotEquals(currentSlideImage, newCurrentSlideImage,"Carousel banner image did not change after clicking next.");
		
	 }

}
