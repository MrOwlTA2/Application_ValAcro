package com.isen_lille.valacro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité de Login
 *
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * EditText du mail et du mot de passe utilisateur
     */
    protected EditText userMail, userPassword;

    /**
     * Bouton de login
     */
    protected Button login;

    /**
     * TextView de creation d'un nouveau compte ou de reset de mot de passe
     */
    protected TextView createAccount, resetPassword;

    /**
     * Référence à la base de donnée
     */
    private DatabaseReference ref;

    /**
     * Référence à la base d'authentification Firebase
     */
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FirebaseAuth.getInstance().signOut();


        initializeFirebase();
        initializeButtons();
        initializeEditText();
        initializeTextViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Méthode d'initialisation de Firebase.
     * Ici, on redirige directement l'utilisateur vers l'accueil s'il est déjà connecté via FirebaseAuth.
     */
    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    /**
     * Méthode d'initialisation des boutons
     */
    private void initializeButtons() {
        login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = userMail.getText().toString();
                String password = userPassword.getText().toString();
                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Les champs sont vides.", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(mail, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Problème de connexion.", Toast.LENGTH_LONG).show();
                                    } else {
                                        onAuthSuccess(task.getResult().getUser());
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * Méthode se lançant quand la connexion à Firebase est réussie
     * Vérifie le flag (niveau d'authorité) de l'utilisateur et le redirige selon ce flag
     * @param user Utilisateur actuel
     */
    private void onAuthSuccess(FirebaseUser user) {
        if (user != null) {
            final DatabaseReference parent = FirebaseDatabase.getInstance().getReference("table_valacro").child("table_parent").child(user.getUid()).child("informations");
            ref = FirebaseDatabase.getInstance().getReference("table_valacro").child("table_parent").child(user.getUid()).child("informations").child("flag");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    //1 ADMIN
                    //2 COMITE
                    //3 COACH
                    //4 ADHERENT
                    // Pour chaque valeur de flag on va vérifier que l'utilisateur a bien finalisé son inscription (nom + prénom etc...)
                    if (Integer.parseInt(value) == 1) {
                        parent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("nom")) {
                                    // L'utilisateur a rentré ses infos
                                    startActivity(new Intent(LoginActivity.this, AdminAccueil.class));
                                    clearChamps();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, Inscription.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else if (Integer.parseInt(value) == 2) {
                        parent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("nom")) {
                                    // L'utilisateur a rentré ses infos
                                    startActivity(new Intent(LoginActivity.this, UserAccueil.class));
                                    clearChamps();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, Inscription.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else if (Integer.parseInt(value) == 3) {
                        parent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("nom")) {
                                    // L'utilisateur a rentré ses infos
                                    startActivity(new Intent(LoginActivity.this, UserAccueil.class));
                                    clearChamps();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, Inscription.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else if (Integer.parseInt(value) == 4) {
                        parent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("nom")) {
                                    // L'utilisateur a rentré ses infos
                                    startActivity(new Intent(LoginActivity.this, UserAccueil.class));
                                    clearChamps();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, Inscription.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    /**
     * Méthode d'initialisation des EditText
     */
    private void initializeEditText() {
        userMail = (EditText) findViewById(R.id.email);
        userPassword = (EditText) findViewById(R.id.password);
    }

    /**
     * Méthode d'initialisation des TextView.
     * Ici, j'ajoute un clickListener pour rediriger vers l'activité Register.
     */
    private void initializeTextViews() {
        createAccount = (TextView) findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        resetPassword = (TextView) findViewById(R.id.reset_password);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    /**
     * Nettoie les champs mail et password
     */
    private void clearChamps() {
        userMail.getText().clear();
        userPassword.getText().clear();
    }
}
