package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginFormController {
    public Label createNewAccountOnclick;
    public TextField txtFieldUsername;
    public PasswordField txtFieldPassword;
    public AnchorPane root;

    public static String loginUserName;
    public static String loginUserId;

    public void onMluseClickedCreateNewAccount(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/createNewAccountForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage =(Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create new account form");
        primaryStage.centerOnScreen();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        String uname = txtFieldUsername.getText();
        String pward = txtFieldPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where name=? and password=?");
            preparedStatement.setObject(1,uname);
            preparedStatement.setObject(2,pward);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){

                loginUserId = resultSet.getString(1);
                loginUserName = resultSet.getString(2);

                Parent parent = FXMLLoader.load(getClass().getResource("../view/ToDoForm.fxml"));
                Scene scene = new Scene(parent);
                Stage primaryStage =(Stage) root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Create new account form");
                primaryStage.centerOnScreen();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password doesnot matched");
                alert.showAndWait();

                txtFieldUsername.clear();
                txtFieldPassword.clear();
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
}








