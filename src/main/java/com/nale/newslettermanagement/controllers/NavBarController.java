package com.nale.newslettermanagement.controllers;

import com.nale.newslettermanagement.data.Admin;
import com.nale.newslettermanagement.data.FileSystemStorage;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class NavBarController implements Initializable {

    public Label usernameLabel;
    public Label emailLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileSystemStorage fileSystemStorage = FileSystemStorage.getInstance();
        Admin admin = fileSystemStorage.loadCreatorAccount();
        usernameLabel.setText(admin.getProfile().getFirstName() + " " + admin.getProfile().getLastName());
        emailLabel.setText(admin.getProfile().getEmail());

    }
}
