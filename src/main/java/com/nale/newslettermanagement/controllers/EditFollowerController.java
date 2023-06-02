package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
import com.nale.newslettermanagement.listeners.RefreshUI;
import com.nale.newslettermanagement.model.Bulletin;
import com.nale.newslettermanagement.model.Follower;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

public class EditFollowerController implements Initializable {


    public Label processSuccess;
    public Label processError;
    public TextField lastnameField;
    public Label lastnameError;
    public TextField firstnameField;
    public Label firstnameError;
    public TextField emailField;
    public Label emailError;
    public TextField phoneField;
    public Label phoneError;
    public TextField addressField;
    public Label addressError;

    private Follower follower;
    private FileSystemStorage fileSystemStorage;
    private RefreshUI refreshUI;
    private Set<Follower> followers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();

        processError.setVisible(false);
        processError.managedProperty().bind(processError.visibleProperty());

        processSuccess.setVisible(false);
        processSuccess.managedProperty().bind(processSuccess.visibleProperty());
    }

    public void onCancel(ActionEvent actionEvent) {
        follower = null;
        closeStage(actionEvent);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> follower = null);
    }

    public void onSave(ActionEvent actionEvent) {
        firstnameError.setText("");
        lastnameError.setText("");
        addressError.setText("");
        phoneError.setText("");
        emailError.setText("");
        boolean hasError = false;

        if(firstnameField.getText().trim().isEmpty()){
            hasError = true;
            firstnameError.setText("Champ obligatoire");
        }
        if(lastnameField.getText().trim().isEmpty()){
            hasError = true;
            lastnameError.setText("Champ obligatoire");
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

        follower.setFirstName(Util.formatText(firstnameField.getText().trim()));
        follower.setLastName(Util.formatText(lastnameField.getText().trim()));
        follower.setEmail(Util.formatText(emailField.getText().trim()));
        follower.setPhoneNumber(Util.formatText(phoneField.getText().trim()));
        follower.setAddress(Util.formatText(addressField.getText().trim()));
        boolean success;
        if (follower.getId() != null) {
            for (Follower follower1 : followers) {
                if (follower1.getId() == follower.getId()) {
                    follower1.setFirstName(follower.getFirstName());
                    follower1.setLastName(follower.getLastName());
                    follower1.setEmail(follower.getEmail());
                    follower1.setPhoneNumber(follower.getPhoneNumber());
                    follower1.setAddress(follower.getAddress());
                }
            }
            success = fileSystemStorage.saveFollowers(followers);
            postEdit(success, actionEvent);
        } else {
            follower.setId(UUID.randomUUID());
            success = fileSystemStorage.saveFollower(follower);
            postEdit(success, actionEvent);
        }

    }

    private void postEdit(boolean success, ActionEvent actionEvent) {
        if (success) {
            processError.setVisible(false);
            processError.managedProperty().bind(processError.visibleProperty());
            processSuccess.setVisible(true);

            processSuccess.setText((follower.getId() != null) ? "Abonné modifié avec succès." : "Abonné ejouté avec succès.");
            closeStage(actionEvent);
            Platform.runLater(() -> refreshUI.refresh());
        } else {
            processSuccess.setVisible(false);
            processSuccess.managedProperty().bind(processSuccess.visibleProperty());
            processError.setVisible(true);

            processError.setText("Erreur. Veuillez ressayez plus tard.");
        }
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
        if (follower.getId() != null) {
            firstnameField.setText(follower.getFirstName());
            lastnameField.setText(follower.getLastName());
            emailField.setText(follower.getEmail());
            phoneField.setText(follower.getPhoneNumber());
            addressField.setText(follower.getAddress());
        } else {
            addressField.setText("");
            phoneField.setText("");
        }
    }

    public Follower getFollower() {
        return follower;
    }

    public void setRefreshUI(RefreshUI refreshUI) {
        this.refreshUI = refreshUI;
    }

    public void setFollowers(Set<Follower> followers) {
        this.followers = followers;
    }
}
