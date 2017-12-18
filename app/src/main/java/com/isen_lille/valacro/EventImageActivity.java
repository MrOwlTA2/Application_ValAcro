package com.isen_lille.valacro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Activité permettant l'affichage de l'affiche de l'evenement choisis.
 *
 */
public class EventImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_image);

        //Code pour le lien vers la page facebook
        TextView t2 = (TextView) findViewById(R.id.linktxt);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }


}