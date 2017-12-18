package com.isen_lille.valacro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
 * @author hugos, carlos
 * @version 1.0
 *
 * Activité d'accueil recensant tous les événements passés et à venir (côté admin)
 *
 */
public class AdminAccueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Bouton ajouter un événement ou aller au calendrier
     */
    protected Button addEvent, goCalendar;

    /**
     * ListView des événements à venir ou passés
     */
    protected ListView upcomingEvents, pastEvents;

    /**
     * CustomListViewAdapter des événements à venir ou passés
     */
    protected CustomListViewAdapter upcomingEventsAdapter, pastEventsAdapter;

    /**
     * Nouvel instance d'ArrayList des événements à venir
     */
    protected ArrayList<Event> upcomingEventList = new ArrayList<>();

    /**
     * Nouvel instance d'ArrayList des événements passés
     */
    protected ArrayList<Event> pastEventList = new ArrayList<>();

    /**
     * Création d'un objet fragment
     */
    Fragment fragment = null;

    /**
     * Variables de la base de données Firebase
     */
    private static final String TAG = "UserAccueil";

    /**
     * Firebase
     */
    FirebaseDatabase database;

    /**
     * Référence à la base de donnée
     */
    private DatabaseReference mDatabaseParent;

    /**
     * Layout
     */
    static View v;

    /**
     * Toolbar
     */
    Toolbar toolbar = null;

    /**
     * Barre de navigation
     */
    NavigationView navigationView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accueil);

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
        // Si le menu est ouvert, on le ferme
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(fragment != null && fragment.isVisible()){
            // Si on a un fragment actif, on le supprime
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
            this.setTitle("Val\'Acro");
            v.setVisibility(View.VISIBLE);
        } else {
            // Action normale de retour
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
     *  id de l'item ex : messagerie, tournois ..
     */
    private void displaySelectedScreen(int itemId) {

        // On initialise le fragment correspondant au choix
        switch (itemId) {
            case R.id.nav_liste_adherents:
                fragment = new ListeAdherents();
                break;
            case R.id.nav_messagerie:
                fragment = new Messagerie();
                break;
            case R.id.nav_liste_parents:
                fragment = new ListeParents();
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
            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            database = FirebaseDatabase.getInstance();
            mDatabaseParent = database.getReference("table_valacro").child("table_parent").child(currentUserID);
    }

    /**
     * Initialise les variables
     */
    private void InitVariables(){
        // Enregistre le layout du main dans une variable pour modifier sa visibilité quand on navigue dans les fragments
        v = (View) findViewById(R.id.fragment_layout_admin_accueil);

        //textViewBonjour = (TextView) findViewById(R.id.textViewAccueilBonjour) ;
    }


    /**
     * Méthode d'initialisation des boutons
     */
    private void initializeButtons(){
        //Bouton d'ajout des événements
        addEvent = (Button)findViewById(R.id.add_event);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminAccueil.this, AddEventActivity.class));
            }
        });

        //Bouton pour afficher le calendrier
        goCalendar = (Button)findViewById(R.id.goCalendar);
        goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAccueil.this,CalendarActivity.class);
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
                Intent eventPreview = new Intent(AdminAccueil.this, EventPreviewAdminActivity.class);
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
                Intent eventPreview = new Intent(AdminAccueil.this, EventPreviewAdminActivity.class);
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
}
