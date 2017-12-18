package com.isen_lille.valacro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.isen_lille.valacro.R;
import com.isen_lille.valacro.objects.Event;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 *
 * @author hugos
 * @version 1.0
 *
 * Classe Adaptateur personnalisé pour les ListView
 *
 */

public class CustomListViewAdapter extends ArrayAdapter<Event> {

    /**
     * Constructeur
     * @param context le contexte
     * @param eventArrayList ArrayList d'events
     */
    public CustomListViewAdapter(Context context, ArrayList<Event> eventArrayList) {
        super(context, 0, eventArrayList);
    }

    /**
     * Override du getter getView avec en entrée les paramètres utile à la création de la vue
     * @param position la position
     * @param convertView vue
     * @param parent Regroupement de vue
     * @return la vue créée
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Etape 1 : On récupère une liste particulière
        Event event = getItem(position);

        // Etape 2 : On utilise le LayoutInflater pour inclure le layout list_item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_row, parent, false);
        }

        // Etape 3 : On récupère la référence du champ de texte shoppingListName
        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView dateName = (TextView) convertView.findViewById(R.id.eventDate) ;

        // Etape 4 : On inclus le nom de la liste et la couleur de la liste sur la vue texte
        eventName.setText(event.getName());
        dateName.setText(getDate(event.getDateTimestamp()));

        // Etape 5 : On retournne la vue créée
        return convertView;
    }

    /**
     * Getter pour la date
     * @param date La date
     * @return la date au format souhaité
     */
    private String getDate(long date){
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(date * 1000L);
    }



}