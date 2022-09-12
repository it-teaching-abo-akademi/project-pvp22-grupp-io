module pvp.cashier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires pvp.models;

    opens pvp.cashier to javafx.fxml;
    exports pvp.cashier;
    exports pvp.cashier.models;
    exports pvp.cashier.controllers;
    opens pvp.cashier.controllers to javafx.fxml;
    opens pvp.cashier.models to javafx.fxml;
}