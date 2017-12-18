package com.isen_lille.valacro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui va completer le parent
 */
public class ListeParents extends Fragment {

    /**
     * Base de donnée Firebase
     */
    FirebaseDatabase database;

    /**
     * Référence à la base de donnée
     */
    private DatabaseReference mDatabaseTest;

    /**
     * Adapter objet de liste enfant
     */
    ListeEnfants adapter;

    /**
     * Layout
     */
    View v;

    // Variables des parents
    /**
     * ListView d'une liste
     */
    ListView list;

    /**
     * Liste du prénom des parents
     */
    List<String> prenomParents = new ArrayList<>();

    /**
     * Liste du nom des parents
     */
    List<String> nomParents = new ArrayList<>();

    /**
     * Id des images
     */
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
        v = inflater.inflate(R.layout.fragment_liste_parents, container, false);

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
                    prenomParents.clear();
                    nomParents.clear();
                    adapter = new ListeEnfants(getActivity(), prenomParents, nomParents, imageId);
                    for(DataSnapshot c : dataSnapshot.getChildren()){
                        for(DataSnapshot child : c.getChildren()){
                            String nom = child.getKey();
                            if(nom.contentEquals("informations")){
                                Parent parent = child.getValue(Parent.class);
                                prenomParents.add(parent.getPrenom());
                                nomParents.add(parent.getNom());
                            } else {
                                // On ne fait rien
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
