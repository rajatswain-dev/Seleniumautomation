package com.amazon.objectregistry;

import com.amazon.page.AmazonLandingStep;
import com.amazon.utils.PageFunctions;

public class PageObjectRegistry {
  
	protected PageFunctions     pageFunctions;
	protected AmazonLandingStep amazonLandingStep;
 
	public void initPageObjects() {
		pageFunctions     = new PageFunctions();
		amazonLandingStep = new AmazonLandingStep();
	}

}