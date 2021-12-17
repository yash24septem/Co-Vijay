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

public class DBUtils {
    public static void changeScene(ActionEvent event,String fxmlFile,String title,String username,String phone,String email){
        Parent root=null;
        if(username!=null){
            try{
                FXMLLoader loader=new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root=loader.load();
                LoggedInController loggedInController=loader.getController();
                loggedInController.setUserInformation(username,phone);
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
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/co-vijay","root","root");
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

                changeScene(event,"logged-in.fxml","Welcome!",username,phone,email);
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
    public static void logInUser(ActionEvent event,String username,String password){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/co-vijay","root","root");
            preparedStatement=connection.prepareStatement("SELECT password,phone,email FROM users where username=?");
            preparedStatement.setString(1,username);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.isBeforeFirst()){
                System.out.println("User not found");
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            }
            else{
                while(resultSet.next()){
                    String retrievedPassword=resultSet.getString("password");
                    String retrievedPhone=resultSet.getString("phone");
                    String retrievedemail=resultSet.getString("email");
                    if(retrievedPassword.equals(password)){
                        changeScene(event,"logged-in.fxml","Welcome!",username,retrievedPhone,retrievedemail);
                    }
                    else {
                        System.out.println("password not matched!");
                        Alert alert=new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials are incorrect");
                        alert.show();
                    }
                }
            }

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


}
