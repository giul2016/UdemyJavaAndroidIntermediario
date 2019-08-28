package com.devmasterteam.convidados.entity;

public class GuestEntity {

    private int mId;
    private String mName;
    private int mConfirmed;

    public int getConfirmed() {
        return mConfirmed;
    }

    public void setConfirmed(int confirmed) {
        this.mConfirmed = confirmed;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}
