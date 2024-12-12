package com.abhijeet.memorizeit;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Preference_Helper {

    private static final String PREFERENCES_FILE = "com.example.memorytilegame.sharedprefs";
    private static final String KEY_HIGHEST_SCORE_LEVEL_1 = "highest_score_level_1";
    private static final String KEY_HIGHEST_SCORE_LEVEL_2 = "highest_score_level_2";
    private static final String KEY_HIGHEST_SCORE_LEVEL_3 = "highest_score_level_3";

    private SharedPreferences sharedPreferences;

    public Shared_Preference_Helper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    // Save highest score for level 1
    public void saveHighestScoreLevel1(String score) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_HIGHEST_SCORE_LEVEL_1, score);
        editor.apply();
    }

    // Retrieve highest score for level 1
    public String getHighestScoreLevel1() {
        return sharedPreferences.getString(KEY_HIGHEST_SCORE_LEVEL_1, null);
    }

    // Similarly, implement methods for level 2 and level 3
    // Save highest score for level 2
    public void saveHighestScoreLevel2(String score) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_HIGHEST_SCORE_LEVEL_2, score);
        editor.apply();
    }

    // Retrieve highest score for level 2
    public String getHighestScoreLevel2() {
        return sharedPreferences.getString(KEY_HIGHEST_SCORE_LEVEL_2, null);
    }

    // Save highest score for level 3
    public void saveHighestScoreLevel3(String score) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_HIGHEST_SCORE_LEVEL_3, score);
        editor.apply();
    }

    // Retrieve highest score for level 3
    public String getHighestScoreLevel3() {
        return sharedPreferences.getString(KEY_HIGHEST_SCORE_LEVEL_3, null);
    }

}
