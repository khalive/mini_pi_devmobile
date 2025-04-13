package com.example.darim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.darim.R;
import com.example.darim.models.User;
import com.example.darim.utils.DatabaseHelper;
import com.example.darim.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Utilisez un layout simplifié sans lien vers Register

        // Initialisation
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Création des utilisateurs par défaut
        dbHelper.createDefaultUsers();

        // Si déjà connecté, redirection immédiate
        if (session.isLoggedIn()) {
            redirectToAppropriateInterface();
            return;
        }

        // Liaison des vues
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Gestion de la connexion
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInputs(username, password)) {
            return;
        }

        User user = dbHelper.authenticateUser(username, password);
        if (user != null) {
            startUserSession(user);
        } else {
            showError("Identifiants incorrects");
        }
    }

    private boolean validateInputs(String username, String password) {
        if (username.isEmpty()) {
            etUsername.setError("Nom d'utilisateur requis");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Mot de passe requis");
            return false;
        }
        return true;
    }

    private void startUserSession(User user) {
        session.createLoginSession(
                user.getId(),
                user.getUsername(),
                user.getType(),
                user.getFullName()
        );

        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(this::redirectToAppropriateInterface, 1000);
    }

    private void redirectToAppropriateInterface() {
        Intent intent;
        String userType = session.getUserType();

        if ("admin".equals(userType)) {
            intent = new Intent(this, AdminActivity.class);
        } else {
            intent = new Intent(this, UserActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}