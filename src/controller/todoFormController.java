package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import tm.toDoTM;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class todoFormController {
    public AnchorPane root;
    public Label lblTitle;
    public Label lblUserIID;
    public TextField txtTaskName;
    public Button btnAddToList;
    public Pane subRoot;
    public ListView<toDoTM> listViewToDo;
    public TextField txtSelectedToDo;
    public Button btnDeleteOnClickAction;
    public Button btnUpdateOnClickAction;

    public void initialize(){
        lblTitle.setText("Hi "+ loginFormController.loginUserName + " welcome to to do list");
        lblUserIID.setText(loginFormController.loginUserId);
        subRoot.setVisible(false);
        txtSelectedToDo.requestFocus();
        loadList();

        setdesableCommon(true);

        listViewToDo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<toDoTM>() {
            @Override
            public void changed(ObservableValue<? extends toDoTM> observable, toDoTM oldValue, toDoTM newValue) {
                setdesableCommon(false);

                if (listViewToDo.getSelectionModel().getSelectedItem()==null){
                    return;
                }

                String description = listViewToDo.getSelectionModel().getSelectedItem().getDescription();
                txtSelectedToDo.setText(description);
            }
        });
    }

    public void setdesableCommon(boolean value){
        btnDeleteOnClickAction.setDisable(value);
        btnUpdateOnClickAction.setDisable(value);
        txtSelectedToDo.setDisable(value);
    }

    public void btnOnClickActionDelete(ActionEvent actionEvent) {
        
    }

    public void btnOnClickActionUpdate(ActionEvent actionEvent) {
        String selectedTodo = txtSelectedToDo.getText();
        String id = listViewToDo.getSelectionModel().getSelectedItem().getId();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description=? where id=?");
            preparedStatement.setObject(1,selectedTodo);
            preparedStatement.setObject(2,id);

            preparedStatement.executeUpdate();

            loadList();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnOnClickActionAddNewToDo(ActionEvent actionEvent) {
        subRoot.setVisible(true);
        txtTaskName.requestFocus();
        setdesableCommon(true);
        listViewToDo.getSelectionModel().clearSelection();
    }

    public void btnOnClickActionAddToList(ActionEvent actionEvent) {
        String todoName = txtTaskName.getText();
        String taskId = genarateTaskId();
        String loginUserId = loginFormController.loginUserId;

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values(?,?,?)");
            preparedStatement.setObject(1,taskId);
            preparedStatement.setObject(2,todoName);
            preparedStatement.setObject(3,loginUserId);

            preparedStatement.executeUpdate();

            txtTaskName.clear();
            subRoot.setVisible(false);

            loadList();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnOnClickActionLogOut(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to logout", ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)){
            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LogInForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.centerOnScreen();
        }else {
            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/ToDoForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.centerOnScreen();
        }
    }
    public String genarateTaskId(){
        String id = null;
        int intSub = 0;
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todo order by id desc limit 1");


            if (resultSet.next()){
                id = resultSet.getString(1);
                String substring = id.substring(1, id.length());
                intSub = Integer.parseInt(substring);
                intSub++;

                if (intSub<10){
                    id = "T00"+intSub;
                }else if (intSub<100){
                    id = "T0"+intSub;
                }else if (intSub<1000){
                    id="T"+intSub;
                }
            }else{
                id = "T001";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }
    public void loadList(){
        ObservableList<toDoTM> todos = listViewToDo.getItems();
        todos.clear();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where user_id=?");

            preparedStatement.setObject(1,lblUserIID.getText());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String taskId = resultSet.getString(1);
                String description = resultSet.getString(2);
                String userId = resultSet.getString(3);

                todos.add(new toDoTM(taskId,description,userId));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
