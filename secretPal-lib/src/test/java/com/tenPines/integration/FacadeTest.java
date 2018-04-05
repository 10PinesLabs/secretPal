package com.tenPines.integration;


import com.tenPines.application.SystemPalFacade;
import com.tenPines.model.DefaultGift;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FacadeTest extends SpringBaseTest {

    @Autowired
    SystemPalFacade facade;

    @Test
    public void when_i_get_all_gift_defaults_and_not_is_empty_because_always_create_default_gift(){
        assertThat(facade.retrieveAllGiftsDefaults(), not(empty()));
    }

    @Test
    public void when_i_get_all_gift_defaults_and_have_gift(){
        DefaultGift defaultGift = DefaultGift.createGiftDfault("Mural", "$1000");
        facade.addGiftDefaults(defaultGift);
        assertThat(facade.retrieveAllGiftsDefaults(), not(empty()));
        assertEquals(facade.retrieveTheLastDefaultGift().getGiftDefault(),"Mural");
        assertEquals(facade.retrieveTheLastDefaultGift().getAmountDefault(),"$1000");

    }


}
