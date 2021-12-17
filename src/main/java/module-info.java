module com.example.covijay {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.covijay to javafx.fxml;
    exports com.example.covijay;
}