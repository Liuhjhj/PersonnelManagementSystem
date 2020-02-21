package com.ui;

import com.Sql;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Menu implements Initializable {
    public Stage primaryStage;
    public BorderPane rootLayout;
    public Label userLabel;
    public Button exit;
    public TabPane tabPane3;
    public TextField username;
    public PasswordField password;
    public PasswordField password2;
    public SingleSelectionModel singleSelectionModel;
    public String[] info;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void init(){
        singleSelectionModel = tabPane3.getSelectionModel();
        primaryStage = (Stage) rootLayout.getScene().getWindow();
        info = (String[]) primaryStage.getUserData();
        userLabel.setText("尊敬的"+info[0]+", 您好!");
    }

    public void exitAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/Login.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            Login login = loader.getController();
            login.init();
            stage.setTitle("人事管理系统");
            stage.show();
            primaryStage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createUserBtnAction(ActionEvent actionEvent) {
        singleSelectionModel.select(0);
    }

    public void modifyPasswordBtnAction(ActionEvent actionEvent) {
        singleSelectionModel.select(1);
    }

    public void deleteUserBtnAction(ActionEvent actionEvent) {
        singleSelectionModel.select(2);
    }

    public void reset2Action(ActionEvent actionEvent) {
    }

    public void resetAction(ActionEvent actionEvent) {
        username.setText("");
        password.setText("");
        password2.setText("");
    }

    public void createUserAction(ActionEvent actionEvent) {
        if (verifyText(username.getText(), password.getText(), password2.getText())){
            Connection connection;
            Statement statement;
            try {
                connection = Sql.getConnection(info[0], info[1]);
                assert connection != null;
                statement = connection.createStatement();
            }catch (Exception e){
                e.printStackTrace();
                AlertDiaog.alert(0, "错误", "创建用户失败", "连接数据库失败");
                return;
            }
            if (!Sql.createUser(statement, username.getText(), password.getText())) {
                AlertDiaog.alert(0, "错误", "创建用户失败", "你没有权限,用户已存在或密码格式不正确");
                return;
            }
            Sql.disconnect(connection);
            AlertDiaog.alert(3, "成功", "创建用户成功");
        }else {
            AlertDiaog.alert(0, "错误", "创建用户失败", "用户名或密码为空或两次输入的密码不匹配");
        }
        resetAction(actionEvent);
    }

    public void createAdminAction(ActionEvent actionEvent) {
        if (verifyText(username.getText(), password.getText(), password2.getText())) {
            Connection connection;
            Statement statement;
            try {
                connection = Sql.getConnection(info[0], info[1]);
                assert connection != null;
                statement = connection.createStatement();
            } catch (Exception e) {
                e.printStackTrace();
                AlertDiaog.alert(0, "错误", "创建管理员失败", "连接数据库失败");
                return;
            }
            boolean bool = false;
            if (Sql.createUser(statement, username.getText(), password.getText())) {
                bool = true;
            }
            if (Sql.createAdmin(statement, username.getText())) {
                if (!bool) {
                    AlertDiaog.alert(3, "成功", "添加管理员成功", "已将该用户提升为管理员");
                } else {
                    AlertDiaog.alert(3, "成功", "创建管理员成功", "已创建新的管理员用户");
                }
            } else {
                AlertDiaog.alert(0, "错误", "创建管理员失败", "授予权限失败");
            }
            Sql.disconnect(connection);
        }else {
            AlertDiaog.alert(0, "错误", "创建管理员失败", "用户名或密码为空或两次输入的密码不匹配");
        }
        resetAction(actionEvent);
    }

    public boolean verifyText(String str1, String str2, String str3){
        if (str1.equals("")||str2.equals("")||str3.equals(""))
            return false;
        else
            return str3.equals(str2);
    }
}
