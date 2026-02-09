package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class loginFormController {
    public Label createNewAccountOnclick;
    public TextField txtFieldUsername;
    public PasswordField txtFieldPassword;
    public AnchorPane root;

    public void onMluseClickedCreateNewAccount(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/createNewAccountForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage =(Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create new account form");
        primaryStage.centerOnScreen();
    }
}
