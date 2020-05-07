package com.stevecrossin.grocerytracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShopFrequencyValidatorTest {

    @Test
    public void isShopFrequencyValid_ValidShopFrequency_NullErrorReturned() {
        String actualError = InputValidator.isShopFrequencyValid("1");
        assertNull(actualError);
    }

    @Test
    public void isShopFrequencyValid_ShopFrequencyEmpty_ShopFrequencyRequiredErrorReturned() {
        String actualError = InputValidator.isShopFrequencyValid("");
        assertEquals(InputValidator.EMPTY_SHOP_FREQUENCY_ERROR,actualError);
    }
}