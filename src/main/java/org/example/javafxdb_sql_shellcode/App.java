package org.example.javafxdb_sql_shellcode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.example.javafxdb_sql_shellcode.db.ConnDbOps;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static ConnDbOps cdbop;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("db_interface_gui.fxml"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/example/javafxdb_sql_shellcode/primary.fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        cdbop = new ConnDbOps();
        Scanner scan = new Scanner(System.in);

        char input;
        do {
            System.out.println(" ");
            System.out.println("============== Menu ==============");
            System.out.println("| To start GUI,           press 'g' |");
            System.out.println("| To connect to DB,       press 'c' |");
            System.out.println("| To display all users,   press 'a' |");
            System.out.println("| To insert to the DB,    press 'i' |");
            System.out.println("| To delete from the DB,  press 'd' |");
            System.out.println("| To update the DB,       press 'u' |");
            System.out.println("| To query by name,       press 'q' |");
            System.out.println("| To exit,                press 'e' |");
            System.out.println("===================================");
            System.out.print("Enter your choice: ");
            input = scan.next().charAt(0);

            switch (input) {
                case 'g':
                     launch(args); //GUI
                    break;

                case 'c':
                    cdbop.connectToDatabase(); //Your existing method
                    break;
                case 'a':
                    List<Person> users = cdbop.listAllUsers(); // Make sure to store the return value
                    if (users.isEmpty()) {
                        System.out.println("No users found.");
                    } else {
                        // Print the list of users
                        for (Person user : users) {
                            System.out.println(user);
                        }
                    }
                    break;

                case 'i':
                    System.out.print("Enter Name: ");
                    String name = scan.next();
                    System.out.print("Enter Email: ");
                    String email = scan.next();
                    System.out.print("Enter Phone: ");
                    String phone = scan.next();
                    System.out.print("Enter Address: ");
                    String address = scan.next();
                    System.out.print("Enter Password: ");
                    String password = scan.next();
                    cdbop.insertUser(name, email, phone, address, password); //Your insertUser method
                    break;
                case 'd':
                    System.out.print("Enter the user ID to delete: ");
                    int id = scan.nextInt();
                    cdbop.deleteUser(id);
                    break;
                case 'u':
                    System.out.print("Enter the user ID to update: ");
                    int newid = scan.nextInt();
                    System.out.print("Enter new Name: ");
                    String newName = scan.next();
                    System.out.print("Enter new Email: ");
                    String newEmail = scan.next();
                    System.out.print("Enter new Phone: ");
                    String newPhone = scan.next();
                    System.out.print("Enter new Address: ");
                    String newAddress = scan.next();
                    System.out.print("Enter new Password: ");
                    String newPassword = scan.next();
                    cdbop.updateUser(newid,newName, newEmail, newPhone, newAddress, newPassword);
                    break;


                case 'q':
                    System.out.print("Enter the name to query: ");
                    String queryName = scan.next();
                    cdbop.queryUserByName(queryName); //Your queryUserByName method
                    break;
                case 'e':
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println(" ");
        } while (input != 'e');

        scan.close();

       
    }




}
