package com.isen_lille.valacro.objects;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hugos
 * @version 1.0
 *
 * Gestion de l'objet Event
 *
 */
@IgnoreExtraProperties
public class Event {

    private String name;
    private String user;
    private long dateTimestamp;
    private double latitude;
    private double longitude;

    /**
     * Constructeur par défaut
     */
    public Event(){ }

    /**
     * Constructeur prenant en paramètre tous les attributs d'un événement
     * @param name Le nom de l'événement
     * @param dateTimestamp La date de l'événement sous forme de TimeStamp
     * @param username Le nom de l'utilisateur ayant crée l'événement
     * @param latitude La latitude du lieu de l'événement (Pour intégration avec Google Maps)
     * @param longitude La longitude du lieu de l'événement (Pour intégration avec Google Maps)
     */
    public Event(String name, String username, long dateTimestamp, double latitude, double longitude){
        this.name = name;
        this.user = username;
        this.dateTimestamp = dateTimestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Getter pour le nom de l'event
     * @return le nom de l'évent
     */
    public String getName(){ return this.name; }

    /**
     * Getter pour l'utilisateur
     * @return l'utilisateur
     */
    public String getUser(){ return this.user; }

    /**
     * Getter pour la date et heure de l'event
     * @return la date et heure de l'event
     */
    public long getDateTimestamp(){ return this.dateTimestamp; }

    /**
     * Getter pour la latitude
     * @return la latitude
     */
    public double getLatitude(){ return this.latitude; }

    /**
     * Getter pour la longitude
     * @return la longitude
     */
    public double getLongitude(){ return this.longitude; }


    /**
     * Setter pour le nom de l'event
     * @param name Nom de l'evenement
     */
    public void setName(String name){ this.name = name; }

    /**
     * Setter pour le nom de l'utilisateur
     * @param user nom de l'utilisateur
     */
    public void setUser(String user){ this.user = user; }

    /**
     * Setter pour la date de l'événement
     * @param dateTimestamp La date de l'événement sous forme de TimeStamp
     */
    public void setDateTimestamp(long dateTimestamp){ this.dateTimestamp = dateTimestamp; }

    /**
     * Setter pour la latitude de l'evenement
     * @param latitude la latitude
     */
    public void setLatitude(double latitude){ this.latitude = latitude; }

    /**
     * Setter pour la longitude
     * @param longitude la longitude
     */
    public void setLongitude(double longitude){ this.longitude = longitude; }

    /**
     * Override pour la fonction toString
     * @return l'event et ses caractéristiques sous forme de String
     */
    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", dateTimestamp=" + dateTimestamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    /**
     * Exclude pour la fonction toMap
     * @return Map contenant nom, utilisateur, date et heure, latitude et longitude
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("user", user);
        result.put("dateTimestamp", dateTimestamp);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
