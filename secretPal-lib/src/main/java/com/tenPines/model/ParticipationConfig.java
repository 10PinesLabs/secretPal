package com.tenPines.model;

import javax.persistence.Embeddable;

@Embeddable
public class ParticipationConfig {

    private Boolean wantsToGive;
    private Boolean wantsToReceive;
    private Boolean wantsToReceiveMail;

    private ParticipationConfig(){}
    public ParticipationConfig(Boolean wantsToGiveGift, Boolean wantsToReceiveGift, Boolean wantsToReceiveBirthdayMessage) {
        wantsToGive = wantsToGiveGift;
        wantsToReceive = wantsToReceiveGift;
        wantsToReceiveMail = wantsToReceiveBirthdayMessage;
    }

    public void changeIntentionToGive() {
        wantsToGive = !wantsToGive;
    }

    public Boolean getWantsToGive() {
        return wantsToGive;
    }

    public void setWantsToGive(Boolean wantsToGive) {
        this.wantsToGive = wantsToGive;
    }

    public Boolean getWantsToReceive() {
        return wantsToReceive;
    }

    public void setWantsToReceive(Boolean wantsToReceive) {
        this.wantsToReceive = wantsToReceive;
    }

    public Boolean getWantsToReceiveMail() {
        return wantsToReceiveMail;
    }

    public void setWantsToReceiveMail(Boolean wantsToReceiveMail) {
        this.wantsToReceiveMail = wantsToReceiveMail;
    }

}
