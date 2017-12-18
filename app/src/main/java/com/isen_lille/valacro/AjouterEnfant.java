package com.isen_lille.valacro;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activité permettant d'ajouter un enfant
 */
public class AjouterEnfant extends Fragment {
    /**
     * Firebase
     */
    FirebaseDatabase database;

    /**
     * Référence à la base de donnée pour accéder aux infos parent
     */
    private DatabaseReference mDatabaseParent;

    /**
     * Référence à la base de donnée
     */
    private DatabaseReference mDatabase;

    /**
     * Récupere l'id de l'utilisateur connecté
     */
    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    /**
     * Layout
     */
    View v;

    /**
     * numéro de l'enfant
     */
    String child;

    /**
     * maximum d'enfants
     */
    boolean maxEnfants;

    /**
     * Entiers pour la date
     */
    int year, month, day;

    /**
     * Liste des partenaires
     */
    private final List<String> partenaires = new ArrayList<String>();

    /**
     * Certificat médical
     */
    private String certifMed;

    /**
     * AccordParental
     */
    private String accordParental;

    /**
     * Droit à l'image
     */
    private String droitImage;

    /**
     * Formation
     */
    private String formation;

    /**
     * Groupe
     */
    private String groupe;

    /**
     * Checkbox
     */
    private CheckBox checkBox;

    /**
     * EditText nom prénom date de naissance
     */
    EditText prenom, nom, dateNaissance;

    /**
     * Bouton valider
     */
    Button buttonValider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ajouter_enfant, container, false);
        // Inflate the layout for this fragment

        InitDatabase();
        initVariables();


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Formulaire d'inscription");
    }

    /**
     * Initialisation pour les fonctions faites sur la base de donnée
     */
    private void InitDatabase(){
        database = FirebaseDatabase.getInstance();
        mDatabaseParent = database.getReference("table_valacro").child("table_parent").child(currentUserID).child("informations");
        mDatabase = database.getReference("table_valacro").child("table_parent").child(currentUserID);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 1;
                while (dataSnapshot.hasChild("enfant"+Integer.toString(i)) && (i <= 10)){
                    i++;
                }
                if(i > 10){
                    maxEnfants = true;

                } else{
                    child = "enfant"+Integer.toString(i);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Initialisation pour les variables
     */
    private void initVariables(){
        accordParental = "";
        certifMed = "";
        droitImage = "";
        formation = "";
        groupe="";
        partenaires.add("partenaire A");
        partenaires.add("partenaire B");
        prenom = (EditText) v.findViewById(R.id.editTextIncriptionPrenom);
        nom = (EditText) v.findViewById(R.id.editTextIncriptionNom);
        dateNaissance = (EditText) v.findViewById(R.id.editTextIncriptionDateNaissance);
        checkBox = (CheckBox) v.findViewById(R.id.checkBoxAutorisationParentale);
        buttonValider = (Button) v.findViewById(R.id.button_validation_inscription);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!maxEnfants){
                    creerEnfant();
                } else Toast.makeText(getContext(), "Nom maximal d'adhérents atteint!", Toast.LENGTH_SHORT).show();
            }
        });



        dateNaissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateNaissance.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();

            }
        });
    }

    /**
     * On envoie l'ordre de création à Firebase
     */
    public void creerEnfant(){
        if(!checkBox.isChecked()) Toast.makeText(getContext(), "Veuillez lire et accepter les conditions d'inscription", Toast.LENGTH_LONG).show();
        else if (!dateNaissance.getText().toString().isEmpty() &&
                !prenom.getText().toString().trim().isEmpty() &&
                !nom.getText().toString().trim().isEmpty()){
            mDatabase.child(child).setValue(new Enfant(prenom.getText().toString(),
                    nom.getText().toString(),
                    dateNaissance.getText().toString(),
                    partenaires,
                    accordParental,
                    certifMed,
                    droitImage,
                    groupe,
                    formation));
            child = null;
            getActivity().onBackPressed();
        }
        else {
            Toast.makeText(getContext(), "Nom invalide", Toast.LENGTH_SHORT).show();
            child = null;
        }
    }

}

