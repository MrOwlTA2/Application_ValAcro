package com.isen_lille.valacro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité permettant à un utilisateur de s'enregistrer
 *
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * EditText pour le nom, mail, mot de passe et confirmation de mot de passe
     */
    protected EditText userName, userMail, userPassword, userConfirmPassword;

    /**
     * Bouton pour s'enregister
     */
    protected Button register;

    /**
     * Valeur suivant le statut
     */
    public int toggle_val;

    /**
     * Référence à la base de donnée
     */
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference("table_valacro").child("table_parent");
        initializeButtons();
        initializeEditText();
    }


    /**
     * Méthode d'initialisation des boutons
     *
     * @see FirebaseAuth
     */
    private void initializeButtons() {
        register = (Button) findViewById(R.id.button_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enregistrement de l'utilisateur via FirebaseAuth
                if (TextUtils.isEmpty(userMail.getText().toString()) || TextUtils.isEmpty(userPassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Les champs sont vides.", Toast.LENGTH_LONG).show();
                }
                else if(!isInputEditTextMatches(userPassword,userConfirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Mots de passe différents.", Toast.LENGTH_LONG).show();
                }
                else{
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(userMail.getText().toString(), userPassword.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Champs mal remplis.", Toast.LENGTH_LONG).show();
                                    } else{
                                        onAuthSuccess(FirebaseAuth.getInstance().getCurrentUser());
                                    }

                                    /*else {
                                        // On crée également une requête pour mettre à jour le nom de l'utilisateur (qui par défaut a la valeur null)
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userName.getText().toString())
                                                .build();
                                        // Puis on met à jour ses données. Si les données sont bien mises à jour, on redirigera l'utilisateur vers
                                        // l'accueil de l'application.
                                        task.getResult().getUser().updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            finish();
                                                            onAuthSuccess(FirebaseAuth.getInstance().getCurrentUser());
                                                        }
                                                    }
                                                });
                                    }*/
                                }
                            });
                }
            }
        });
    }

    /**
     * Se lance quand on a bien crée le compte. Confirme la création du compte via un Toast
     * @param user Utilisateur de la base de donnée
     */
    private void onAuthSuccess(FirebaseUser user) {
        //String username = usernameFromEmail(user.getEmail());

        // Write new user
        String flag = "4";
        writeNewUser(user.getUid(), flag );
        Toast.makeText(this, "Le compte " + user.getEmail() + " a bien été créé", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Méthode qui écrit l'id de l'utilisateur et son flag dans la base de donnée
     * @param userId id de l'utilisateur
     * @param flag flag que l'on veut attribuer à l'utilisateur
     */
    private void writeNewUser(String userId, String flag) {
        mDatabase.child(userId).child("informations").child("flag").setValue(flag);
    }

    /**
     * Méthode qui vérifie si le mot de passe et sa confirmation sont identiques
     * @param EditText1 EditText du mot de passe
     * @param EditText2 EditText de la confirmation de mot de passe
     * @return vrai ou faux suivant si le mot de passe et sa confirmation sont identiques
     */
    public boolean isInputEditTextMatches(EditText EditText1, EditText EditText2){
        String value1 = EditText1.getText().toString();
        String value2 = EditText2.getText().toString();
        return value1.contentEquals(value2);
    }


    /**
     * Méthode d'initialisation des EditText
     */
    private void initializeEditText(){
        userMail = (EditText)findViewById(R.id.email);
        userPassword = (EditText)findViewById(R.id.password);
        userConfirmPassword = (EditText)findViewById(R.id.confirm_password);
    }

}
