package ru.meow.uksap.Java;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.List;


class Preferences {

    private Data data;

    private Gson gson;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    Preferences(Context context, Data data) {
        this.data = data;

        gson = new Gson();
        sharedPreferences = context.getApplicationContext().getSharedPreferences("preferencesUksap", Context.MODE_PRIVATE);

        loadGroup();
    }

    private int selectedGroutInt;
    private String selectedGroupString = "";

    void loadGroup() {
        if (sharedPreferences.contains(getSelectedGroupString())) {
            selectedGroutInt = sharedPreferences.getInt(getSelectedGroupString(), 0);
        }
    }


    void saveGroup(int selectedGroup) {
        editor = sharedPreferences.edit();
        editor.putInt(selectedGroupString, selectedGroup);
        editor.apply();
    }


    boolean loadData() {
        data.setCells((List<List<String>>) gson.fromJson(sharedPreferences.getString("data", null), new TypeToken<List<List<String>>>() {}.getType()));
        if (data.getCells() != null) return true;
        else return false;
    }

    void saveData(List<List<String>> data) {
        String json = gson.toJson(data);
        editor = sharedPreferences.edit();
        editor.putString("data", json);
        editor.apply();
    }


    public int getSelectedGroutInt() {
        return selectedGroutInt;
    }

    public String getSelectedGroupString() {
        return selectedGroupString;
    }
}
