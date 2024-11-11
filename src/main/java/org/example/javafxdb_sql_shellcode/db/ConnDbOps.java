/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.javafxdb_sql_shellcode.db;

import org.example.javafxdb_sql_shellcode.Person;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author MoaathAlrajab
 * @version
 * connect to the database
 * add update and delete
 */
public class ConnDbOps {
    final static String MYSQL_SERVER_URL = "jdbc:mysql://csc311admin.mysql.database.azure.com/";
    final static String DB_URL = "jdbc:mysql://csc311admin.mysql.database.azure.com/DBname";
   //  final String USERNAME = "parisi";
   // final String PASSWORD = "qwertyuiop1!";

    public boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;
        String USERNAME = "";
        String PASSWORD = "";

        // Load credentials from file
        try (Scanner infile = new Scanner(new File("config_shellcode.txt"), StandardCharsets.UTF_8.name())) {
        USERNAME = infile.next();
         PASSWORD = infile.next();

            // Connect to MySQL server and create the database if not created
            try (Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
                 Statement statement = conn.createStatement()) {
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DBname");
            }

            // Connect to the specific database and create the "users" table if not created
            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 Statement statement = conn.createStatement()) {
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
                try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users")) {
                    if (resultSet.next()) {
                        int numUsers = resultSet.getInt(1);
                        if (numUsers > 0) {
                            hasRegisteredUsers = true;
                        }
                    }
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return hasRegisteredUsers;
    }


    // Query a user by name and return a Person object
    public Person queryUserByName(String name) {
        Person person = null;
        Connection conn = null;
        String USERNAME = "";
        String PASSWORD = "";

        try {

            Scanner infile = new Scanner(new File("config_shellcode.txt"), StandardCharsets.UTF_8.name());
            USERNAME = infile.next();
            PASSWORD = infile.next();
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
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
        } catch (SQLException |FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }



        return person;
    }

    // List all users from the database and return a list of Person objects
    public List<Person> listAllUsers() {
        List<Person> users = new ArrayList<>();
        Connection conn = null;
        String USERNAME = "";
        String PASSWORD = "";

        try {

            Scanner infile = new Scanner(new File("config_shellcode.txt"), StandardCharsets.UTF_8.name());
            USERNAME = infile.next();
            PASSWORD = infile.next();
           conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
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
        } catch (SQLException |FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return users;
    }

    // Insert a new user into the database
    public void insertUser(String name, String email, String phone, String address, String password) {
        Connection conn = null;
        String USERNAME = "";
        String PASSWORD = "";

        try {

            Scanner infile = new Scanner(new File("config_shellcode.txt"), StandardCharsets.UTF_8.name());
            USERNAME = infile.next();
            PASSWORD = infile.next();
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
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
                System.out.println("Please close to refresh if in GUI");
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException |FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void deleteUser(Integer id) {

        String USERNAME = "";
        String PASSWORD = "";

        try (Scanner infile = new Scanner(new File("config_shellcode.txt"), StandardCharsets.UTF_8.name())){
            USERNAME = infile.next();
            PASSWORD = infile.next();
            // Establish a connection to the database
            try(Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
                //SQL delete user detail
                PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                preparedStatement.setInt(1, id);
                // Execute the update query
                int rowsAffected = preparedStatement.executeUpdate();

                // Checking if the deletion was successful
                if (rowsAffected > 0) {
                    System.out.println("User with ID " + id + " was deleted successfully.");
                    System.out.println("Please close to refresh if in GUI");
                } else {
                    System.out.println("No user found with ID " + id + ".");
                }
            }catch(SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void updateUser(Integer id, String name, String email, String phone, String address, String password) {

        String USERNAME = "";
        String PASSWORD = "";

        try (Scanner infile = new Scanner(new File("config_shellcode.txt"), StandardCharsets.UTF_8.name())){
            USERNAME = infile.next();
            PASSWORD = infile.next();
            // Establish a connection to the database
            try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // SQL update user details

            // Prepare the statement
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE id = ?")) {
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
                    System.out.println("Please close to refresh if in GUI");
                } else {
                    System.out.println("No user found with ID " + id + ". Update failed.");
                }
            }

        } catch (SQLException |FileNotFoundException e) {
            // Handle any SQL exceptions
            System.out.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);

        }
            }
        }
