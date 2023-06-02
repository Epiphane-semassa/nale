package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
import com.nale.newslettermanagement.listeners.RefreshUI;
import com.nale.newslettermanagement.model.Bulletin;
import com.nale.newslettermanagement.model.Follower;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class FollowerSubscriptionsController implements Initializable {

    public VBox bulletinsContainer;
    public Button btnClose;
    public Button btnSubscribe;
    public Button btnQuit;
    public Label processLabel;

    private Follower follower;
    private RefreshUI refreshUI;
    private FileSystemStorage fileSystemStorage;
    private Set<Bulletin> bulletins;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();
        bulletins = fileSystemStorage.loadBulletins();
    }

    public void loadData() {
        bulletinsContainer.getChildren().clear();
        for (Bulletin bulletin : bulletins) {
            addBulletinItems(bulletin);
        }
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }

    public void setRefreshUI(RefreshUI refreshUI) {
        this.refreshUI = refreshUI;
    }

    public void oncClose(ActionEvent actionEvent) {
        closeStage(actionEvent);
        refreshUI.refresh();
    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest(event -> refreshUI.refresh());
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void addBulletinItems(Bulletin bulletin) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(Region.USE_PREF_SIZE);
        anchorPane.setMaxWidth(bulletinsContainer.getMaxWidth());
        anchorPane.setPrefHeight(Region.USE_PREF_SIZE);
        anchorPane.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: white");

        Label label = new Label(bulletin.getName());
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        label.setPrefHeight(Region.USE_COMPUTED_SIZE);
        label.setStyle("-fx-text-fill: #343434; -fx-font-size: 13px");
        AnchorPane.setTopAnchor(label, 20.0);
        AnchorPane.setLeftAnchor(label, 20.0);
        AnchorPane.setBottomAnchor(label, 20.0);
        anchorPane.getChildren().add(label);

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPrefWidth(Region.USE_PREF_SIZE);
        hBox.setPrefHeight(Region.USE_PREF_SIZE);
        AnchorPane.setTopAnchor(hBox, 20.0);
        AnchorPane.setRightAnchor(hBox, 20.0);
        AnchorPane.setBottomAnchor(hBox, 20.0);

        btnSubscribe = new Button("S'abonner");
        btnSubscribe.setMnemonicParsing(false);
        btnSubscribe.setMinWidth(Region.USE_PREF_SIZE);
        btnSubscribe.setStyle("-fx-background-color: #079e52;\n" + "-fx-border-radius: 3px;\n" + "-fx-background-radius: 3px;\n" + "-fx-text-fill: #fff;\n" + "-fx-padding: 8px 16px;");
        btnSubscribe.setOnAction(actionEvent -> subscribeToBulletin(bulletin, actionEvent));

        btnQuit = new Button("Se désabonner");
        btnQuit.setMnemonicParsing(false);
        btnQuit.setMinWidth(Region.USE_PREF_SIZE);
        btnQuit.setStyle("-fx-background-color: #e53935;\n" + "-fx-border-radius: 3px;\n" + "-fx-background-radius: 3px;\n" + "-fx-text-fill: #fff;\n" + "-fx-padding: 8px 16px;");
        btnQuit.setOnAction(actionEvent -> quitBulletin(bulletin, actionEvent));

        hBox.getChildren().addAll(btnSubscribe, btnQuit);
        displayBtns(bulletin, btnSubscribe, btnQuit);
        anchorPane.getChildren().add(hBox);
        bulletinsContainer.getChildren().add(anchorPane);
    }

    private void displayBtns(Bulletin bulletin, Button btnSubscribe, Button btnQuit) {
        if (bulletin.getFollowersIds().contains(follower.getId())) {
            btnQuit.setVisible(true);
            btnSubscribe.setVisible(false);
            btnSubscribe.managedProperty().bind(btnSubscribe.visibleProperty());
        } else {
            btnSubscribe.setVisible(true);
            btnQuit.setVisible(false);
            btnQuit.managedProperty().bind(btnQuit.visibleProperty());
        }
    }

    private void quitBulletin(Bulletin bulletin, ActionEvent actionEvent) {
        processLabel.setText("");
        bulletin.getFollowersIds().remove(follower.getId());
        for (Bulletin bulletin1 : bulletins) {
            if (bulletin1.getId() == bulletin.getId()) {
                bulletin1.setFollowersIds(bulletin.getFollowersIds());
            }
        }
        boolean success = fileSystemStorage.saveBulletins(bulletins);
        if (success) {
            processLabel.setStyle("-fx-text-fill: green");
            processLabel.setText("Désabonnement effectué avec succès");
        } else {
            processLabel.setStyle("-fx-text-fill: #e53935");
            processLabel.setText("Désabonnement échoué. veuillez ressayer plus tard");
        }
        loadData();
    }

    private void subscribeToBulletin(Bulletin bulletin, ActionEvent actionEvent) {
        processLabel.setText("");
        bulletin.getFollowersIds().add(follower.getId());
        for (Bulletin bulletin1 : bulletins) {
            if (bulletin1.getId() == bulletin.getId()) {
                bulletin1.setFollowersIds(bulletin.getFollowersIds());
            }
        }
        boolean success = fileSystemStorage.saveBulletins(bulletins);
        if (success) {
            processLabel.setStyle("-fx-text-fill: green");
            processLabel.setText("Abonnement effectué avec succès");
        } else {
            processLabel.setStyle("-fx-text-fill: #e53935");
            processLabel.setText("Abonnement échoué. veuillez ressayer plus tard");
        }
        loadData();
    }

}
