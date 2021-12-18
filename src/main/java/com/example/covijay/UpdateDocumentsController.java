package com.example.covijay;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateDocumentsController implements Initializable {
    @FXML
    private Button button_cancel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"logged-in.fxml","Welcome!!",null,null,null);

            }
        });

    }
}
