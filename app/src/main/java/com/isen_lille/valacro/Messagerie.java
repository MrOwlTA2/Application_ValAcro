package com.isen_lille.valacro;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe sur la messagerie
 */
public class Messagerie extends Fragment {

    /**
     * Layout
     */
    View v;

    /**
     * Nouvel instance de la base de donnée Firebase
     */
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    /**
     * Référence à la base de donnée
     */
    DatabaseReference mDatabaseRef;

    /**
     * Bouton
     */
    Button button;

    /**
     * EditText pour le sms et l'objet
     */
    EditText smsText, et_subject;

    // Listes de chaque groupe
    /**
     * Liste des groupes
     */
    List<String> listeGroupes;

    /**
     * Liste des téléphones pour le babygym1
     */
    List<String> telephoneBabyGym1;

    /**
     * Liste des mails pour le babygym1
     */
    List<String> mailBabyGym1;

    /**
     * Liste des téléphones pour le babygym2
     */
    List<String> telephoneBabyGym2;

    /**
     * Liste des mails pour le babygym2
     */
    List<String> mailBabyGym2;

    /**
     * Liste des téléphones pour le federal A
     */
    List<String> telephoneFederalA;

    /**
     * Liste des mails pour le federal A
     */
    List<String> mailFederalA;

    /**
     * Liste des téléphones pour le federal B débutant
     */
    List<String> telephoneFederalBDeb;

    /**
     * Liste des mail pour le federal B débutant
     */
    List<String> mailFederalBDeb;

    /**
     * Liste des téléphones pour le federal B confirmé
     */
    List<String> telephoneFederalBConf;

    /**
     * Liste des mails pour le federal B confirmé
     */
    List<String> mailFederalBConf;

    /**
     * Liste des téléphones pour l'école acro
     */
    List<String> telephoneEcoleAcro;

    /**
     * Liste des mails pour l'école acro
     */
    List<String> mailEcoleAcro;

    /**
     * Liste des téléphones pour les national
     */
    List<String> telephoneNational;

    /**
     * Liste des mails pour les national
     */
    List<String> mailNational;

    // Liste "globale" des personnes concernées
    /**
     * Liste global des mails
     */
    List<String> listeMails;

