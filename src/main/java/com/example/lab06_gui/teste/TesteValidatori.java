package com.example.lab06_gui.teste;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.PrietenieValidator;
import com.example.lab06_gui.domain.validators.UtilizatorValidator;
import com.example.lab06_gui.domain.validators.ValidationException;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.testng.AssertJUnit.assertTrue;


public class TesteValidatori {

    @Test
    public void testValidatorUtilizator() {
        UtilizatorValidator v = new UtilizatorValidator();
        Utilizator u1 = new Utilizator(1111L, "ana", "banana", "", "");//, "33.13.2029");
        try {
            v.validate(u1);
            assertTrue(false);
        } catch (ValidationException e) {
            assertTrue(true);
        }

        Utilizator u2 = new Utilizator(0L, "ana", "banana", "sfes@gmail.com", "");//, "30.12.2009");
        try {
            v.validate(u2);
            assertTrue(false);
        } catch (ValidationException e) {
            assertTrue(true);
        }

        Utilizator u3 = new Utilizator(1135L, "Bugnar", "Andreea", "andreeb@yahoo.com", "dkjfngeha");//, "03.12.2001");
        try {
            v.validate(u3);
            assertTrue(true);
        } catch (ValidationException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testValidatorPrietenie(){
        PrietenieValidator v = new PrietenieValidator();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeString = now.format(formatter);
        LocalDateTime friendsFrom = LocalDateTime.parse(dateTimeString, formatter);

        Prietenie p1 = new Prietenie(0L, 0L,friendsFrom);
        try{
            v.validate(p1);
            assertTrue(false);
        }catch(ValidationException e){
            assertTrue(true);
        }

        Prietenie p2=new Prietenie(1123L, 1123L,friendsFrom);
        try{
            v.validate(p2);
            assertTrue(false);
        }catch(ValidationException e){
            assertTrue(true);
        }
        Prietenie p3=new Prietenie(1145L, 1678L,friendsFrom);
        try{
            v.validate(p3);
            assertTrue(true);
        }catch(ValidationException e){
            assertTrue(false);
        }

    }
}