package com.devmasterteam.convidados.entity;

public class GuestCountEntity {

    private int mTotalGuests;
    private int mTotalPresent;
    private int mTotalAbsent;

    public GuestCountEntity(int totalGuests, int totalPresent, int totalAbsent) {
        this.mTotalGuests = totalGuests;
        this.mTotalPresent = totalPresent;
        this.mTotalAbsent = totalAbsent;
    }

    public int getTotalGuests() {
        return mTotalGuests;
    }

    public void setTotalGuests(int totalGuests) {
        this.mTotalGuests = totalGuests;
    }

    public int getTotalPresent() {
        return mTotalPresent;
    }

    public void setTotalPresent(int totalPresent) {
        this.mTotalPresent = totalPresent;
    }

    public int getTotalAbsent() {
        return mTotalAbsent;
    }

    public void setTotalAbsent(int totalAbsent) {
        this.mTotalAbsent = totalAbsent;
    }

}
