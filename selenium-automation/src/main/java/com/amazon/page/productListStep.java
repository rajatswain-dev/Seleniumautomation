package com.amazon.page;

import com.amazon.utils.PageFunctions;

import java.text.MessageFormat;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class productListStep extends PageFunctions {

	public String getProductTitle(String labelText) {
		return getTextFromXpath(MessageFormat.format(productListPO.productTitle, labelText));
	}

	public List<WebElement> getSareeList() {
		return getElementsFromXpath(productListPO.listOfSilkSaree);

	}

	/**
	 * @Author: Rajat
	 * @Desc: Click Add to Cart button
	 */

	public void clickAddToCart() {
		WebElement addToCart = getElementFromID(productListPO.addToCart);
		if (addToCart != null) {
			addToCart.click();
		} else {
			Assert.fail("Add to Cart button not found");
		}
	}

	/**
	 * @Author: Rajat
	 * @Desc: Verify product is added to cart
	 * @return boolean
	 */

	public boolean isProductAddedToCart() {
		return isTextPresent(TextConstants.ADD_TO_CART_TEXT);
	}

	public String getSaree() {
		List<WebElement> sarees = getSareeList();

		if (sarees != null && !sarees.isEmpty()) {
			String sareeTitle = sarees.get(0).getText();
			sarees.get(0).click();
			return sareeTitle;
		}

		return null;
	}

}
