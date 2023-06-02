package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class FollowerItemController implements Initializable {

    public Label lastnameLabel;
    public Label firstnameLabel;
    public Label emailLabel;
    public Label phoneLabel;
    public Label addressLabel;
    public Label nbBulletinsLabel;

    private RefreshUI refreshUI;
    private Follower follower;
    private Set<Follower> followers;
    private Set<Bulletin> bulletins;
    private FileSystemStorage fileSystemStorage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();
        bulletins = fileSystemStorage.loadBulletins();
    }

    public void onDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Confirmation");
        alert.setContentText("Voulez vous supprimer cet abonné ?");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Oui");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Non");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/views/assets/css/main.css")).toExternalForm());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            followers.removeIf(follower1 -> follower1.getId() == follower.getId());
            fileSystemStorage.saveFollowers(followers);
            for (Bulletin bulletin : bulletins) {
                bulletin.getFollowersIds().remove(follower.getId());
            }
            fileSystemStorage.saveBulletins(bulletins);
            Platform.runLater(() -> refreshUI.refresh());
        }
    }

    public void onUpdate(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/edit-follower.fxml"));
        Parent parent = fxmlLoader.load();
        EditFollowerController editFollowerController = fxmlLoader.getController();
        editFollowerController.setFollower(follower);
        editFollowerController.setRefreshUI(refreshUI);
        editFollowerController.setFollowers(followers);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Util.formatText("Modifier un abonné"));
        stage.setResizable(false);
        stage.setScene(scene);
        editFollowerController.setStage(stage);
        stage.showAndWait();
        if (editFollowerController.getFollower() != null) {
            setFollower(editFollowerController.getFollower());
        }
    }

    public void seeBulletins(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/follower-subscriptions.fxml"));
        Parent parent = fxmlLoader.load();
        FollowerSubscriptionsController followerSubscriptionsController = fxmlLoader.getController();
        followerSubscriptionsController.setFollower(follower);
        followerSubscriptionsController.setRefreshUI(refreshUI);
        followerSubscriptionsController.loadData();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Util.formatText("Bulletins de l'abonné"));
        stage.setResizable(false);
        stage.setScene(scene);
        followerSubscriptionsController.setStage(stage);
        stage.showAndWait();
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
        firstnameLabel.setText(follower.getFirstName());
        lastnameLabel.setText(follower.getLastName());
        emailLabel.setText(follower.getEmail());
        phoneLabel.setText(follower.getPhoneNumber());
        addressLabel.setText(follower.getAddress());
        long count = 0;
        for (Bulletin bulletin : bulletins) {
            if (bulletin.getFollowersIds().contains(follower.getId())) {
                count ++;
            }
        }
        nbBulletinsLabel.setText("" + count);

    }

    public void setFollowers(Set<Follower> followers) {
        this.followers = followers;
    }

    public void setRefreshUI(RefreshUI refreshUI) {
        this.refreshUI = refreshUI;
    }


}
