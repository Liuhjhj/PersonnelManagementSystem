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

    public BorderPane rootLayout;
    public Label userLabel;
    public Button exit;
    public TabPane tabPane3;
    public TextField username;
    public PasswordField password;
    public PasswordField password2;
    public PasswordField oldPassword;
    public PasswordField newPassword;
    public PasswordField newPassword2;
    public Stage primaryStage;
    public SingleSelectionModel singleSelectionModel;
    public String[] info;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    //初始化
    public void init(){
        singleSelectionModel = tabPane3.getSelectionModel();
        primaryStage = (Stage) rootLayout.getScene().getWindow();   //获取本窗口的Stage实例
        info = (String[]) primaryStage.getUserData();   //info保存登录进来的用户名和密码
        userLabel.setText("尊敬的"+info[0]+", 您好!");
    }

    //退出系统按钮,返回主菜单
    public void exitAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/Login.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            Login login = loader.getController();
            login.init();
            login.username.setText(info[0]);
            login.password.requestFocus();
            stage.setTitle("人事管理系统");
            stage.show();
            primaryStage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //左侧菜单栏创建用户按钮
    public void createUserBtnAction(ActionEvent actionEvent) {
        singleSelectionModel.select(0);
    }

    //左侧菜单栏修改密码按钮
    public void modifyPasswordBtnAction(ActionEvent actionEvent) {
        singleSelectionModel.select(1);
    }

    //左侧菜单栏删除用户按钮
    public void deleteUserBtnAction(ActionEvent actionEvent) {
        singleSelectionModel.select(2);
    }

    //右侧密码修改页重置按钮
    public void reset2Action(ActionEvent actionEvent) {
        oldPassword.setText("");
        newPassword.setText("");
        newPassword2.setText("");
    }

    //右侧创建用户页重置按钮
    public void resetAction(ActionEvent actionEvent) {
        username.setText("");
        password.setText("");
        password2.setText("");
    }

    //右侧创建用户页创建普通用户按钮
    public void createUserAction(ActionEvent actionEvent) {
        //用户名,密码均不为空且输入的两个密码的值相等
        if (verifyText(username.getText(), password.getText(), password2.getText())){
            Connection connection;
            Statement statement;
            try {   //连接数据库
                connection = Sql.getConnection(info[0], info[1]);
                assert connection != null;
                statement = connection.createStatement();
            }catch (Exception e){
                e.printStackTrace();
                AlertDiaog.alert(0, "错误", "创建用户失败", "连接数据库失败");
                return;
            }
            if (!Sql.createUser(statement, username.getText(), password.getText())) {   //创建用户失败,已存在该用户名的用户
                AlertDiaog.alert(0, "错误", "创建用户失败", "你没有权限,用户已存在或密码格式不正确");
                return;
            }
            Sql.disconnect(connection);
            AlertDiaog.alert(3, "成功", "创建用户成功");
        }else { //文本框有空值或两次输入的密码不匹配
            AlertDiaog.alert(0, "错误", "创建用户失败", "用户名或密码为空或两次输入的密码不匹配");
        }
        resetAction(actionEvent);
    }

    //右侧创建用户页创建管理员按钮
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
            boolean bool = false;   //判断该用户是否存在,若不存在则创建用户
            if (Sql.createUser(statement, username.getText(), password.getText())) {
                bool = true;
            }
            if (Sql.createAdmin(statement, username.getText())) {   //给该用户提升权限
                if (!bool) {    //该用户不存在,先创建用户再提权
                    AlertDiaog.alert(3, "成功", "添加管理员成功", "已将该用户提升为管理员");
                } else {    //该用户已存在,创建用户失败然后提权
                    AlertDiaog.alert(3, "成功", "创建管理员成功", "已创建新的管理员用户");
                }
            } else {    //登录的账户没有对数据库进行创建管理员的权限
                AlertDiaog.alert(0, "错误", "创建管理员失败", "授予权限失败");
            }
            Sql.disconnect(connection);
        }else {
            AlertDiaog.alert(0, "错误", "创建管理员失败", "用户名或密码为空或两次输入的密码不匹配");
        }
        resetAction(actionEvent);
    }

    //验证三个输入框是否为空且后两个输入框文本是否相等
    public boolean verifyText(String str1, String str2, String str3){
        if (str1.equals("")||str2.equals("")||str3.equals(""))
            return false;
        else
            return str3.equals(str2);
    }

    //右侧密码修改页确认修改按钮
    public void confirmAction(ActionEvent actionEvent) {
        if (verifyText(oldPassword.getText(), newPassword.getText(), newPassword2.getText())){
            if (oldPassword.getText().equals(info[1])){   //原密码匹配
                Connection connection;
                Statement statement;
                try{
                    connection = Sql.getConnection(info[0], info[1]);
                    assert connection != null;
                    statement = connection.createStatement();
                }catch (Exception e){
                    e.printStackTrace();
                    AlertDiaog.alert(0, "错误", "连接数据库失败");
                    return;
                }
                if (Sql.changePassword(statement, info[0], newPassword.getText())){
                    AlertDiaog.alert(3, "成功", "密码修改成功");
                }else {
                    AlertDiaog.alert(0, "错误", "密码修改失败");
                }
                Sql.disconnect(connection);
            }else { //原密码不匹配
                AlertDiaog.alert(0, "错误", "输入的原密码不正确");
            }
        }else {
            AlertDiaog.alert(0, "错误", "有空值或两次输入的密码不匹配");
        }
    }
}
