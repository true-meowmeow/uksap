package ru.meow.uksap.Java;

import android.content.Context;
import android.content.SharedPreferences;


class Preferences {


    private SharedPreferences sharedPreferences;
    Preferences(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(getSelectedGroupString(), Context.MODE_PRIVATE);
        load();
    }

    private int selectedGroutInt;
    private String selectedGroupString = "";

    void load() {
        if (sharedPreferences.contains(selectedGroupString)) {
            selectedGroutInt = sharedPreferences.getInt(selectedGroupString, 0);
        }
    }

    void save(int selectedGroup) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(selectedGroupString, selectedGroup);
        editor.apply();
    }

    public int getSelectedGroutInt() {
        return selectedGroutInt;
    }

    public String getSelectedGroupString() {
        return selectedGroupString;
    }
}
