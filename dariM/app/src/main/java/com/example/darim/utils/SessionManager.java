package com.example.darim.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor pour modifier les préférences
    private SharedPreferences.Editor editor;

    // Contexte
    private Context context;

    // Mode partagé


    // Nom du fichier de préférences
    private static final String PREF_NAME = "DariMauritaniePref";

    // Clés pour stocker les valeurs
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_FULL_NAME = "fullName";

    // Constructeur
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Crée une session de connexion
     * @param id ID de l'utilisateur
     * @param username Nom d'utilisateur
     * @param type Type d'utilisateur (admin/user)
     * @param fullName Nom complet
     */
    public void createLoginSession(int id, String username, String type, String fullName) {
        // Stocker les valeurs de connexion
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_TYPE, type);
        editor.putString(KEY_FULL_NAME, fullName);

        // Commit les changements
        editor.commit();
    }

    /**
     * Vérifie si l'utilisateur est connecté
     * @return boolean État de connexion
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Récupère le type d'utilisateur
     * @return String Type d'utilisateur
     */
    public String getUserType() {
        return pref.getString(KEY_USER_TYPE, null);
    }

    /**
     * Récupère le nom d'utilisateur
     * @return String Nom d'utilisateur
     */
    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    /**
     * Récupère le nom complet
     * @return String Nom complet
     */
    public String getFullName() {
        return pref.getString(KEY_FULL_NAME, null);
    }

    /**
     * Déconnecte l'utilisateur
     */
    public void logoutUser() {
        // Efface toutes les données de la session
        editor.clear();
        editor.commit();
    }
}