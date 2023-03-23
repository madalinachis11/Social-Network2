package com.example.lab06_gui.teste;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.testng.AssertJUnit.assertEquals;

public class TesteDomain {

    @Test
    public void testUtilizator(){
        Utilizator u=new Utilizator(1000L,"Alexia","Nistor", "a@yahoo.com","afrhtrshg");//,"15.09.2002");
        assert(u.getID()==1000);
        assertEquals(u.getEmail(),"a@yahoo.com");
        assertEquals(u.getParola(),"afrhtrshg");
        assertEquals(u.getNume(),"Alexia");
        assertEquals(u.getPrenume(),"Nistor");

        //u.setId("2");
        u.setEmail("b@yahoo.com");
        u.setParola("715mk45");
        u.setNume("b");
        u.setPrenume("b");

        assert(u.getID()==1000);
        assertEquals(u.getEmail(),"b@yahoo.com");
        assertEquals(u.getParola(),"715mk45");
        assertEquals(u.getNume(),"b");
        assertEquals(u.getPrenume(),"b");
    }

    @Test
    public void testPrietenie() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateTimeString = "2022-12-12 08:45";
        LocalDateTime friendsFrom = LocalDateTime.parse(dateTimeString, formatter);
        Prietenie p = new Prietenie(1121L, 1345L, friendsFrom);
        System.out.println(p.getID());
        assert(Objects.equals(p.getID(), "1121_1345"));
        assert(p.getID1()==1121);
        assert(p.getID2()==1345);
    }
}
