package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.sql.Connection;

public class createNewAccountFormController {
    public AnchorPane root;
    public TextField txtUserName;
    public TextField txtPassword;
    public TextField txtEmail;
    public Button btnOnclickRegister;
    public TextField txtConfirmPassword;
    public Label lblPasswordDoesNotMatched;

    public void initialize(){
        lblPasswordDoesNotMatched.setVisible(false);
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

        DBConnection object = DBConnection.getInstance();
        Connection connection = object.getConnection();
        System.out.println(connection);
    }
    public void passwordChecker(String condition){
        txtPassword.setStyle(condition);
        txtConfirmPassword.setStyle(condition);
    }
}









