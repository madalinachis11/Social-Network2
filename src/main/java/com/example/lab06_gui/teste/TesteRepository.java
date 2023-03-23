package com.example.lab06_gui.teste;

import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.UtilizatorValidator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.domain.validators.Validator;
import com.example.lab06_gui.repository.file.UtilizatorFile;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;


public class TesteRepository {
    @Test
    public void testAdaugaStergeUtilizator(){
        Validator<Utilizator> valUtilizator = new UtilizatorValidator();
        UtilizatorFile repo=new UtilizatorFile("src/teste/testusers.txt", valUtilizator);

        Iterable<Utilizator> copy = repo.findAll();
        List<Utilizator> lista = new ArrayList<>();
        copy.forEach(lista::add);

        assertEquals(9, repo.getSize());

        Utilizator u1 = new Utilizator(1000L,"Alexia","Nistor", "a@yahoo.com","afrhtrshg");//,"15.09.2002");
        try {
            repo.save(u1);
            assertEquals(10, repo.getSize());
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        repo.delete(u1.getID());
        assertEquals(9, repo.getSize());

        repo.saveToFile(lista);
    }

    @Test
    public void testAdaugaStergePrietenie(){

    }
}
