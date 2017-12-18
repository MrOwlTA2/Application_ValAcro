package com.isen_lille.valacro;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isen_lille.valacro.objects.Event;

import java.util.Calendar;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité permettant d'ajouter un évènement dans la base de données
 *
 */
public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /**
     * TAG
     */
    private static final String TAG = "AutoComplete";

    /**
     * Calendrier sauvegardé
     */
    private Calendar savedCalendar;

    /**
     * Reference vers la base de donnée
     */
    private DatabaseReference mDatabase;

    /**
     * Les différents boutons
     */
    protected Button addEvent, addImage;

    /**
     * EditText pour le nom de l'evenement
     */
    protected EditText eventName;

    /**
     * Bouton image pour le nom et l'heure
     */
    protected ImageButton editDate, editTime;

    /**
     * TextView pour prévisualiser la date et l'heure
     */
    protected TextView previewDate, previewTime;

    /**
     * Fragment autocomplété pour Google Maps
     */
    protected PlaceAutocompleteFragment autocompleteFragment;

    /**
     * Création d'un nouvel objet évenement à sauvegarder
     */
    private Event eventToSave = new Event();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        savedCalendar = Calendar.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("table_valacro").child("table_events");

        initializeButtons();
        initializeEditText();
        initializeAutoCompleteFragment();
        initializeTextViews();
        initializeImageButtons();

    }

    /**
     * Méthode issue de l'interface OnDateSetListener
     * @see android.app.DatePickerDialog.OnDateSetListener
     * @param view vue choix de date
     * @param year L'année
     * @param monthOfYear Le mois
     * @param dayOfMonth Le jour
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        savedCalendar.set(Calendar.YEAR, year); // Attribution de l'année au Calendar à sauvegardé
        savedCalendar.set(Calendar.MONTH, monthOfYear); // Attribution du mois au Calendar à sauvegardé
        savedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // Attribution du jour du mois au Calendar à sauvegardé
        previewDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year); // Affichage de la date dans l'aperçu texte
    }

    /**
     * Méthode issues de l'interface OnTimeSetListener
     * @see android.app.TimePickerDialog.OnTimeSetListener
     * @param timePicker Choix de l'heure
     * @param selectedHour Heure choisis
     * @param selectedMinute Minutes choisis
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        savedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour); // Attribution de l'heure au Calendar à sauvegardé
        savedCalendar.set(Calendar.MINUTE, selectedMinute); // Attribution des minutes au Calendar à sauvegardé
        previewTime.setText(selectedHour + ":" + selectedMinute); // Affichage de l'heure dans l'aperçu texte
    }

    /**
     * Méthode d'initialisation des boutons
     */
    private void initializeButtons(){
        addEvent = (Button)findViewById(R.id.button_validate);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Etape 1 : On récupère le reste des valeurs à attribuer à l'événement.
                //      Les valeurs de latitude et de longitude sont directement attribuées
                //      via la Listener de l'AutoCompleteFragment.
                eventToSave.setName(eventName.getText().toString());
                eventToSave.setUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                eventToSave.setDateTimestamp(savedCalendar.getTimeInMillis() / 1000L);

                // Etape 2 : On sauvegarde les données de l'événement dans la base de données Firebase.
                mDatabase.child(String.valueOf(savedCalendar.getTimeInMillis() / 1000L)).setValue(eventToSave, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        //startActivity(new Intent(AddEventActivity.this, MainActivity.class)); // On repars vers l'accueil une fois que les données ont bien été enregistrées.
                        finish();
                    }
                });
            }
        });

        addImage = (Button)findViewById(R.id.button_add_affiche);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Module en cours d'implémentation.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Méthode d'initialisation des EditText
     */
    private void initializeEditText(){
        eventName = (EditText)findViewById(R.id.event_name);
    }

    /**
     * Méthode d'initialisation du fragment d'auto-complétion d'adresse
     */
    private void initializeAutoCompleteFragment() {
        //Etape 1 : On récupère la référence du fragment AutoCOmplete.
        autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        // Etape 2 : On lui attribue un Listener pour récupérer la latitude et la longitude d'un lieu renseigné.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                eventToSave.setLatitude(place.getLatLng().latitude);
                eventToSave.setLongitude(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /**
     * Méthode d'initialisation des TextView
     */
    private void initializeTextViews(){
        previewDate = (TextView)findViewById(R.id.event_date_preview);
        previewTime = (TextView)findViewById(R.id.event_time_preview);
    }

    /**
     * Méthode d'initialisation des boutons d'édition
     */
    private void initializeImageButtons(){
        // Etape 1 : On récupère les références des vues via la classe R
        editDate = (ImageButton)findViewById(R.id.event_date_button);
        editTime = (ImageButton)findViewById(R.id.event_time_button);

        // Etape 2 : Gestion du clic sur le bouton d'édition de la date
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On récupère au préalable la date et l'heure actuelle
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                // Puis on crée la boite de dialogue contenant un DatePicker.
                new DatePickerDialog(AddEventActivity.this, AddEventActivity.this, mYear, mMonth, mDay).show();
            }
        });

        // Etape 3 : Gestion du clic sur le bouton d'édition de l'heure
        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pareil que pour la date mais avec l'heure et les minutes
                Calendar mCurrentTime = Calendar.getInstance();
                int mHour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int mMinute = mCurrentTime.get(Calendar.MINUTE);

                // Et on crée la boite de dialogue contenant le TimePicker
                // NB : Le true à la fin du constructeur signifie que les heures apparaitront sous le format 24H.
                //      Il suffira de mettre false pour avoir une base de 12H avec "AM" et "PM"
                new TimePickerDialog(AddEventActivity.this, AddEventActivity.this, mHour, mMinute, true).show();
            }
        });
    }

    /**
     * Méthode d'ajout d'une affiche
     */
    private void addImage(){

    }

}