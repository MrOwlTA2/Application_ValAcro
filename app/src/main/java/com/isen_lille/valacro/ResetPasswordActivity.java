package com.isen_lille.valacro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité permettant à un utilisateur de recevoir un mail de reset mot de passe
 *
 */
public class ResetPasswordActivity extends AppCompatActivity {

    /**
     * EditText pour l'entrée du mail
     */
    private EditText inputEmail;

    /**
     * Bouton de validation du reset de mot de passe
     */
    private Button btnReset;

    /**
     * Nouvelle instance de la base d'authentification de Firebase
     */
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.email);

        auth = FirebaseAuth.getInstance();

        initializeButton();

    }

    /**
     * Méthode d'initialisation des boutons
     */
    private void initializeButton(){
        //Bouton d'ajout des événements
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Entrez votre email d'identification", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "Nous vous avons envoyé les instructions pour réinitialiser votre mot de passe!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Nous n'avons pas pu vous envoyer d'email!", Toast.LENGTH_SHORT).show();
                                }
                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                            }
                        });
            }
        });
    }

}