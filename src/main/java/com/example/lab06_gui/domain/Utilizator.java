package com.example.lab06_gui.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Utilizator extends Entity<Long>{
    private String nume;
    private String prenume;
    //private String username;
    private String email;
    private String parola;
    LocalDateTime date;

    public Utilizator(Long id, String nume, String prenume, String email, String parola){
        this.nume = nume;
        this.prenume = prenume;
        setID(id);
        this.email = email;
        this.parola = parola;
    }

    public String toString() {
        return getID() + " " + nume + " " + prenume + " " + email + " " + parola; }

    public int hashCode(){ return Objects.hash(nume, getID()); }

    //getters
    public String getNume(){ return this.nume; }

    public String getPrenume(){ return this.prenume; }

    public String getEmail(){ return this.email; }

    public String getParola(){ return this.parola; }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    //setters
    public void setNume(String nume){ this.nume = nume; }

    public void setPrenume(String prenume){ this.prenume = prenume; }

    public void setEmail(String email){ this.email = email; }

    public void setParola(String parola){ this.parola = parola; }

    public boolean equals(Object o){
        // verifica daca este acelasi obiect, prin adresa
        if (this == o) return true;
        // daca obiectul este null sau clasa lui nu este Utilizator,
        // nu sunt egale
        if (o == null || getClass() != o.getClass()) return false;
        // cast la utilizator
        Utilizator user = (Utilizator) o;
        // compara proprietatile ca Utilizatori
        return Objects.equals(user.getID(), getID()) // abs(x-y)<epsilon
                && Objects.equals(nume, user.nume);
    }


}

