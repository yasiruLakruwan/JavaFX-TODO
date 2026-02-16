package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.*;

public class createNewAccountFormController {
    public AnchorPane root;
    public TextField txtUserName;
    public TextField txtPassword;
    public TextField txtEmail;
    public Button btnOnclickRegister;
    public TextField txtConfirmPassword;
    public Label lblPasswordDoesNotMatched;
    public Button btnAddUser;
    public Label lblUid;

    public void initialize(){
        lblPasswordDoesNotMatched.setVisible(false);
        disableVisible(true);
    }

    public void btnOnClickAcrionRegister(ActionEvent actionEvent) {
        String getPassword = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if(getPassword.equals(confirmPassword)){   // Equals add here....
            passwordChecker("-fx-border-color: transparent;");
            lblPasswordDoesNotMatched.setVisible(false);
        }
        else{ // Not equals add here....
            passwordChecker("-fx-border-color: red;");
            lblPasswordDoesNotMatched.setVisible(true);
        }
        txtPassword.requestFocus();
        register();

    }
    public void passwordChecker(String condition){
        txtPassword.setStyle(condition);
        txtConfirmPassword.setStyle(condition);
    }

    public void btnOnClickActionAddUser(ActionEvent actionEvent) {
        disableVisible(false);
        txtUserName.requestFocus();
        autoGenarateId();
    }
    public void disableVisible(boolean value){
        txtUserName.setDisable(value);
        txtEmail.setDisable(value);
        txtPassword.setDisable(value);
        btnOnclickRegister.setDisable(value);
        txtConfirmPassword.setDisable(value);
    }

    public void autoGenarateId(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select uid from users order by uid desc limit 1");
            boolean next = resultSet.next();

            if(next){
                String columnId = resultSet.getString(1);
                System.out.println(columnId);
                String substring = columnId.substring(1, columnId.length());

                int intNum = Integer.parseInt(substring);
                intNum++;

                if(intNum<10){
                    lblUid.setText("U00"+intNum);
                }else if (intNum<100){
                    lblUid.setText("U0"+intNum);
                }else{
                    lblUid.setText("U"+intNum);
                }

            }else {
                lblUid.setText("U001");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void register(){
        String id = lblUid.getText();
        String username = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users values (?,?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,username);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,password);

            preparedStatement.executeUpdate();

            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LogInForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.centerOnScreen();


        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
}

