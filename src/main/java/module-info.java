module com.example.covijay {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
//    requires j2ee;
    requires mail;

    opens com.example.covijay to javafx.fxml;
    exports com.example.covijay;
}