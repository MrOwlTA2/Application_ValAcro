package com.isen_lille.valacro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Activité affichant les enfants de l'utilisateur
 */
public class AfficherEnfantsUser extends Fragment {

    final static String DATA_RECEIVE = "data_receive";

    /**
     * id de l'enfant
     */
    String idChild;

    /**
     * Firebase
     */
    FirebaseDatabase database;

    /**
     * Reference a la base de donnée
     */
    private DatabaseReference mDatabase;

    /**
     * Recupere l'id de l'utilisateur connecté
     */
    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    /**
     * TextView nom de l'enfant
     */
    TextView nom;

    /**
     * TextView prénom de l'enfant
     */
    TextView prenom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On ajoute le layout au fragment
        View v = inflater.inflate(R.layout.fragment_afficher_enfants_user, container, false);

        nom = (TextView) v.findViewById(R.id.textViewNom2);
        prenom = (TextView) v.findViewById(R.id.textViewPrenom2);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // On récupère les arguments envoyés pour afficher l'enfant
        Bundle args = getArguments();
        if (args != null) {
            idChild = args.getString(DATA_RECEIVE);
        }
        InitDatabase();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Initialisation de Firebase
     * On affiche les informations en temps réel
     */
    private void InitDatabase(){
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("table_valacro").child("table_parent").child(currentUserID).child(idChild);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Enfant enfant = dataSnapshot.getValue(Enfant.class);
                    nom.setText(enfant.getNom());
                    prenom.setText(enfant.getPrenom());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
