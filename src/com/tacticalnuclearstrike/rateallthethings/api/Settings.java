package com.tacticalnuclearstrike.rateallthethings.api;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.inject.Inject;

public class Settings implements ISettings {

    final String TAG = "RateAllTheThings";

    private Context context;

    @Inject
    public Settings(Context context){
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return this.context.getSharedPreferences(TAG, 0);
    }

    public String getEmail(){
        return this.getSharedPreferences().getString("EMAIL", "");
    }

    public String getPassword() {
        return this.getSharedPreferences().getString("PASSWORD", "");
    }

    public String getTag() {
        return TAG;
    }

    public void setEmail(String email) {
        this.saveStringSetting("EMAIL", email);
    }

    public void setPassword(String password) {
        this.saveStringSetting("PASSWORD", password);
    }

    private void saveStringSetting(String name, String password) {
        SharedPreferences prefs = this.getSharedPreferences();
        SharedPreferences.Editor editor= prefs.edit();
        editor.putString(name, password);
        editor.commit();
    }
}
