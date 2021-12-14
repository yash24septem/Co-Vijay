module com.example.covijay {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.covijay to javafx.fxml;
    exports com.example.covijay;
}