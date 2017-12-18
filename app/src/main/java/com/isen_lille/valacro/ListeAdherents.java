package com.isen_lille.valacro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListeAdherents extends Fragment {

    // Variables de Firebase
    FirebaseDatabase database;
    private DatabaseReference mDatabaseTest;

    // Objet servant à remplit la liste
    ListeEnfants adapter;

    // Layout
    View v;

    // Variables de l'enfant
    ListView list;
    List<String> prenomEnfants = new ArrayList<>();
    List<String> nomEnfants = new ArrayList<>();
    List<String> groupesEnfants = new ArrayList<>();
    Integer[] imageId = {
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro,
            R.drawable.logo_valacro

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_liste_adherents, container, false);

        InitDatabase();
        list=(ListView) v.findViewById(R.id.list);

        return v;
    }

    /**
     * Initialisation de la base Firebase
     * Récupère dans une liste l'intégralité des adhérents pour les afficher par la suite
     */
    private void InitDatabase() {
        database = FirebaseDatabase.getInstance();
        mDatabaseTest = database.getReference("table_valacro").child("table_parent");
        mDatabaseTest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getActivity() != null){
                    prenomEnfants.clear();
                    nomEnfants.clear();
                    groupesEnfants.clear();
                    adapter = new ListeEnfants(getActivity(), prenomEnfants, nomEnfants, imageId);
                    for(DataSnapshot c : dataSnapshot.getChildren()){
                        String UserID = dataSnapshot.getKey();
                        for(DataSnapshot child : c.getChildren()){
                            String nom = child.getKey();
                            if(nom.contentEquals("informations")){
                                // On ne fait rien, on veut pas les infos du parent
                            } else {
                                Enfant enfant = child.getValue(Enfant.class);
                                prenomEnfants.add(enfant.getPrenom());
                                nomEnfants.add(enfant.getNom());
                                groupesEnfants.add(enfant.getGroupe());
                            }
                        }
                    }
                }
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
