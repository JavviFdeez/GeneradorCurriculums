module org.JavviFdeez {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;

    opens org.JavviFdeez to javafx.fxml;
    exports org.JavviFdeez;
}
