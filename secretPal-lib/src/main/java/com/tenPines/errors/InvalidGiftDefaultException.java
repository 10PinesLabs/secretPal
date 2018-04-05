package com.tenPines.errors;

/**
 * Created by Aye on 02/11/16.
 */
public class InvalidGiftDefaultException extends RuntimeException{


    public static String HAVE_NOT_AMOUNT_DEFAULT = "Do not create Gift Default without amount default";
    public static String HAVE_NOT_GIFT_DEFAULT = "Do not create gift default without gift";
    private String error;

    public InvalidGiftDefaultException(String error) {
        this.error = error;
    }

    public String  getMessage(){
        return error;
    }

}
