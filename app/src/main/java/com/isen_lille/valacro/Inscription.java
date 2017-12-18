package com.isen_lille.valacro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Activité permettant l'inscription du parent, obligatoire
 */
public class Inscription extends AppCompatActivity {

    /**
     * Référence à la base de donnée
     */
    DatabaseReference mDatabaseRef;

    /**
     * Base de donnée firebase
     */
    FirebaseDatabase mDatabase;

    /**
     * id de l'utilisateur connecté
     */
    String userID;

    /**
     * Nom
     */
    EditText nomBox;

    /**
     * Prenom
     */
    EditText prenomBox;

    /**
     * Mail
     */
    EditText mailBox;

    /**
     * Téléphone
     */
    EditText telBox;

    /**
     * Adresse postal
     */
    EditText adresseBox;

    /**
     * Code postal
     */
    EditText codepostalBox;

    /**
     * Nom de la ville
     */
    EditText villeBox;

    /**
     * Bouton de validation du formulaire
     */
    Button buttonValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        initVariables();
        initDatabase();
        initClickListener();
    }

    /**
     * Initialise les variables du layout
     */
    public void initVariables(){
        nomBox = (EditText) findViewById(R.id.editTextIncriptionNom);
        prenomBox = (EditText) findViewById(R.id.editTextIncriptionPrenom);
        mailBox = (EditText) findViewById(R.id.editTextIncriptionMail);
        mailBox.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        telBox = (EditText) findViewById(R.id.editTextIncriptionTelephone);
        adresseBox = (EditText) findViewById(R.id.editTextIncriptionAdresse);
        codepostalBox = (EditText) findViewById(R.id.editTextIncriptionCodePostal);
        villeBox = (EditText) findViewById(R.id.editTextIncriptionVille);
        buttonValidation = (Button) findViewById(R.id.button_validation_inscription);
    }

    /**
     * Initialise les variables de Firebase
     */
    public void initDatabase(){
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("table_valacro").child("table_parent").child(userID).child("informations");
    }

    /**
     * Associe au bouton les actions à éxecuter quand il est cliqué
     */
    public void initClickListener(){
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nomBox.getText().toString().isEmpty() || prenomBox.getText().toString().isEmpty()
                        || mailBox.getText().toString().isEmpty() || telBox.getText().toString().isEmpty()
                        || adresseBox.getText().toString().isEmpty() || codepostalBox.getText().toString().isEmpty()
                        || villeBox.getText().toString().isEmpty()) {
                    Toast.makeText(Inscription.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else{
                    Parent parent = new Parent(nomBox.getText().toString().trim(), prenomBox.getText().toString().trim(), mailBox.getText().toString().trim(), adresseBox.getText().toString().trim(),
                            codepostalBox.getText().toString().trim(), villeBox.getText().toString().trim(), telBox.getText().toString().trim(), "4");
                    mDatabaseRef.setValue(parent);
                    finish();
                }
            }
        });
    }
}
