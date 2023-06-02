package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.Admin;
import com.nale.newslettermanagement.data.FileSystemStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    public AnchorPane navBarView;
    public BorderPane containerPane;
    public ToggleGroup dashMenu;
    public ToggleButton bulletinBtn;
    public ToggleButton followerBtn;

    private Parent bulletinManager;
    private Parent followerManager;

    private BulletinController bulletinController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashMenu.selectToggle(bulletinBtn);
    }

    public void onBulletin(ActionEvent actionEvent) {
        if (bulletinController == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/bulletin.fxml"));
                bulletinManager = loader.load();
                bulletinController = loader.getController();
                bulletinController.setDashContainerPane(containerPane);
                bulletinController.setRootContainer(bulletinManager);
                bulletinController.loadData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        containerPane.setCenter(bulletinManager);
    }

    public void onFollower(ActionEvent actionEvent) {
        if (followerManager == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/follower.fxml"));
                followerManager = loader.load();
                FollowerController followerController = loader.getController();
                followerController.loadData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        containerPane.setCenter(followerManager);
    }
}
