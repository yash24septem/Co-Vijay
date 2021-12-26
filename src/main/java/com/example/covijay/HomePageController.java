package com.example.covijay;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable{
    @FXML
    private Button btn_login, btn_otp, btn_sign_up;
    @FXML
    private Label message;
    @FXML
    private TextField tf_mail, tf_adhaar, tf_otp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_login.setOnAction(event -> {
            if(tf_otp.getText().trim().isEmpty()||tf_adhaar.getText().trim().isEmpty()||tf_mail.getText().isEmpty()){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter valid credentials");
                alert.show();
            }else DBUtils.logInUser(event, tf_mail.getText(), tf_adhaar.getText(), tf_otp.getText());
        });
        btn_otp.setOnAction(event -> {
            if(tf_adhaar.getText().trim().isEmpty()||tf_mail.getText().isEmpty()){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter valid credentials");
                alert.show();
                System.out.println("Please enter valid credentials");
            }else{
                String email = tf_mail.getText();
                boolean response = DBUtils.sendOtp(email);
                message.setVisible(true);
                if(response){
                    message.setTextFill(Color.color(0,1,0));
                    message.setText("OTP has been sent to you through the email provided. Please enter otp below to login!");
                }else {
                    message.setTextFill(Color.color(1,0,0));
                    message.setText("Server error please try again!");
                }
            }
        });
        btn_sign_up.setOnAction(event -> {
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

        });

    }
}