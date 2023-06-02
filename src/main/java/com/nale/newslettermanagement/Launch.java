package com.nale.newslettermanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Launch extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/welcome.fxml")));
        primaryStage.setTitle("Nale");
        Scene scene = new Scene(root, 800, 800);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/views/assets/css/main.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
