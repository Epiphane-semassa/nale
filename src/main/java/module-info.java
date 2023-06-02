module newslettermanagement {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.bootstrapicons;

    opens com.nale.newslettermanagement to javafx.fxml;
    opens com.nale.newslettermanagement.controllers to javafx.fxml, com.google.gson;
    opens com.nale.newslettermanagement.data to com.google.gson;
    opens com.nale.newslettermanagement.model to com.google.gson;
    exports com.nale.newslettermanagement;
}
