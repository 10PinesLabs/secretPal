package com.tenPines.model;

import com.tenPines.errors.InvalidGiftDefaultException;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;

public class GiftTest {



    @Test
    public void when_i_create_giftDefault_and_amount_Default_is_null(){
        try {
            DefaultGift.createGiftDfault("Libro", "");
            fail("Should be failed");
        }catch (InvalidGiftDefaultException exception){
            assertThat(exception.getMessage()).isEqualTo(InvalidGiftDefaultException.HAVE_NOT_AMOUNT_DEFAULT);
        }

    }

    @Test
    public void when_i_create_giftDefault_and_gift_Default_is_null(){
        try {
            DefaultGift.createGiftDfault("", "300");
            fail("Should be failed");
        }catch (InvalidGiftDefaultException exception){
            assertThat(exception.getMessage()).isEqualTo(InvalidGiftDefaultException.HAVE_NOT_GIFT_DEFAULT);
        }

    }

    @Test
    public void when_i_change_the_default_gift_and_it_change(){
        DefaultGift actualDefaultGift = DefaultGift.createGiftDfault("Un libro", "300");
        DefaultGift newDefaultGift = DefaultGift.createGiftDfault("Una taza", "300");
        actualDefaultGift.changeDefaultGift(newDefaultGift);
        assertThat(actualDefaultGift.getAmountDefault()).isEqualTo("300");
        assertEquals(actualDefaultGift.getGiftDefault(), "Una taza");
    }
}
