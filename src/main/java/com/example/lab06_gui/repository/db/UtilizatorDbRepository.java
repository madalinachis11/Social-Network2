package com.example.lab06_gui.repository.db;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.domain.validators.Validator;
import com.example.lab06_gui.repository.Repository;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Utilizator findOne(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null");

        String sql = "SELECT * FROM users where users.id = ?";

        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, Math.toIntExact(id));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                //Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");

                Utilizator user = new Utilizator(id, nume, prenume, email, parola);//, dataNasterii);
                user.setID(id);
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator findByTwoStrings(String emailUser, String passwordUser){
        Utilizator utilizator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users where email = ? and parola = ?")){
            statement.setString(1, emailUser);
            statement.setString(2, passwordUser);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");

                utilizator = new Utilizator(id, nume, prenume, email, parola);
                utilizator.setID(id);
                return utilizator;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilizator;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String email = resultSet.getString("email");
                String parola = resultSet.getString("parola");

                Utilizator utilizator = new Utilizator(id, nume, prenume, email, parola);//, dataNasterii);
                utilizator.setID(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator save(Utilizator entity) throws ValidationException {
        if (entity==null)
            throw new IllegalArgumentException("Entity must be not null");

        validator.validate(entity);

        String sql = "insert into users (nume, prenume, email, parola ) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getPrenume());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getParola());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Utilizator delete(Long id) {
        if(id == null)
            throw new IllegalArgumentException("ID is null! Cannot delete!");

        String sql = "delete from users where id = ?";
        String sql2 = "delete from friendships where id1 = ? or id2 = ?";
        Utilizator user = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            user = this.findOne(id);
            if(user == null)
                return null;

            ps2.setLong(1, id);
            ps2.setLong(2, id);
            ps2.executeUpdate();

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Utilizator update(Utilizator entity) throws ValidationException {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null");

        validator.validate(entity);

        String sql = "update users set nume = ?, prenume = ?, email = ?, parola = ? where id = ?";
        int nrRowsAffected = 0;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getPrenume());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getParola());
            ps.setLong(5, entity.getID());
            //ps.setString(5, entity.getDataNasterii());

            nrRowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(nrRowsAffected > 0)
            return null;
        return entity;
    }

    @Override
    public List<Utilizator> getAllAsList() {
        Iterable<Utilizator> iterable = findAll();
        List<Utilizator> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public int getSize() {
        return getAllAsList().size();
    }
}