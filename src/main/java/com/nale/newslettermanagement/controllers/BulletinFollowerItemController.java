package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.model.Follower;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BulletinFollowerItemController implements Initializable {


    public Label lastnameLabel;
    public Label firstnameLabel;
    public Label addressLabel;
    public Label emailLabel;
    public Label phoneLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setFollower(Follower follower) {
        firstnameLabel.setText(follower.getFirstName());
        lastnameLabel.setText(follower.getLastName());
        emailLabel.setText(follower.getEmail());
        phoneLabel.setText(follower.getPhoneNumber());
        addressLabel.setText(follower.getAddress());
    }
}