    /**
     * Liste global des mails
     */
    List<String> listeTel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_messagerie, container, false);

        // Demande d'autorisation d'envoyer des sms
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        } else {
            // On ne fait rien, on a les autorisations
        }

        initVariables();

        initNomGroupes();

        initMailTelephone();

        initClickListener();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Messagerie");
    }


    /**
     * Associe au bouton d'envoi les commandes à suivre quand on clique
     */
    public void initClickListener() {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String sms = smsText.getText().toString();
                String subject = et_subject.getText().toString();

                final CheckBox boxBaby1 = (CheckBox) v.findViewById(R.id.boxBaby1);
                if (boxBaby1.isChecked()) {

                    for (String mail : mailBabyGym1) listeMails.add(mail);
                    for (String tel : telephoneBabyGym1) listeTel.add(tel);

                }
                final CheckBox boxBaby2 = (CheckBox) v.findViewById(R.id.boxBaby2);
                if (boxBaby2.isChecked()) {

                    for (String mail : mailBabyGym2) listeMails.add(mail);
                    for (String tel : telephoneBabyGym2) listeTel.add(tel);

                }
                final CheckBox boxEcole = (CheckBox) v.findViewById(R.id.boxEcole);
                if (boxEcole.isChecked()) {

                    for (String mail : mailEcoleAcro) listeMails.add(mail);
                    for (String tel : telephoneEcoleAcro) listeTel.add(tel);

                }
                final CheckBox boxfedBb = (CheckBox) v.findViewById(R.id.boxFedBdebutant);
                if (boxfedBb.isChecked()) {

                    for (String mail : mailFederalBDeb) listeMails.add(mail);
                    for (String tel : telephoneFederalBDeb) listeTel.add(tel);
                }
                final CheckBox boxfedBc = (CheckBox) v.findViewById(R.id.boxFedBconfirme);
                if (boxfedBc.isChecked()) {

                    for (String mail : mailFederalBConf) listeMails.add(mail);
                    for (String tel : telephoneFederalBConf) listeTel.add(tel);

                }
                final CheckBox boxFedA = (CheckBox) v.findViewById(R.id.boxFedA);
                if (boxFedA.isChecked()) {

                    for (String mail : mailFederalA) listeMails.add(mail);
                    for (String tel : telephoneFederalA) listeTel.add(tel);

                }
                final CheckBox boxNational = (CheckBox) v.findViewById(R.id.boxNational);
                if (boxNational.isChecked()) {

                    for (String mail : mailNational) listeMails.add(mail);
                    for (String tel : telephoneNational) listeTel.add(tel);
                }

                try {

                    SmsManager smsManager = SmsManager.getDefault();

                    for (String test : listeTel) {

                        smsManager.sendTextMessage(test, null, sms, null, null);
                    }

                    Toast.makeText(getActivity(), "Sent !", Toast.LENGTH_SHORT).show();
                    smsText.setText("");
                    et_subject.setText("");


                    String[] listeEnvoi = listeMails.toArray(new String[listeMails.size()]);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, listeEnvoi);
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, sms);

                    intent.setType("message/rfc822");

                    startActivity(Intent.createChooser(intent, "Select email app"));

                } catch (Exception e) {

                    Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_SHORT).show();
                    et_subject.setText("");
                    smsText.setText("");
                }
            }
        });
    }

    /**
     * Remplit chaque liste associée à un groupe avec les informations nécessaires
     */
    public void initMailTelephone() {

        mDatabaseRef = mDatabase.getReference("table_valacro").child("table_parent");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot parent : dataSnapshot.getChildren()) {
                    // On parcourt les parents existants
                    String email = parent.child("informations").child("adresseMail").getValue().toString();
                    String telephone = parent.child("informations").child("numtel").getValue().toString();
                    for (DataSnapshot child : parent.getChildren()) {
                        if (child.getKey().contentEquals("informations")) ;
                        else {
                            Enfant enfant = child.getValue(Enfant.class);

                            if (enfant.getGroupe().contentEquals("Baby Gym 1") && (!mailBabyGym1.contains(email)) && (!telephoneBabyGym1.contains(email))) {
                                mailBabyGym1.add(email);
                                telephoneBabyGym1.add(telephone);
                            } else if (enfant.getGroupe().contentEquals("Baby Gym 2") && (!mailBabyGym2.contains(email)) && (!telephoneBabyGym2.contains(email))) {
                                mailBabyGym1.add(email);
                                telephoneBabyGym1.add(telephone);
                            } else if (enfant.getGroupe().contentEquals("Fédéral A") && (!mailFederalA.contains(email)) && (!telephoneFederalA.contains(email))) {
                                mailBabyGym1.add(email);
                                telephoneBabyGym1.add(telephone);
                            } else if (enfant.getGroupe().contentEquals("Fédéral B Confirmés") && (!mailFederalBConf.contains(email)) && (!telephoneFederalBConf.contains(email))) {
                                mailBabyGym1.add(email);
                                telephoneBabyGym1.add(telephone);
                            } else if (enfant.getGroupe().contentEquals("Fédéral B Débutants") && (!mailFederalBDeb.contains(email)) && (!telephoneFederalBDeb.contains(email))) {
                                mailBabyGym1.add(email);
                                telephoneBabyGym1.add(telephone);
                            } else if (enfant.getGroupe().contentEquals("Ecole d\'Acro") && (!mailEcoleAcro.contains(email)) && (!telephoneEcoleAcro.contains(email))) {
                                mailBabyGym1.add(email);
                                telephoneBabyGym1.add(telephone);
                            } else if (enfant.getGroupe().contentEquals("National") && (!mailNational.contains(email)) && (!telephoneNational.contains(email))) {
                                mailBabyGym1.add(email);
                                telephoneBabyGym1.add(telephone);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Initialise nos listes et associe les élements visuels à une variable
     */
    public void initVariables() {
        listeGroupes = new ArrayList<>();

        mailBabyGym1 = new ArrayList<>();
        telephoneBabyGym1 = new ArrayList<>();
        mailBabyGym2 = new ArrayList<>();
        telephoneBabyGym2 = new ArrayList<>();
        mailEcoleAcro = new ArrayList<>();
        telephoneEcoleAcro = new ArrayList<>();
        mailFederalA = new ArrayList<>();
        telephoneFederalA = new ArrayList<>();
        mailFederalBConf = new ArrayList<>();
        telephoneFederalBConf = new ArrayList<>();
        mailFederalBDeb = new ArrayList<>();
        telephoneFederalBDeb = new ArrayList<>();
        mailNational = new ArrayList<>();
        telephoneNational = new ArrayList<>();

        listeTel = new ArrayList<>();
        listeMails = new ArrayList<>();

        et_subject = (EditText) v.findViewById(R.id.et_subject);
        smsText = (EditText) v.findViewById(R.id.editText2);
        button = (Button) v.findViewById(R.id.button);
    }

    /**
     * Récupère la liste des groupes existans
     */
    public void initNomGroupes() {
        final DatabaseReference groupes = mDatabase.getReference("table_valacro").child("table_groupe");
        groupes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot grp : dataSnapshot.getChildren()) {
                    // On va parcourir la liste de groupes existants
                    listeGroupes.add(grp.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "permission granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
