package com.nairaland.snakevpn;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSharedPreference {

    Context context;
    SharedPreferences sharedPreferences;

    public SaveSharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("key", Context.MODE_PRIVATE);
    }

    public void setState(Boolean bo){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("editor", bo);
        editor.apply();
    }

    public boolean getState(){
        return sharedPreferences.getBoolean("editor", false);
    }
}
