package com.example.lab06_gui.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<String> {
    //Utilizator u1, u2;
    //Aceasta linie de cod declara doua variabile de tip Long numite ID1 si ID2, care sunt marcate ca fiind
    // finale (final). Acest lucru inseamna ca valorile lor nu pot fi modificate dupa ce sunt initializate.
    // Variabilele sunt private, ceea ce inseamna ca pot fi accesate doar din interiorul clasei in care sunt definite.
    private final Long ID1, ID2;
    private LocalDateTime friendsFrom;
    private boolean acceptat;

    //Acest constructor primeste trei argumente: IDprieten1, IDprieten2 si friendsFrom.
    // Aceste argumente sunt folosite pentru a initializa membrii instantei clasei Prietenie:
    // ID1, ID2, friendsFrom, acceptat si ID.

    //Argumentele IDprieten1 si IDprieten2 sunt ID-urile a doua entitati de tip Profil,
    // care sunt prieteni. Argumentul friendsFrom este data si ora la care a fost creata
    // prietenia intre cele doua entitati.

    //Membrii ID1 si ID2 sunt initializati cu IDprieten1 si IDprieten2. Membrii friendsFrom
    // si acceptat sunt initializati cu valorile date ca argumente, respectiv friendsFrom si false.

    //Membrii ID1 si ID2 sunt concatenati pentru a forma o singura valoare de tip String numita id.
    // Aceasta valoare este folosita pentru a initializa membrul ID al clasei Entity prin intermediul
    // metodei mostenite setID.

    public Prietenie(Long IDprieten1, Long IDprieten2, LocalDateTime friendsFrom)
    {
        this.ID1 = IDprieten1;
        this.ID2 = IDprieten2;
        this.friendsFrom=friendsFrom;
        this.acceptat = false;

        String ids1, ids2, id;
        ids1 = String.valueOf(this.ID1);
        ids2 = String.valueOf(this.ID2);
        if(this.ID1<this.ID2)
            id = ids1 + "_" + ids2;
        else id = ids2 + "_" + ids1;

        setID(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(ID1, prietenie.ID1) && Objects.equals(ID2, prietenie.ID2);
    }

    //Metoda hashCode() din Java este folosită pentru a genera un cod hash pentru un obiect.
    // Un cod hash este un număr întreg utilizat pentru a identifica un obiect într-o colecție.
    // Implementarea unei bune metode hashCode() poate îmbunătăți performanța aplicației în anumite cazuri.

    //În cazul metodei hashCode() din exemplul dat, se calculează valoarea hash pe baza valorilor
    // ID-urilor prietenilor și a valorii hash-ului obiectului din superclasă. Se folosește metoda
    // Objects.hash() pentru a calcula valoarea hash pe baza mai multor argumente.

    //Această implementare asigură că doi prieteni cu aceleași ID-uri,
    // dar în ordine diferită, vor avea același cod hash.

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ID1, ID2);
    }

    @Override
    public String toString() {
        return "Prietenie {" + "ID1=" + ID1 + ", id2=" + ID2 + '}';
    }

    public Long getID1(){
        return this.ID1;
    }

    public Long getID2(){
        return this.ID2;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public boolean isAcceptat() {
        return acceptat;
    }

    public void setAcceptat(boolean acceptat) {
        this.acceptat = acceptat;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    /*public void addPrietenie(Utilizator u1, Utilizator u2){
        u1.getListaPrieteni().add(u2);
        u2.getListaPrieteni().add(u1);
    }*/


}