package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
import com.nale.newslettermanagement.listeners.RefreshUI;
import com.nale.newslettermanagement.model.Bulletin;
import com.nale.newslettermanagement.model.Follower;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class FollowerController implements Initializable, RefreshUI {
    public Label nbLabel;
    public BorderPane contentBorder;
    public ProgressIndicator loaderProgress;
    public AnchorPane container;

    private Set<Follower> followers = new HashSet<>();
    private FileSystemStorage fileSystemStorage;
    private int lastColumn;
    private int lastRow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();
        loadData();
    }

    public void loadData() {
        container.getChildren().clear();
        lastColumn = 0;
        lastRow = 0;
        GridPane gridPane = new GridPane();
        gridPane.setHgap(30);
        gridPane.setVgap(30);
        AnchorPane.setTopAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);
        AnchorPane.setBottomAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        loaderProgress.setVisible(true);
        followers = fileSystemStorage.loadFollowers();
        loaderProgress.setVisible(false);
        loaderProgress.managedProperty().bind(loaderProgress.visibleProperty());

        nbLabel.setText(followers.size() + " abonné" + ((followers.size() > 1) ? "s" : ""));
        for (Follower follower : followers) {
            addItem(follower, gridPane);
        }
        container.getChildren().add(gridPane);
    }

    private void addItem(Follower follower, GridPane gridPane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/follower-item.fxml"));
            Parent productNode = loader.load();
            FollowerItemController itemController = loader.getController();
            itemController.setFollower(follower);
            itemController.setFollowers(followers);
            itemController.setRefreshUI(this);
            gridPane.add(productNode, lastColumn, lastRow);
            if (lastColumn < 1) {
                lastColumn ++ ;
            } else {
                lastColumn = 0;
                lastRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRefresh(ActionEvent actionEvent) {
        loadData();
    }

    public void onAdd(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/edit-follower.fxml"));
        Parent parent = fxmlLoader.load();
        EditFollowerController editFollowerController = fxmlLoader.getController();
        editFollowerController.setFollower(new Follower());
        editFollowerController.setRefreshUI(this);
        editFollowerController.setFollowers(followers);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Util.formatText("Ajouter un abonné"));
        stage.setResizable(false);
        stage.setScene(scene);
        editFollowerController.setStage(stage);
        stage.showAndWait();
    }

    @Override
    public void refresh() {
        loadData();
    }
}
