package com.amazon.page;

import com.amazon.utils.PageFunctions;

public class AmazonLandingStep extends PageFunctions {

	public AmazonLandingStep() {
		super();
	}

	public void waitForCarousel() {
		waitUntilVisibleByElement(AmazonLandingPagePO.carousel);
	}

	public String getActiveBannerImage() {
		return getAttributeValue(AmazonLandingPagePO.activeImage, AmazonLandingPagePO.srcAtribute);
	}

	public void clickNextBanner() {
		clickElementUsingXPath(AmazonLandingPagePO.nextArrow);
	}

	public boolean waitForBannerImageToChange(String oldValue) {

		return waitForAttributeToChange(AmazonLandingPagePO.activeImage, AmazonLandingPagePO.srcAtribute, oldValue);
	}

}
