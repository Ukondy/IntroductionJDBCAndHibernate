package dao;

import model.User;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getJDBCConnection();

    @Override
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS user (id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL, name VARCHAR(30), lastName VARCHAR(40), age TINYINT(3) )";
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS user";
        try(Statement statement = connection.createStatement();){
            statement.executeUpdate(sql);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO user (name, lastName, age) VALUES(?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
           statement.setString(1 , name);
           statement.setString(2 , lastName);
           statement.setByte(3 , age);
           statement.executeUpdate();
           connection.commit();
            System.out.printf("user %s %s добавлен в базу данных" + System.lineSeparator(), name, lastName);
        } catch(SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch(SQLException e) {
            try {
                connection.rollback();
            } catch(SQLException ex) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, name, lastName, age FROM user";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet results = statement.executeQuery();
            while(results.next()) {
                long id = results.getLong("id");
                String name = results.getString("name");
                String lastName = results.getString("lastname");
                byte age = results.getByte("age");
                User user = new User(id, name, lastName, age);
                list.add(user);
                connection.commit();
            }
        } catch(SQLException e) {
            try {
                connection.rollback();
            } catch(SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void clearUserTable() {
        String sql = "TRUNCATE TABLE user";
        try(Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(sql);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
