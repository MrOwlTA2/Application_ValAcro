package com.isen_lille.valacro;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Classe qui va peupler la liste selon les données qui lui sont envoyées
 */
public class ListeEnfants extends ArrayAdapter<String> {

    /**
     * Contexte de l'activité
     */
    private final Activity context;

    /**
     * Liste de String concernant le web
     */
    private final List<String> web;

    /**
     * Liste d'entier concernant les images
     */
    private final Integer[] imageId;

    /**
     * Liste de String des descriptions
     */
    private final List<String> description;

    /**
     * Constructeur de la classe
     * @param context Contexte
     * @param web Web
     * @param description Description
     * @param imageId id de l'image
     */
    public ListeEnfants(Activity context,
                        List<String> web, List<String> description, Integer[] imageId) {
        super(context, R.layout.liste_enfants, web);
        this.context = context;
        this.web = web;
        this.description = description;
        this.imageId = imageId;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.liste_enfants, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textViewPrenomProfilListe);
        TextView txtGroup = (TextView) rowView.findViewById(R.id.textViewGroupeProfilListe);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageProfilSmall);
        txtTitle.setText(web.get(position));
        txtGroup.setText(description.get(position));

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
