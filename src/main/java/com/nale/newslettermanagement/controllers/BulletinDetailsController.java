package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.FileSystemStorage;
import com.nale.newslettermanagement.listeners.CallBack;
import com.nale.newslettermanagement.listeners.RefreshUI;
import com.nale.newslettermanagement.model.Bulletin;
import com.nale.newslettermanagement.model.Follower;
import com.nale.newslettermanagement.model.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BulletinDetailsController implements RefreshUI, Initializable {


    public Label peopleTouchedErrorLabel;
    public TextField peopleTouchedField;
    public Label messageErrorLabel;
    public TextField messageField;
    public VBox messagesBox;
    public VBox followersBox;
    public Label nbMessagesLabel;
    public Label nbFollowersLabel;
    public Label descriptionLabel;
    public Label nameLabel;

    private Bulletin bulletin;

    private Set<Follower> followers = new HashSet<>();
    private Set<Message> messages = new HashSet<>();
    private Set<Bulletin> bulletins;
    private FileSystemStorage fileSystemStorage;
    private CallBack callBack;
    private int lastColumn;
    private int lastRow;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileSystemStorage = FileSystemStorage.getInstance();
        bulletins = fileSystemStorage.loadBulletins();
        Util.formatNumberInput(peopleTouchedField);
    }

    public void onAddFollower(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/edit-follower.fxml"));
        Parent parent = fxmlLoader.load();
        EditFollowerController editFollowerController = fxmlLoader.getController();
        editFollowerController.setFollower(new Follower());
        editFollowerController.setRefreshUI(this);
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
                }
            }
            fileSystemStorage.saveBulletins(bulletins);
            bulletins = fileSystemStorage.loadBulletins();
            nbFollowersLabel.setText(bulletin.getFollowersIds().size() + "");
            loadFollowers();
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        messageErrorLabel.setText("");
        peopleTouchedErrorLabel.setText("");
        boolean hasError = false;

        if (messageField.getText().trim().isEmpty()) {
            hasError = true;
            messageErrorLabel.setText("Champ obligatoire");
        }
        if (peopleTouchedField.getText().trim().isEmpty()) {
            hasError = true;
            peopleTouchedErrorLabel.setText("Champ obligatoire");
        }

        if (hasError) {
            return;
        }

        Message message = new Message(UUID.randomUUID());
        message.setBulletinId(bulletin.getId());
        message.setContent(Util.formatText(messageField.getText().trim()));
        message.setPeopleTouched(Long.parseLong(peopleTouchedField.getText().trim()));

        boolean success = fileSystemStorage.saveMessage(message);
        if (success) {
            loadMessages();
            messageField.setText("");
            peopleTouchedField.setText("");
            messageErrorLabel.setText("");
            peopleTouchedErrorLabel.setText("");

        }else {
            messageErrorLabel.setText("Errreur d'envoi du message, veuillez ressayer plus tard.");
        }
    }

    public void goBack(ActionEvent actionEvent) {
        callBack.onCall();
    }

    public Bulletin getBulletin() {
        return bulletin;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
        nameLabel.setText(bulletin.getName());
        descriptionLabel.setText(bulletin.getDescription());
        nbFollowersLabel.setText(bulletin.getFollowersIds().size() + "");
        loadData();
    }

    private void loadFollowers() {
        followersBox.getChildren().clear();
        followers.clear();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setMaxWidth(Region.USE_PREF_SIZE);
        lastColumn = 0;
        lastRow = 0;

        Set<Follower> followersData = fileSystemStorage.loadFollowers();
        for (UUID followersId : bulletin.getFollowersIds()) {
            Optional<Follower> followerOptional = followersData.stream().filter(follower -> follower.getId().equals(followersId)).findAny();
            followerOptional.ifPresent(follower -> followers.add(follower));
        }

        assert followers.size() > 0;
        for (Follower follower : followers) {
            addFollowerItem(follower, gridPane);
        }
        followersBox.getChildren().add(gridPane);
    }

    private void addFollowerItem(Follower follower, GridPane gridPane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/bulletin-follower-item.fxml"));
            Parent productNode = loader.load();
            BulletinFollowerItemController bulletinFollowerItemController = loader.getController();
            bulletinFollowerItemController.setFollower(follower);
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

    private void loadMessages() {
        messagesBox.getChildren().clear();
        messages.clear();
        for (Message loadMessage : fileSystemStorage.loadMessages()) {
            if (loadMessage.getBulletinId().equals(bulletin.getId())) {
                messages.add(loadMessage);
            }
        }

        assert messages.size() > 0;
        nbMessagesLabel.setText(messages.size() + "");
        for (Message message : messages) {
            addMessageItem(message);
        }
    }

    private void addMessageItem(Message message) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(Region.USE_PREF_SIZE);
        anchorPane.setMinWidth(400.0);
        //anchorPane.setPrefWidth(Region.USE_PREF_SIZE);
        anchorPane.setStyle("-fx-background-radius: 15px; -fx-border-radius: 5px; -fx-background-color: #fefefe; -fx-text-fill: white");

        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10.0));
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setStyle("-fx-background-color: #57104586;-fx-background-radius: 12px; -fx-border-radius: 2px;");
        AnchorPane.setTopAnchor(vBox, 5.0);
        AnchorPane.setBottomAnchor(vBox, 5.0);
        AnchorPane.setLeftAnchor(vBox, 5.0);
        AnchorPane.setRightAnchor(vBox, 5.0);
        anchorPane.getChildren().add(vBox);

        Label content = new Label(message.getContent());
        content.setPrefWidth(Region.USE_COMPUTED_SIZE);
        content.setPrefHeight(Region.USE_PREF_SIZE);
        content.setMaxWidth(Region.USE_COMPUTED_SIZE);
        content.setTextAlignment(TextAlignment.RIGHT);
        content.setStyle("-fx-text-fill: #000 !important; -fx-font-size: 13px;");
        vBox.getChildren().add(content);

        Label peopleTouched = new Label("Personnes touchées : " + message.getPeopleTouched());
        peopleTouched.setPrefWidth(Region.USE_COMPUTED_SIZE);
        peopleTouched.setPrefHeight(Region.USE_COMPUTED_SIZE);
        peopleTouched.setAlignment(Pos.CENTER_RIGHT);
        peopleTouched.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
        peopleTouched.setFont(new Font("italic", 13));
        vBox.getChildren().add(peopleTouched);

        messagesBox.getChildren().add(anchorPane);
    }

    public void loadData() {
        loadFollowers();
        loadMessages();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void refresh() {

    }
}
