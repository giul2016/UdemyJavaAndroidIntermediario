package com.devmasterteam.tasks.infra.security;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Acesso a dados rápidos do projeto - SharedPreferences
 */
public class SecurityPreferences {

    private final SharedPreferences mPreferences;

    public SecurityPreferences(Context context) {
        this.mPreferences = context.getSharedPreferences("UltraTask", Context.MODE_PRIVATE);
    }

    public void storeString(String key, String value) {
        this.mPreferences.edit().putString(key, value).apply();
    }

    public void removeStoredString(String key) {
        this.mPreferences.edit().remove(key).apply();
    }

    public String getStoredString(String key) {
        return this.mPreferences.getString(key, "");
    }

}
