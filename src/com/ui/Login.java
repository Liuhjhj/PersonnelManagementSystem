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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    public AnchorPane rootLayout;
    public Stage primaryStage;

    public void loginAction(ActionEvent actionEvent) {
        loginDatabase();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void exitAction(ActionEvent actionEvent) {
        primaryStage.close();
    }

    public void loginKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            loginDatabase();
        }
    }

    public void loginDatabase(){
        Connection connection = Sql.getConnection(username.getText(), password.getText());
        if (connection != null){
            Sql.disconnect(connection);
            String[] info = {username.getText(), password.getText()};
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/Menu.fxml"));
                Parent parent = loader.load();
                stage.setScene(new Scene(parent));
                stage.setTitle("人事管理系统");
                stage.setUserData(info);
                stage.setResizable(false);
                Menu menu = loader.getController();
                menu.init();
                stage.show();
                primaryStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            AlertDiaog.alert(0, "登录数据库失败", "用户名或密码错误");
        }
    }

    public void init(){
        primaryStage = (Stage)rootLayout.getScene().getWindow();
    }
}
