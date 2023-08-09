package com.example.humancreator.repository;

import com.example.humancreator.dto.Human;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcDao {
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/human_creator?allowPublicKeyRetrieval=true&useSSL=false";
    private final String DATABASE_USERNAME = "root";
    private final String DATABASE_PASSWORD = "root";
    private final String INSERT_QUERY = "INSERT INTO human(name, age, birthday) VALUES (?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE human SET name=?, age=?, birthday=? WHERE id=?";
    private final String DELETE_QUERY = "DELETE FROM human WHERE id=?";
    private final String SELECT_QUERY = "SELECT id, name, age, birthday FROM human";


    public List<Human> findAll() {
        List<Human> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {

             ResultSet resultSet = preparedStatement.executeQuery();
             while (resultSet.next()) {
                 result.add(new Human(
                         resultSet.getLong("id"),
                         resultSet.getString("name"),
                         resultSet.getInt("age"),
                         resultSet.getDate("birthday").toLocalDate()));
             }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return result;
    }

    public void storeIntoToDb(Human human) {
        if (human.getId() > 0) {
            updateRecord(human);
        } else {
            insertRecord(human);
        }
    }

    public void deleteFromDb(Human human) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setLong(1, human.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void insertRecord(Human human) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, human.getName().getValue());
            preparedStatement.setInt(2, human.getAge().get());
            preparedStatement.setDate(3, Date.valueOf(human.getBirthday().get()));

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    human.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void updateRecord(Human human) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, human.getName().getValue());
            preparedStatement.setInt(2, human.getAge().get());
            preparedStatement.setDate(3, Date.valueOf(human.getBirthday().get()));
            preparedStatement.setLong(4, human.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}
