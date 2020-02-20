package com.ui;

import com.Sql;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class Login implements Initializable{

    public Button login;
    public Button exit;
    public TextField username;
    public PasswordField password;

    public void loginAction(ActionEvent actionEvent) {
        Connection connection = Sql.connect(username.getText(), password.getText());
        if (connection != null){
            System.out.println("SQL Database Connected!");
            Sql.disconnect(connection);
        }else {
            AlertDiaog.alert("登录数据库失败", "用户名或密码错误");
        }
    }

    public void exitAction(Parent root, Stage primaryStage){
        exit = (Button)root.lookup("#exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
