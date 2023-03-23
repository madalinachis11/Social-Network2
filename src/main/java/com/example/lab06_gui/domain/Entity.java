package com.example.lab06_gui.domain;

///Acesta este un cod Java care definește o clasă abstractă numită "Entity"
// care va fi folosită pentru a crea alte entități specifice aplicației.
// Clasa este generificată, unde "ID" reprezintă tipul de date al id-ului unic al entității.

///Această clasă conține două metode simple getter și setter pentru a accesa
// și modifica id-ul entității. Clasa este, de asemenea, serializabilă, ceea ce
// înseamnă că poate fi transmisă pe rețea sau salvată într-un fișier și apoi restaurată.

public class Entity<ID> {
    private ID id;

    public ID getID(){
        return id;
    }

    public void setID(ID id){ this.id = id; }
}
