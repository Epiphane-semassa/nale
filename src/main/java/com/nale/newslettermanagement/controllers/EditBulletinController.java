package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
import com.nale.newslettermanagement.listeners.RefreshUI;
import com.nale.newslettermanagement.model.Bulletin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

public class EditBulletinController implements Initializable {

    public TextArea descriptionField;
    public Label descriptionError;
    public TextField nameField;
    public Label nameError;
    public Label processSuccess;
    public Label processError;

    private Bulletin bulletin;
    private FileSystemStorage fileSystemStorage;
    private RefreshUI refreshUI;
    private Set<Bulletin> bulletins;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();

        processError.setVisible(false);
        processError.managedProperty().bind(processError.visibleProperty());

        processSuccess.setVisible(false);
        processSuccess.managedProperty().bind(processSuccess.visibleProperty());
    }


    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
        if (bulletin.getId() != null) {
            nameField.setText(bulletin.getName());
            descriptionField.setText(bulletin.getDescription());
        } else {
            nameField.setText("");
            descriptionField.setText("");
        }
    }

    public Bulletin getBulletin() {
        return bulletin;
    }

    public void onCancel(ActionEvent actionEvent) {
        bulletin = null;
        closeStage(actionEvent);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> bulletin = null);
    }

    public void onSave(ActionEvent actionEvent) {

        nameError.setText("");
        descriptionError.setText("");
        boolean hasError = false;

        if (nameField.getText().trim().isEmpty()) {
            hasError = true;
            nameError.setText("Champ obligatoire");
        }
        if (descriptionField.getText().trim().isEmpty()) {
            hasError = true;
            descriptionError.setText("Champ obligatoire");
        }

        if (hasError) {
            return;
        }

        bulletin.setName(Util.formatText(nameField.getText().trim()));
        bulletin.setDescription(Util.formatText(descriptionField.getText().trim()));
        boolean success;

        if (bulletin.getId() != null) {
            for (Bulletin bulletin1 : bulletins) {
                if (bulletin1.getId().equals(bulletin.getId())) {
                    bulletin1.setName(bulletin.getName());
                    bulletin1.setDescription(bulletin.getDescription());
                }
            }
            success = fileSystemStorage.saveBulletins(bulletins);
            postEdit(success, actionEvent);
        } else {
            bulletin.setId(UUID.randomUUID());
            success = fileSystemStorage.saveBulletin(bulletin);
            postEdit(success, actionEvent);
        }
    }

    private void postEdit(boolean success, ActionEvent actionEvent) {
        if (success) {
            processError.setVisible(false);
            processError.managedProperty().bind(processError.visibleProperty());
            processSuccess.setVisible(true);

            processSuccess.setText((bulletin.getId() != null) ? "Bulletin modifié avec succès." : "Bulletin ejouté avec succès.");
            closeStage(actionEvent);
            Platform.runLater(() -> refreshUI.refresh());
        } else {
            processSuccess.setVisible(false);
            processSuccess.managedProperty().bind(processSuccess.visibleProperty());
            processError.setVisible(true);

            processError.setText("Erreur. Veuillez ressayez plus tard.");
        }
    }

    public void setRefreshUI(RefreshUI refreshUI) {
        this.refreshUI = refreshUI;
    }

    public void setBulletins(Set<Bulletin> bulletins) {
        this.bulletins = bulletins;
    }
}
