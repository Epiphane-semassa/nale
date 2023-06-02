package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.Admin;
import com.nale.newslettermanagement.data.FileSystemStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreatorSettingsController  implements Initializable {

    public TextField usernameField;
    public Label usernameError;
    public TextField passwordField;
    public Label passwordError;
    public TextField lastnameField  ;
    public Label lastnameError;
    public TextField firstnameField;
    public Label firstnameError;
    public TextField emailField;
    public Label emailError;
    public TextField phoneField;
    public Label phoneError;
    public TextField addressField;
    public Label addressError;
    public Label processError;
    public BorderPane parent;
    public Button cancelBtn;
    public Button validBtn;

    private  FileSystemStorage fileSystemStorage;
    private DashboardController dashboardController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();
    }

    public void cancelProcess(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/welcome.fxml")));
        primaryStage.setTitle("Nale");
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void saveAccount(ActionEvent actionEvent) throws IOException {
        usernameError.setText("");
        passwordError.setText("");
        firstnameError.setText("");
        lastnameError.setText("");
        addressError.setText("");
        phoneError.setText("");
        emailError.setText("");
        boolean hasError = false;

        if(usernameField.getText().trim().isEmpty()){
            hasError = true;
            usernameError.setText("Champ obligatoire");
        }
        if(passwordField.getText().trim().isEmpty()){
            hasError = true;
            passwordError.setText("Champ obligatoire");
        }
        if(firstnameField.getText().trim().isEmpty()){
            hasError = true;
            firstnameError.setText("Champ obligatoire");
        }
        if(lastnameField.getText().trim().isEmpty()){
            hasError = true;
            lastnameError.setText("Champ obligatoire");
        }
        if(addressField.getText().trim().isEmpty()){
            hasError = true;
            addressError.setText("Champ obligatoire");
        }
        if(phoneField.getText().trim().isEmpty()){
            hasError = true;
            phoneError.setText("Champ obligatoire");
        }
        if(emailField.getText().trim().isEmpty()){
            hasError = true;
            emailError.setText("Champ obligatoire");
        } else {
            if (!Util.validateEmail(emailField.getText().trim())){
                hasError = true;
                emailError.setText("Email non valide");
            }
        }

        if(hasError){
            return;
        }

        processError.setVisible(false);
        processError.managedProperty().bind(processError.visibleProperty());

        Admin admin = new Admin();
        Admin.User user = new Admin.User(usernameField.getText().trim(), passwordField.getText().trim());
        Admin.Profile profile = new Admin.Profile(
                emailField.getText().trim(),
                lastnameField.getText().trim(),
                firstnameField.getText().trim(),
                phoneField.getText().trim(),
                addressField.getText().trim()
        );
        admin.setUser(user);
        admin.setProfile(profile);

        boolean success = fileSystemStorage.saveCreatorAccount(admin);
        if (success) {
            goToDash(actionEvent);
        } else {
            processError.setVisible(true);
            processError.setText(Util.formatText("Une erreur s'est produite. Veuillez ressayer plus tard."));
        }
    }

    public void goToDash(ActionEvent actionEvent) throws IOException {
        Stage dashStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
        Parent root = loader.load();
        dashboardController = loader.getController();
        dashboardController.onBulletin(actionEvent);
        dashStage.setTitle("Dashboard");
        Scene scene = new Scene(root);
        dashStage.setScene(scene);
        dashStage.setMaximized(true);
        dashStage.show();
        Stage stage = (Stage) validBtn.getScene().getWindow();
        stage.close();
    }
}
