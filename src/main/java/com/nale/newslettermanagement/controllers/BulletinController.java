package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
import com.nale.newslettermanagement.listeners.RefreshUI;
import com.nale.newslettermanagement.model.Bulletin;
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

public class BulletinController implements Initializable, RefreshUI {

    public Label nbLabel;
    public BorderPane contentBorder;
    public ProgressIndicator loaderProgress;
    public AnchorPane container;

    private Set<Bulletin> bulletins = new HashSet<>();
    private FileSystemStorage fileSystemStorage;
    private int lastColumn;
    private int lastRow;
    private BorderPane dashContainerPane;
    private Parent rootContainer;

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
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        AnchorPane.setTopAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);
        AnchorPane.setBottomAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        loaderProgress.setVisible(true);
        bulletins = fileSystemStorage.loadBulletins();
        loaderProgress.setVisible(false);
        loaderProgress.managedProperty().bind(loaderProgress.visibleProperty());

        nbLabel.setText(bulletins.size() + " bulletin" + ((bulletins.size() > 1) ? "s" : ""));
        for (Bulletin bulletin : bulletins) {
            addItem(bulletin, gridPane);
        }
        container.getChildren().add(gridPane);
    }

    public void onRefresh(ActionEvent actionEvent) {
        loadData();
    }

    public void onAdd(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/edit-bulletin.fxml"));
        Parent parent = fxmlLoader.load();
        Bulletin bulletin = new Bulletin();
        EditBulletinController editBulletinController = fxmlLoader.getController();
        editBulletinController.setBulletin(bulletin);
        editBulletinController.setRefreshUI(this);
        editBulletinController.setBulletins(bulletins);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(Util.formatText("Ajouter un bulletin"));
        stage.setResizable(false);
        stage.setScene(scene);
        editBulletinController.setStage(stage);
        stage.showAndWait();
    }

    private void addItem(Bulletin bulletin, GridPane gridPane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/bulletin-item.fxml"));
            Parent productNode = loader.load();
            BulletinItemController itemController = loader.getController();
            itemController.setBulletin(bulletin);
            itemController.setBulletins(bulletins);
            itemController.setRefreshUI(this);
            itemController.setDashContainerPane(getDashContainerPane());
            itemController.setBulletinsContainerPane(getRootContainer());
            gridPane.add(productNode, lastColumn, lastRow);
            if (lastColumn < 2) {
                lastColumn ++ ;
            } else {
                lastColumn = 0;
                lastRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() {
        loadData();
    }


    public BorderPane getDashContainerPane() {
        return dashContainerPane;
    }

    public void setDashContainerPane(BorderPane dashContainerPane) {
        this.dashContainerPane = dashContainerPane;
    }

    public Parent getRootContainer() {
        return rootContainer;
    }

    public void setRootContainer(Parent rootContainer) {
        this.rootContainer = rootContainer;
    }
}
