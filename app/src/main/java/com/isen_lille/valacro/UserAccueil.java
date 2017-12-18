package com.isen_lille.valacro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isen_lille.valacro.adapter.CustomListViewAdapter;
import com.isen_lille.valacro.objects.Event;

import java.util.ArrayList;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité d'accueil recensant tous les événements passés et à venir (côté utilisateur)
 *
 */
public class UserAccueil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MonProfil.DataPassListener {

    /**
     * Bouton pour aller au calendrier
     */
    protected Button goCalendar;

    /**
     * ListView des évènements à venir et passés
     */
    protected ListView upcomingEvents, pastEvents;

    /**
     * CustomListViewAdapter des événements à venir et passés
     */
    protected CustomListViewAdapter upcomingEventsAdapter, pastEventsAdapter;

    /**
     * Nouvelle instance d'ArrayList des événements à venir
     */
    protected ArrayList<Event> upcomingEventList = new ArrayList<>();

    /**
     * Nouvelle instance d'ArrayList des événements passés
     */
    protected ArrayList<Event> pastEventList = new ArrayList<>();

    /**
     * Fragment créé à null
     */
    Fragment fragment = null;

    // Variables de la base de données Firebase
    /**
     * TAG
     */
    private static final String TAG = "UserAccueil";

    /**
     * Base de donnée Firebase
     */
    FirebaseDatabase database;

    /**
     * Référence à la base de donnée
     */
    private DatabaseReference mDatabaseParent;

    // Variables du layout
    /**
     * Layout
     */
    static View v;

    /**
     * Toolbar set a null
     */
    Toolbar toolbar = null;

    /**
     * Barre de navigation
     */
    NavigationView navigationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accueil);


        InitToolbar();
        InitVariables();
        initializeButtons();
        initializeListView();
        InitDatabase();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment != null && fragment.isVisible()) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
            this.setTitle("Val\'Acro");
            v.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    /**
     * Méthode définissant quelles actions effectuer selon l'onglet du menu
     * @param itemId
     */
    private void displaySelectedScreen(int itemId) {

        // On initialise le fragment correspondant au choix
        switch (itemId) {
            case R.id.nav_mon_profil:
                fragment = new MonProfil();
                break;
            case R.id.nav_messagerie:
                fragment = new Messagerie();
                break;
            case R.id.nav_groupe_entrainement:
                fragment = new EntrainementGroupe();
                break;
            case R.id.nav_contacter:
                fragment = new NousContacter();
                break;
            case R.id.nav_disconnect:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }

        // On remplace le fragment actuel par le nouveau
        if (fragment != null) {
            v.setVisibility(View.INVISIBLE);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // On ferme le menu dès qu'on a fait un choix
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * Initialise la toolbar
     */
    private void InitToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.logo_icon_valacro);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Initialise la connexion à Firebase
     */
    private void InitDatabase(){
        try{
            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            database = FirebaseDatabase.getInstance();
            mDatabaseParent = database.getReference("table_valacro").child("table_parent").child(currentUserID);
        } catch (Exception e){
            //
        }

    }

    /**
     * Initialise les variables
     */
    private void InitVariables(){
        // Enregistre le layout du main dans une variable pour modifier sa visibilité quand on navigue dans les fragments
        v = (View) findViewById(R.id.fragment_layout_user_accueil);

    }

    /**
     * Méthode d'initialisation des boutons
     */
    private void initializeButtons(){
        goCalendar = (Button)findViewById(R.id.goCalendar);

        goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAccueil.this,CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Méthode d'initialisation de la ListView
     */
    private void initializeListView(){
        upcomingEvents = (ListView) findViewById(R.id.upcoming_events);
        pastEvents = (ListView) findViewById(R.id.past_events);

        // On crée l'adapter par rapport aux données présentes dans la liste
        upcomingEventsAdapter = new CustomListViewAdapter(this, upcomingEventList);
        // On attache l'adapter
        upcomingEvents.setAdapter(upcomingEventsAdapter);
        // On crée enfin la méthode qui va détecter le clic sur un item en particulier
        upcomingEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent eventPreview = new Intent(UserAccueil.this, EventPreviewActivity.class);
                eventPreview.putExtra("eventName", upcomingEventList.get(i).getName());
                eventPreview.putExtra("date", upcomingEventList.get(i).getDateTimestamp());
                eventPreview.putExtra("username", upcomingEventList.get(i).getUser());
                eventPreview.putExtra("latitude", upcomingEventList.get(i).getLatitude());
                eventPreview.putExtra("longitude", upcomingEventList.get(i).getLongitude());
                startActivity(eventPreview);
            }
        });

        // On crée l'adapter par rapport aux données présentes dans la liste
        pastEventsAdapter = new CustomListViewAdapter(this, pastEventList);
        // On attache l'adapter
        pastEvents.setAdapter(pastEventsAdapter);
        // On crée enfin la méthode qui va détecter le clic sur un item en particulier
        pastEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent eventPreview = new Intent(UserAccueil.this, EventPreviewActivity.class);
                eventPreview.putExtra("eventName", pastEventList.get(i).getName());
                eventPreview.putExtra("date", pastEventList.get(i).getDateTimestamp());
                eventPreview.putExtra("username", pastEventList.get(i).getUser());
                eventPreview.putExtra("latitude", pastEventList.get(i).getLatitude());
                eventPreview.putExtra("longitude", pastEventList.get(i).getLongitude());
                startActivity(eventPreview);
            }
        });

        prepareEventsDatas();

    }

    /**
     * Méthode qui va récupérer les données des événements enregistrés sur la base de données Firebase
     */
    private void prepareEventsDatas(){

        FirebaseDatabase.getInstance().getReference("table_valacro").child("table_events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Etape 1 : On récupère les données sous forme d'objet Event
                Event event = dataSnapshot.getValue(Event.class);

                // Etape 2 : Ici, pour faire la distinction entre les événements passés et ceux à venir,
                //              on teste la valeur Timestamp de l'événement par rapport à la valeur
                //              Timestamp actuelle du système.
                if(event.getDateTimestamp() * 1000L < System.currentTimeMillis()){ // Si le Timestamp de l'événement est inférieur à la valeur actuelle du Timestamp système
                    pastEventList.add(event); // On ajoute l'événement dans la liste des événements passés...
                    pastEventsAdapter.notifyDataSetChanged(); // ... puis on notifie à l'adaptateur les changements
                } else { // Sinon, cela veut dire que l'événement n'est pas encore passé !
                    upcomingEventList.add(event); // On ajoute l'événement dans la liste des événements à venir...
                    upcomingEventsAdapter.notifyDataSetChanged(); // ... puis on notifie à l'adaptateur les changements
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void envoyerDonnees(String data) {
        AfficherEnfantsUser fragmentB = new AfficherEnfantsUser ();
        Bundle args = new Bundle();
        args.putString(AfficherEnfantsUser.DATA_RECEIVE, data);
        fragmentB .setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragmentB )
                .addToBackStack(null)
                .commit();
    }
}
