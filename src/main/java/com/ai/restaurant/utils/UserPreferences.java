package com.ai.restaurant.utils;

import java.util.prefs.Preferences;

public class UserPreferences {
    private static final Preferences prefs = Preferences.userRoot().node("AI_Restaurant_Manager");

    public static boolean isDarkModeEnabled() {
        return prefs.getBoolean("darkMode", false);
    }

    public static void saveDarkModePreference(boolean enabled) {
        prefs.putBoolean("darkMode", enabled);
    }
}
