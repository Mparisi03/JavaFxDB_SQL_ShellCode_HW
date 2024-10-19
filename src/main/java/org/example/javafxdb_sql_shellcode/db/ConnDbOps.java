/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.javafxdb_sql_shellcode.db;

import org.example.javafxdb_sql_shellcode.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MoaathAlrajab
 */
public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://csc311admin.mysql.database.azure.com/";
    final String DB_URL = "jdbc:mysql://csc311admin.mysql.database.azure.com/DBname";
    final String USERNAME = "parisi";
    final String PASSWORD = "zxcvbnm2@";

    public boolean connectToDatabase() {
        boolean hasRegistredUsers = false;

        try {
            // First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DBname");
            statement.close();
            conn.close();

            // Second, connect to the database and create the table "users" if not created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL UNIQUE,"
                    + "phone VARCHAR(200),"
                    + "address VARCHAR(200),"
                    + "password VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            // Check if we have users in the table
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }

            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegistredUsers;
    }

    // Query a user by name and return a Person object
    public Person queryUserByName(String name) {
        Person person = null;

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users WHERE name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");

                person = new Person(id, name, email, phone, address, password);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }

    // List all users from the database and return a list of Person objects
    public List<Person> listAllUsers() {
        List<Person> users = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String password = resultSet.getString("password");

                Person person = new Person(id, name, email, phone, address, password);
                users.add(person);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Insert a new user into the database
    public void insertUser(String name, String email, String phone, String address, String password) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO users (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("A new user was inserted successfully.");
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteUser(Integer id) {
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            String sql = "DELETE FROM users WHERE id = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            int rowsAffected = preparedStatement.executeUpdate();

            // Checking if the deletion was successful
            if (rowsAffected > 0) {
                System.out.println("User with ID " + id + " was deleted successfully.");
            } else {
                System.out.println("No user found with ID " + id + ".");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateUser(Integer id, String name, String email, String phone, String address, String password) {


        try {
            // Establish a connection to the database
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // SQL query to update user details
            String sql = "UPDATE users SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE id = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);
            preparedStatement.setInt(6, id); // Set the ID of the user you want to update

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if any rows were updated
            if (rowsAffected > 0) {
                System.out.println("User with ID " + id + " was updated successfully.");
            } else {
                System.out.println("No user found with ID " + id + ". Update failed.");
            }

        } catch (SQLException e) {
            // Handle any SQL exceptions
            System.out.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
            }
        }
