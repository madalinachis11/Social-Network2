package com.example.lab06_gui.domain.validators;

import com.example.lab06_gui.domain.Prietenie;

import java.util.Objects;

public class PrietenieValidator implements Validator<Prietenie>{
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getID1()==0)
            throw new ValidationException("\nID prieten 1 Invalid!\n");
        if(entity.getID2()==0)
            throw new ValidationException("\nID prieten 2 Invalid!\n");
        if(entity.getID()==null) //null
            throw new ValidationException("\nID Invalid!\n");
        if(Objects.equals(entity.getID1(), entity.getID2()))
            throw new ValidationException("\nID uri identice!\n");
    }
}
