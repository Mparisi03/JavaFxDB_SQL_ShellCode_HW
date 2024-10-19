package org.example.javafxdb_sql_shellcode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.example.javafxdb_sql_shellcode.db.ConnDbOps;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * copy the code from the ui program, used the primary fxml file for it.
 * connet it to ConnDbOps
 * change the variables to match with ConnDbOps
 */



public class DB_GUI_Controller implements Initializable {
    private ConnDbOps cdbop = new ConnDbOps();
    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
                    new Person(1, "mitch","gmail","ffewf","fsfds","fdsfe"),
                    new Person(2, "Jacob2","gmail","ffewf","fsfds","fdsfe")

            );


    @FXML
    TextField name, email, phone, address,password;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_n, tv_em, tv_phone, tv_address, tv_pass;

    @FXML
    ImageView img_view;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Person> data = cdbop.listAllUsers();
          ObservableList<Person> personObservableList = FXCollections.observableArrayList(data);
        data.clear();
        data.addAll(data);
        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_n.setCellValueFactory(new PropertyValueFactory<>("name"));
        tv_em.setCellValueFactory(new PropertyValueFactory<>("email"));
        tv_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tv_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        tv_pass.setCellValueFactory(new PropertyValueFactory<>("password"));


        tv.setItems(personObservableList);
    }


    @FXML
    protected void addNewRecord() {
        Person newPerson = new Person(
                data.size() + 1,
                name.getText(),
                email.getText(),
                phone.getText(),
                address.getText(),
                password.getText()
        );
        cdbop.insertUser(newPerson.getName(), newPerson.getEmail(), newPerson.getPhone(), newPerson.getAddress(), newPerson.getPassword());
        data.add(newPerson);
        clearForm(); // Reset the form after adding a new record
    }

    @FXML
    protected void clearForm() {
        name.clear();
        email.clear();
        phone.clear();
        address.clear();
        password.clear();


    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }

    @FXML
    protected void editRecord() {
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            int index = data.indexOf(selectedPerson);
            selectedPerson.setName(name.getText());
            selectedPerson.setEmail(email.getText());
            selectedPerson.setPhone(phone.getText());
            selectedPerson.setAddress(address.getText());
            selectedPerson.setPassword(password.getText());

            cdbop.updateUser(selectedPerson.getId(),selectedPerson.getName(),selectedPerson.getEmail(),
                    selectedPerson.getPhone(),selectedPerson.getAddress(),selectedPerson.getPassword());

            data.set(index, selectedPerson); // Update the person in the list
        }
    }

    @FXML
    protected void deleteRecord() {
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            cdbop.deleteUser(selectedPerson.getId());
            data.remove(selectedPerson);
        }
    }

    @FXML
    protected void showImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            name.setText(selectedPerson.getName());
            email.setText(selectedPerson.getEmail());
            phone.setText(selectedPerson.getPhone());
            address.setText(selectedPerson.getAddress());
            password.setText(selectedPerson.getPassword());
        }
    }
}