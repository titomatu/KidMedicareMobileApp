package org.tripulantesg2.kidmedicare.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class Solicitud {
    private String user_id;
    private String state;
    private GeoPoint location;
    private Timestamp date;

    public Solicitud(String user_id, String state, GeoPoint location, Timestamp date) {
        this.user_id = user_id;
        this.state = state;
        this.location = location;
        this.date = date;
    }

    public Solicitud() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return "Solicitud: " +
                "estado: '" + state + '\'' +
                "- ubicación: " + location +
                ", fecha solicitud: " + format.format(date.toDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solicitud solicitud = (Solicitud) o;
        return Objects.equals(user_id, solicitud.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }
}
