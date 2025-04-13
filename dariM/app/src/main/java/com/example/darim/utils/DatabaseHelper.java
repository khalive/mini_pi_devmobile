package com.example.darim.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.darim.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Version de la base de données
    private static final int DATABASE_VERSION = 1;

    // Nom de la base de données
    private static final String DATABASE_NAME = "DariMauritanie.db";

    // Nom de la table utilisateurs
    private static final String TABLE_USERS = "users";

    // Colonnes de la table utilisateurs
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_EMAIL = "email";

    // Requête de création de table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_FULLNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création des tables
        db.execSQL(CREATE_USERS_TABLE);

        // Insertion des utilisateurs par défaut
        createDefaultUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprime les tables existantes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Recrée les tables
        onCreate(db);
    }

    /**
     * Crée les utilisateurs par défaut
     */
    public void createDefaultUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        createDefaultUsers(db);
    }

    private void createDefaultUsers(SQLiteDatabase db) {
        // Vider la table existante
        db.execSQL("DELETE FROM " + TABLE_USERS);

        // Ajouter les utilisateurs spécifiques
        addUser(db, "khalive", "123456", "admin", "Khalive Admin", "khalive@admin.com");
        addUser(db, "sidati", "1234", "user", "Sidati User", "sidati@user.com");
        addUser(db, "abad", "12345", "user", "Abad User", "abad@user.com");
    }

    /**
     * Ajoute un utilisateur à la base de données
     */
    private long addUser(SQLiteDatabase db, String username, String password, String type,
                         String fullName, String email) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password); // Note: En production, hasher le mot de passe
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_FULLNAME, fullName);
        values.put(COLUMN_EMAIL, email);

        return db.insert(TABLE_USERS, null, values);
    }

    /**
     * Authentifie un utilisateur
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @return User objet utilisateur ou null
     */
    public User authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_ID,
                COLUMN_USERNAME,
                COLUMN_TYPE,
                COLUMN_FULLNAME,
                COLUMN_EMAIL
        };

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
            int fullnameIndex = cursor.getColumnIndex(COLUMN_FULLNAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);

            if (idIndex != -1 && usernameIndex != -1 && typeIndex != -1 && fullnameIndex != -1 && emailIndex != -1) {
                User user = new User(
                        cursor.getInt(idIndex),
                        cursor.getString(usernameIndex),
                        cursor.getString(typeIndex),
                        cursor.getString(fullnameIndex),
                        cursor.getString(emailIndex)
                );
                cursor.close();
                return user;
            }
            cursor.close();
        }

        return null;
    }

    /**
     * Vérifie si un utilisateur existe déjà
     * @param username Nom d'utilisateur
     * @return boolean
     */
    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }
}