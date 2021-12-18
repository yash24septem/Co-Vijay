package com.example.covijay;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class DBUtils {
    public static void changeScene(ActionEvent event,String fxmlFile,String title,String username,String phone,String email){
        Parent root=null;
        if(username!=null){
            try{
                FXMLLoader loader=new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root=loader.load();
                UserDashboardController userDashboardController =loader.getController();
                userDashboardController.setUserInformation(username,phone);
            }
            catch(IOException e){
                e.printStackTrace();

            }
        }
        else{
            try{
                root=FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }
            catch(IOException e){
                e.printStackTrace();

            }
        }

        Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,600,400));
        stage.show();
    }
    public static void signUpUser(ActionEvent event,String username,String password,String phone,String email){
        Connection connection=null;
        PreparedStatement psInsert=null;
        PreparedStatement psCheckUserExists=null;
        ResultSet resultSet=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/co-vijay","root","satya");
            psCheckUserExists=connection.prepareStatement("SELECT * FROM users where username=?");
            psCheckUserExists.setString(1,username);
            resultSet=psCheckUserExists.executeQuery();
            if(resultSet.isBeforeFirst()){
                System.out.println("User already exists!");
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();

            }
            else{
                psInsert=connection.prepareStatement("INSERT INTO users (username,password,phone,email) VALUES(?,?,?,?)");
                psInsert.setString(1,username);
                psInsert.setString(2,password);
                psInsert.setString(3,phone);
                psInsert.setString(4,email);
                psInsert.executeUpdate();

                changeScene(event,"userDashboard.fxml","Welcome!",username,phone,email);
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        finally {
            if(resultSet!=null){
                try {
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists!=null){
                try {
                    psCheckUserExists.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psInsert!=null){
                try {
                    psInsert.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }



        }
    }


    //Login of user via otp
    private static String otp = "";
    public static void logInUser(ActionEvent event,String email,String adhaar, String otpUser){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        PreparedStatement psInsert=null;
        ResultSet resultSet=null;
        System.out.println(otpUser);
        if(!otp.equals(otpUser)){

            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Entered otp is wrong");
            alert.show();
            return;
        }
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/co-vijay","root","satya");
            preparedStatement=connection.prepareStatement("SELECT adhaar FROM users where email=?");
            preparedStatement.setString(1,email);
            resultSet=preparedStatement.executeQuery();
            if(!resultSet.next()){
                psInsert=connection.prepareStatement("INSERT INTO users (email,adhaar) VALUES(?,?)");
                psInsert.setString(1,email);
                psInsert.setString(2,adhaar);
                psInsert.executeUpdate();
                System.out.println("Entry added");
            }
            System.out.println("logged in");
            changeScene(event,"userDashboard.fxml","Welcome!",null,null,email);

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();

        }
        finally {
            if(resultSet!=null){
                try {
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(preparedStatement!=null){
                try {
                    preparedStatement.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(connection!=null){
                try {
                    connection.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    private static String generate(int len) {
            String numbers = "0123456789";
            Random rndm_method = new Random();
            char[] otp = new char[len];
            for (int i = 0; i < len; i++)
            {
                otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
            }
            return String.valueOf(otp);
    }

    public static boolean sendOtp(String email){
        otp = generate(4);
        SendEmail sendEmail = new SendEmail(email, otp);
        return sendEmail.sendMail();
    }


}
