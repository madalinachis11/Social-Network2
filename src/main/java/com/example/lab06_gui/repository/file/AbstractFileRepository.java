package com.example.lab06_gui.repository.file;
import com.example.lab06_gui.domain.Entity;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.domain.validators.Validator;
import com.example.lab06_gui.repository.InMemoryRepository;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();

    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }

        //sau cu lambda - curs 4, sem 4 si 5
//        Path path = Paths.get(fileName);
//        try {
//            List<String> lines = Files.readAllLines(path);
//            lines.forEach(linie -> {
//                E entity=extractEntity(Arrays.asList(linie.split(";")));
//                super.save(entity);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    protected abstract String createEntityAsString(E entity);

    public abstract List<E> filter(String criteriu);

    @Override
    public E save(E entity) throws ValidationException {
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;

    }

    @Override
    public E update(E entity) throws ValidationException {
        E e=super.update(entity);
        if (e==null)
        {
            List<E> list = getAllAsList();
            saveToFile(list);
        }
        return e;
    }

    @Override
    public E delete(ID id) {
        E e=super.delete(id);
        if (e!=null)
        {
            List<E> list = getAllAsList();
            saveToFile(list);
        }
        return e;
    }

    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveToFile(List<E> lst) {
        //System.out.println(entities);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (E e : lst) {
                writer.write(createEntityAsString(e));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
