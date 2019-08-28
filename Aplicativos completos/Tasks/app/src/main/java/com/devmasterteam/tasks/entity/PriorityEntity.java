package com.devmasterteam.tasks.entity;

import com.google.gson.annotations.SerializedName;

public class PriorityEntity {

    @SerializedName("Id")
    private int id;

    @SerializedName("Description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int mId) {
        this.id = mId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String mDescription) {
        this.description = mDescription;
    }
}
