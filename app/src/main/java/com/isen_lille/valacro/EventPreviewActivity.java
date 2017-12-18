package com.isen_lille.valacro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité permettant d'avoir un aperçu d'un événement à venir
 *
 */
public class EventPreviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("table_valacro").child("table_parent").child(firebaseAuth.getCurrentUser().getUid()).child("informations");
    Parent parent;

    /**
     * Map Google Map
     */
    protected GoogleMap mMap;

    /**
     * TextView pour le nom et la date de l'event ainsi que le nom de l'utilisateur
     */
    protected TextView eventName, userName, eventDate;

    /**
     * Boutons pour participer ou ne pas participer à l'evenement ainsi que pour voir l'affiche
     */
    protected Button comeToEvent, notComeToEvent, eventImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_preview);
        initializeMap();
        initializeTextViews();
        initializeButton();
    }

    /**
     * Méthode issue de l'interface OnMapReadyCallback
     * @see OnMapReadyCallback
     * @param googleMap Google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(getIntent().getDoubleExtra("latitude", -34), getIntent().getDoubleExtra("longitude", 151));
        mMap.addMarker(new MarkerOptions().position(sydney).title(getIntent().getStringExtra("eventName")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
    }

    /**
     * Méthode d'initialisation de la Map
     */
    private void initializeMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Méthode d'initialisation de tous les champs textes
     */
    private void initializeTextViews(){
        // Etape 1 : On récupère la référence des vues via la classe R
        eventName = (TextView)findViewById(R.id.event_name);
        userName = (TextView)findViewById(R.id.event_username);
        eventDate = (TextView)findViewById(R.id.event_date);

        // Etape 2 : On ajoute les données récupérer grâce à l'Intent
        eventName.setText(getIntent().getStringExtra("eventName"));
        userName.setText(getIntent().getStringExtra("username"));

        // Etape 3 : Cas particulier. Comme on a enregistré les données de la date sous forme de Timestamp,
        //              on va dans un premier temps créer une instance de Calendar, qui se verra attribuer
        //              la valeur présent dans l'Intent.
        //           On formate enfin la date avec DateFormat
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(getIntent().getLongExtra("dateTimestamp", 0));
        eventDate.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(new Date(getIntent().getLongExtra("date", 0) * 1000L)));

    }

    /**
     * Méthode d'initialisation du bouton de participation
     */
    private void initializeButton(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parent = dataSnapshot.getValue(Parent.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Récupération des références
        comeToEvent = (Button)findViewById(R.id.come_to_event);
        notComeToEvent = (Button)findViewById(R.id.not_come_to_event);

        // Gestion du clic sur le bouton de participation à un événement
        comeToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("table_valacro").child("table_events").child(String.valueOf(getIntent().getLongExtra("date", 0))).child("participant")
                        .child(parent.getNom() + " " + parent.getPrenom()).setValue(1);
                Toast.makeText(EventPreviewActivity.this, "Vous participez", Toast.LENGTH_LONG).show();
            }
        });

        // Gestion du clic sur le bouton de non participation à un événement
        notComeToEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("table_valacro").child("table_events").child(String.valueOf(getIntent().getLongExtra("date", 0))).child("participant")
                        .child(parent.getNom() + " " + parent.getPrenom()).removeValue();
                Toast.makeText(EventPreviewActivity.this, "Vous ne participez pas", Toast.LENGTH_LONG).show();
            }
        });

        // Ici, on précise que si l'événement est passé, on n'affiche plus les boutons de participation !
        if(getIntent().getLongExtra("date", 0) * 1000L < System.currentTimeMillis()){
            comeToEvent.setVisibility(View.GONE);
            notComeToEvent.setVisibility(View.GONE);
        }

        //Afficher l'affiche de l'évènement
        eventImage = (Button)findViewById(R.id.event_image);
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventPreviewActivity.this, EventImageActivity.class));
            }
        });

    }

}
