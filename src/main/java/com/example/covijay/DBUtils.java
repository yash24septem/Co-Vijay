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
        // staging window me dikhne ke liye
        Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,600,400));
        stage.show();
    }
    public static void signUpUser(ActionEvent event,String password,String email){
        Connection connection=null;
        PreparedStatement psInsert=null;
        PreparedStatement psCheckUserExists=null;
        PreparedStatement psCheckUserExistsPassword=null;
        ResultSet resultSet=null;
        ResultSet resultSetPassword=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/co-vijay","root","rootpassword");
            // query ban gaya
            psCheckUserExists=connection.prepareStatement("SELECT password FROM admin where email=?");
            psCheckUserExists.setString(1,email);
            resultSet=psCheckUserExists.executeQuery();
            String pass="";
            while(resultSet.next())
            {
               pass= resultSet.getString("password");
               break;
            }
             if(pass.equalsIgnoreCase(password)) {

                 Parent root=null;
                 try{
                     FXMLLoader loader=new FXMLLoader(HomePageController.class.getResource("adminDashboard.fxml"));
                     root=loader.load();
                     AdminDashboardController adminDashboardController =loader.getController();

                 }
                 catch(IOException e){
                     e.printStackTrace();

                 }
                 Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
                 stage.setTitle("Welcome Admin");
                 stage.setScene(new Scene(root,600,400));
                 stage.show();

             }
             else
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Entered Data is wrong");
                alert.show();
                return;
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
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/co-vijay","root","rootpassword");
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
