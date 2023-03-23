package com.example.lab06_gui.repository.file;

import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrietenieFile extends AbstractFileRepository<String, Prietenie> {

    public PrietenieFile(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
    }

    @Override
    public Prietenie extractEntity(List<String> attributes) {
        //TODO: implement method
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new Prietenie(Long.parseLong(attributes.get(0)),
                Long.parseLong(attributes.get(1)),
                LocalDateTime.parse(attributes.get(2), formatter));
        //return new Prietenie(Integer.parseInt(attributes.get(0)), Integer.parseInt(attributes.get(1)));
    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return entity.getID1()+";"+ entity.getID2()+";"+entity.getFriendsFrom().format(formatter);
    }

    @Override
    public List<Prietenie> filter(String criteriu){
        //filtreaza dupa id
        List<Prietenie> all = getAllAsList();
        List<Prietenie> filtered = new ArrayList<>();
        for(Prietenie p : all)
            if(p.getID().contains(criteriu))
                filtered.add(p);

        return filtered;
    }
}
