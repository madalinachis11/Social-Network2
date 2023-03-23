package com.example.lab06_gui.repository.db;

import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.domain.validators.Validator;
import com.example.lab06_gui.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrietenieDbRepository implements Repository<String, Prietenie> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Prietenie> validator;
    //private List<Prietenie> lista = new ArrayList<>();

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        //loadData();
    }

    private Prietenie extractFriendship(ResultSet resultSet) throws SQLException {
        Prietenie friendship;
        if(resultSet.next()){
            Long id1 = resultSet.getLong("id1");
            Long id2 = resultSet.getLong("id2");
            LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
            boolean acceptat = resultSet.getBoolean("acceptat");

            friendship = new Prietenie(id1, id2, date);
            friendship.setAcceptat(acceptat);

            return friendship;
        }
        return null;
    }

    @Override
    public Prietenie findOne(String id) {
        if(id == null)
            throw new IllegalArgumentException("ID must not be null");

        Prietenie friendship;
        String sql = "SELECT * FROM friendships WHERE id_prietenie = ?";
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            friendship = this.extractFriendship(resultSet);
            if(friendship != null)
                return friendship;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Prietenie findByTwoStrings(String string1, String string2) {
        return null;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id_prietenie");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                boolean acceptat = resultSet.getBoolean("acceptat");

                Prietenie friendship = new Prietenie(id1, id2, date);
                friendship.setAcceptat(acceptat);
                friendships.add(friendship);
            }
            return friendships;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    private void executeStatement(Prietenie friendship, String sql){
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, friendship.getID1());
            statement.setLong(2, friendship.getID2());
            //statement.setBoolean(3, friendship.isAcceptat());
            statement.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Prietenie save(Prietenie friendship) throws ValidationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if(friendship == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        if(findOne(friendship.getID()) != null) { //daca mai avem inca o prietenie la fel
            //return entity;
            throw new ValidationException("ID duplicat!!!");
        }

        validator.validate(friendship);

        String sql = "INSERT INTO friendships(id_prietenie,id1,id2,date,acceptat) VALUES ('" + friendship.getID()+ "',?,?,'"+friendship.getFriendsFrom().format(formatter)+"','"+friendship.isAcceptat()+"')";
        this.executeStatement(friendship,sql);
        return null;
    }

    @Override
    public Prietenie delete(String id) {
        if(id == null)
            throw new IllegalArgumentException("ID must not be null");

        Prietenie friendship = this.findOne(id);
        if(friendship != null){
            String sql = "DELETE FROM friendships WHERE id1 = ? AND id2 = ?";
            this.executeStatement(friendship,sql);
        }
        return friendship;
    }

    @Override
    public Prietenie update(Prietenie friendship) throws ValidationException {
        if(friendship == null)
            throw new IllegalArgumentException("Entity must not be null");

        validator.validate(friendship);

        String sql = "update friendships set acceptat = ? where id_prietenie = ?";
        int nrRowsAffected = 0;

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setBoolean(1, friendship.isAcceptat());
            ps.setString(2, friendship.getID());

            nrRowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(nrRowsAffected > 0)
            return null;
        return friendship;
    }

    @Override
    public List<Prietenie> getAllAsList() {
        Iterable<Prietenie> iterable = findAll();
        List<Prietenie> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    @Override
    public int getSize() {
        return getAllAsList().size();
    }
}
