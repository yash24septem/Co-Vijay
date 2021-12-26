package com.example.covijay;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminDashboardController implements Initializable {
    @FXML
    private Button logout_admin_dashboard;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        logout_admin_dashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Parent root=null;
                try{
                    FXMLLoader loader=new FXMLLoader(HomePageController.class.getResource("sign-up.fxml"));
                    root=loader.load();
                    SignUpController signUpController =loader.getController();

                }
                catch(IOException e){
                    e.printStackTrace();

                }
                Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Admin Login");
                stage.setScene(new Scene(root,600,400));
                stage.show();

            }
        });

    }

}
