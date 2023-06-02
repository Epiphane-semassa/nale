package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.Admin;
import com.nale.newslettermanagement.data.FileSystemStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {


    public ProgressIndicator progressRound;
    public Button configAccountBtn;
    public Button dashBtn;
    public VBox containerBox;
    public HBox progressContainer;

    private CreatorSettingsController creatorSettingsController;
    private DashboardController dashboardController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileSystemStorage fileSystemStorage = FileSystemStorage.getInstance();
        fileSystemStorage.init();
        Admin admin = fileSystemStorage.loadCreatorAccount();

        dashBtn.setVisible(false);
        dashBtn.managedProperty().bind(dashBtn.visibleProperty());
        configAccountBtn.setVisible(false);
        configAccountBtn.managedProperty().bind(configAccountBtn.visibleProperty());

        try {
            Thread.sleep(2000);
            progressContainer.setVisible(false);
            progressContainer.managedProperty().bind(progressContainer.visibleProperty());
            if (admin != null) {
                configAccountBtn.setVisible(false);
                configAccountBtn.managedProperty().bind(configAccountBtn.visibleProperty());
                dashBtn.setVisible(true);
            } else {
                configAccountBtn.setVisible(true);
                dashBtn.setVisible(false);
                dashBtn.managedProperty().bind(dashBtn.visibleProperty());
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

    }

    public void configCreatorAccount(ActionEvent actionEvent) throws IOException {
        Stage configStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/creator-settings.fxml")));
        configStage.setTitle("Configuration");
        Scene scene = new Scene(root);
        configStage.setScene(scene);
        configStage.setMaximized(false);
        configStage.show();
        Stage stage = (Stage) configAccountBtn.getScene().getWindow();
        stage.close();

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
        Stage stage = (Stage) dashBtn.getScene().getWindow();
        stage.close();
    }
}
