module org.example.mapagrafos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.example.mapagrafos to javafx.fxml;
    exports org.example.mapagrafos;

    opens org.example.mapagrafos.structures to javafx.fxml;
    exports org.example.mapagrafos.structures;
    exports org.example.mapagrafos.util;
    opens org.example.mapagrafos.util to javafx.fxml;
    exports org.example.mapagrafos.model;
    opens org.example.mapagrafos.model to javafx.fxml;
    exports org.example.mapagrafos.controller;
    opens org.example.mapagrafos.controller to javafx.fxml;
    exports org.example.mapagrafos.exceptions to javafx.fxml;
    opens org.example.mapagrafos.exceptions to javafx.fxml;
}