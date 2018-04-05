package com.tenPines.model;

import com.tenPines.errors.InvalidGiftDefaultException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Entity
public class DefaultGift {

    @Id
    @GeneratedValue
    private Long id;

    public DefaultGift() {
        }

    static public DefaultGift createGiftDfault(String giftDefault, String amountDefault){
        DefaultGift aDefaultGift = new DefaultGift();
        aDefaultGift.setAmountDefault(amountDefault);
        aDefaultGift.setGiftDefault(giftDefault);
        aDefaultGift.validateADefaultGift();
        return aDefaultGift;
    }

    public void validateADefaultGift() {
        if (this.getAmountDefault().isEmpty()){
            throw new InvalidGiftDefaultException(InvalidGiftDefaultException.HAVE_NOT_AMOUNT_DEFAULT);
        }
        if (this.getGiftDefault().isEmpty()){
            throw new InvalidGiftDefaultException(InvalidGiftDefaultException.HAVE_NOT_GIFT_DEFAULT);
        }
    }

    @NotNull
    public String giftDefault;

    @NotNull
    public String amountDefault;


    public String getAmountDefault() {
        return amountDefault;
    }

    public void setAmountDefault(String password) {
        this.amountDefault = password;
    }

    public String getGiftDefault() {
        return giftDefault;
    }

    public void setGiftDefault(String giftDefault) {
        this.giftDefault = giftDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void changeDefaultGift(DefaultGift defaultGift) {
        this.setAmountDefault(defaultGift.getAmountDefault());
        this.setGiftDefault(defaultGift.getGiftDefault());
    }
}
