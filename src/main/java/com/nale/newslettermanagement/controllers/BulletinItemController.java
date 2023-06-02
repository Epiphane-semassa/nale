package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
import com.nale.newslettermanagement.listeners.CallBack;
import com.nale.newslettermanagement.listeners.RefreshUI;
import com.nale.newslettermanagement.model.Bulletin;
import com.nale.newslettermanagement.model.Follower;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class BulletinItemController implements Initializable, CallBack {

    public Label nameLabel;
    public Label descriptionLabel;
    public Label nbFollowersLabel;
    public MenuButton groupMenuBtn;
    public MenuItem btnMoreMenu;
    public MenuItem btnUpdateMenu;
    public MenuItem btnDeleteMenu;
    public MenuItem btnAddFollowerMenu;

    private RefreshUI refreshUI;
    private Bulletin bulletin;
    private Set<Bulletin> bulletins;
    private FileSystemStorage fileSystemStorage;
    private BorderPane dashContainerPane;
    private Parent bulletinsContainerPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();
    }

    public void onDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Confirmation");
        alert.setContentText("Voulez vous supprimer ce bulletin ?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Oui");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Non");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/views/assets/css/main.css")).toExternalForm());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            bulletins.removeIf(bulletin1 -> bulletin1.getId() == bulletin.getId());
            fileSystemStorage.saveBulletins(bulletins);
            Platform.runLater(() -> refreshUI.refresh());
        }
    }

    public void onUpdate(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/edit-bulletin.fxml"));
        Parent parent = fxmlLoader.load();
        EditBulletinController editBulletinController = fxmlLoader.getController();
        editBulletinController.setBulletin(bulletin);
        editBulletinController.setBulletins(bulletins);
        editBulletinController.setRefreshUI(refreshUI);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Util.formatText("Modifier un bulletin"));
        stage.setResizable(false);
        stage.setScene(scene);
        editBulletinController.setStage(stage);
        stage.showAndWait();
        if (editBulletinController.getBulletin() != null) {
            setBulletin(editBulletinController.getBulletin());
        }
    }

    public void setRefreshUI(RefreshUI refreshUI) {
        this.refreshUI = refreshUI;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
        nameLabel.setText(bulletin.getName());
        descriptionLabel.setText(bulletin.getDescription());
        nbFollowersLabel.setText(bulletin.getFollowersIds().size() + " abonné" + ((bulletin.getFollowersIds().size() < 2) ? "" : "s"));
    }

    public void setBulletins(Set<Bulletin> bulletins) {
        this.bulletins = bulletins;
    }

    public void onMore(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/bulletin-details.fxml"));
        Parent parent = fxmlLoader.load();
        BulletinDetailsController bulletinDetailsController = fxmlLoader.getController();
        bulletinDetailsController.setBulletin(bulletin);
        bulletinDetailsController.setCallBack(this);
        dashContainerPane.setCenter(parent);
    }

    public void onAddFollower(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/edit-follower.fxml"));
        Parent parent = fxmlLoader.load();
        EditFollowerController editFollowerController = fxmlLoader.getController();
        editFollowerController.setFollower(new Follower());
        editFollowerController.setRefreshUI(refreshUI);
        editFollowerController.setFollowers(fileSystemStorage.loadFollowers());
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Util.formatText("Ajouter un abonné"));
        stage.setResizable(false);
        stage.setScene(scene);
        editFollowerController.setStage(stage);
        stage.showAndWait();
        if (editFollowerController.getFollower() != null) {
            bulletin.getFollowersIds().add(editFollowerController.getFollower().getId());
            for (Bulletin bulletin1 : bulletins) {
                if (bulletin1.getId().equals(bulletin.getId())) {
                    bulletin1.setFollowersIds(bulletin.getFollowersIds());
                    refreshUI.refresh();
                }
            }
            fileSystemStorage.saveBulletins(bulletins);
        }
    }

    public BorderPane getDashContainerPane() {
        return dashContainerPane;
    }

    public void setDashContainerPane(BorderPane dashContainerPane) {
        this.dashContainerPane = dashContainerPane;
    }

    public Parent getBulletinsContainerPane() {
        return bulletinsContainerPane;
    }

    public void setBulletinsContainerPane(Parent bulletinsContainerPane) {
        this.bulletinsContainerPane = bulletinsContainerPane;
    }

    @Override
    public void onCall() {
        dashContainerPane.setCenter(getBulletinsContainerPane());
        refreshUI.refresh();
    }
}
