package com.tenPines.model;

public class ParticipationConfig {

    private Boolean wantsToGive;
    private Boolean wantsToReceive;
    private Boolean wantsToReceiveMail;

    public ParticipationConfig(Boolean wantsToGiveGift, Boolean wantsToReceiveGift, Boolean wantsToReceiveBirthdayMessage) {
        wantsToGive = wantsToGiveGift;
        wantsToReceive = wantsToReceiveGift;
        wantsToReceiveMail = wantsToReceiveBirthdayMessage;
    }
}
