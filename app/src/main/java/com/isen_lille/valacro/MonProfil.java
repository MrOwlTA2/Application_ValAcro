package com.isen_lille.valacro;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Page mon profil. Affiche les enfants de l'utilisateur
 */
public class MonProfil extends Fragment {
    DataPassListener mCallback;

    /**
     * Interface pour envoyer des données
     */
    public interface DataPassListener{
        void envoyerDonnees(String data);
    }

    // Variables de Firebase
    /**
     * Base de donnée Firebase
     */
    FirebaseDatabase database;

    /**
     * Référence à la base de donnée
     */
    private DatabaseReference mDatabase;

    /**
     * Récupère l'id de l'utilisateur connecté
     */
    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    /**
     * Adapter objet de la liste enfant
     */
    ListeEnfants adapter;

    /**
     * Layout
     */
    View v;

    // Variables de l'enfant
    /**
     * Ajouter enfant
     */
    Button ajouterEnf;

    /**
     * ListView d'une liste
     */
    ListView list;

    /**
     * Liste de String des noms des enfants
     */
    List<String> nomEnfants = new ArrayList<>();

    /**
     * Liste de String des noms des groupes des enfants
     */
    List<String> groupesEnfants = new ArrayList<>();

    /**
     * Liste d'entier des id des images
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
        v = inflater.inflate(R.layout.fragment_mon_profil, container, false);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_mon_profil, container, false);

        InitDatabase();
        InitOnClickListener();

        list=(ListView) v.findViewById(R.id.list);
        //list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Nouveau fragment => détail enfants
                mCallback.envoyerDonnees("enfant"+Integer.toString(position+1));
                //Toast.makeText(getActivity(), "You Clicked at " +nomEnfants.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // On set le titre de la toolbar
        getActivity().setTitle("Mon Profil");
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            mCallback = (DataPassListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement DataPassListener");
        }
    }



    /**
     * Initialisation de la base Firebase
     * Récupère dans une liste l'intégralité des adhérents pour les afficher par la suite
     */
    private void InitDatabase() {
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("table_valacro").child("table_parent").child(currentUserID);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(getActivity() != null){
                    // On clear nomEnfants et groupesEnfants afin de ne pas empiler les nouveaux élements sur les anciens
                    nomEnfants.clear();
                    groupesEnfants.clear();
                    adapter = new
                            ListeEnfants(getActivity(), nomEnfants, groupesEnfants, imageId);

                    for(DataSnapshot c : dataSnapshot.getChildren()){
                        String nom = c.getKey();

                        if(nom.contentEquals("informations")){
                            // Ne rien faire, cette section ne nous intéresse pas
                        } else {
                            Enfant enfant = dataSnapshot.child(nom).getValue(Enfant.class);
                            nomEnfants.add(enfant.getPrenom());
                            groupesEnfants.add(enfant.getGroupe());
                            //nomEnfants.add(c.getKey());
                            //groupesEnfants.add(c.getKey());
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

    /**
     * Initialisation des actions à éxecuter quand on clique sur le bouton
     */
    private void InitOnClickListener(){
        ajouterEnf = (Button) v.findViewById(R.id.ajouterenfant);
        ajouterEnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAccueil.v.setVisibility(View.INVISIBLE);
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new AjouterEnfant());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
