package com.example.lab06_gui.repository.file;

import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.Validator;

import java.util.ArrayList;
import java.util.List;

public class UtilizatorFile extends AbstractFileRepository<Long, Utilizator> {

    public UtilizatorFile(String fileName, Validator<Utilizator> validator) {
        super(fileName, validator);
    }

    @Override
    public Utilizator extractEntity(List<String> attributes) {
        //TODO: implement method
        Utilizator user = new Utilizator(Long.parseLong(attributes.get(0)),attributes.get(1),attributes.get(2),
                attributes.get(3),attributes.get(4));//,attributes.get(5));
        //user.setID(Integer.parseInt(attributes.get(0))); // sau Long - parseLong

        return user;
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getID()+";"+entity.getNume()+";"+entity.getPrenume()+";"
                +entity.getEmail()+";"+entity.getParola();//+";"+entity.getDataNasterii();
    }

    @Override
    public List<Utilizator> filter(String criteriu){
        //filtreaza dupa nume si prenume
        List<Utilizator> all = getAllAsList();
        List<Utilizator> filtered = new ArrayList<>();
        for(Utilizator u : all)
            if(u.getNume().contains(criteriu) || u.getPrenume().contains(criteriu) ||
                    criteriu.contains(u.getNume()) || criteriu.contains(u.getPrenume()))
                filtered.add(u);

        return filtered;
    }
}