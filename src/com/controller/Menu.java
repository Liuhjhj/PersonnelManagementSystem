package com.controller;

import com.AlertDiaog;
import com.Sql;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu implements Initializable {

    public final int totalLayout = 6;   //管理页面总数

    public BorderPane rootLayout;   //布局的父目录
    public VBox userManage; //员工管理页面
    public VBox department; //部门管理页面
    public VBox salary; //薪资管理页面
    public VBox attendance; //考勤管理页面
    public VBox createUser; //创建用户页面
    public VBox modifyPassword; //修改密码页面
    public VBox dropUser;   //删除用户页面
    List<VBox> VBoxList;    //页面管理List
    public Label userLabel; //
    public JFXButton userManageBtn; //员工管理按钮
    public JFXButton departmentBtn; //部门管理按钮
    public JFXButton salaryBtn; //薪资管理按钮
    public JFXButton attendanceBtn; //考勤管理按钮
    public JFXButton exit;  //退出系统按钮
    public JFXButton dropBtn;   //删除用户按钮
    public TextField username;
    public JFXPasswordField password;
    public JFXPasswordField password2;
    public JFXPasswordField oldPassword;
    public JFXPasswordField newPassword;
    public JFXPasswordField newPassword2;
    public JFXComboBox selectUserCombobox;

    public int dropBtnStatus = 0;
    public Stage primaryStage;
    public String[] info;
    public Connection connection;
    public Statement statement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    //初始化
    public void init(){
        primaryStage = (Stage) rootLayout.getScene().getWindow();   //获取本窗口的Stage实例
        info = (String[]) primaryStage.getUserData();   //info保存登录进来的用户名和密码
        userLabel.setText("尊敬的"+info[0]+", 您好!");
        VBoxList = new ArrayList<>();
        VBoxList.add(userManage);
        VBoxList.add(department);
        VBoxList.add(salary);
        VBoxList.add(attendance);
        VBoxList.add(createUser);
        VBoxList.add(modifyPassword);
        VBoxList.add(dropUser);
        for (int i = 0; i<=totalLayout; i++)
            VBoxList.get(i).setVisible(false);
    }

    //退出系统按钮,返回主菜单
    public void exitAction(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fxml/Login_MD.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            Login login = loader.getController();
            login.init();
            login.username.setText(info[0]);
            login.password.requestFocus();
            stage.setTitle("人事管理系统");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/sketch/管理_24.png")));
            stage.show();
            primaryStage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //左侧菜单栏员工管理按钮
    public void userManageBtnAction(ActionEvent actionEvent) {
        setLayoutVisible(0);
        resetDropBtn();
    }

    //左侧菜单栏部门管理按钮
    public void departmentBtnAction(ActionEvent actionEvent){
        setLayoutVisible(1);
        resetDropBtn();
    }

    //左侧菜单栏工资管理按钮
    public void salaryBtnAction(ActionEvent actionEvent) {
        setLayoutVisible(2);
        resetDropBtn();
    }

    //左侧菜单栏考勤管理按钮
    public void attendanceBtnAction(ActionEvent actionEvent) {
        setLayoutVisible(3);
        resetDropBtn();
    }

    //左侧菜单栏创建用户按钮
    public void createUserBtnAction(ActionEvent actionEvent) {
        setLayoutVisible(4);
        resetDropBtn();
    }

    //左侧菜单栏修改密码按钮
    public void modifyPasswordBtnAction(ActionEvent actionEvent) {
        setLayoutVisible(5);
        resetDropBtn();
    }

    //左侧菜单栏删除用户按钮
    public void deleteUserBtnAction(ActionEvent actionEvent) {
        setLayoutVisible(6);
        resetDropBtn();
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
    public void createUserBtn2Action(ActionEvent actionEvent) {
        //用户名,密码均不为空且输入的两个密码的值相等
        if (verifyText(username.getText(), password.getText(), password2.getText())){
            connection = Sql.connect(info[0], info[1]);
            statement = Sql.getStatement(connection);
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
            connection = Sql.connect(info[0], info[1]);
            statement = Sql.getStatement(connection);
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
                connection = Sql.connect(info[0], info[1]);
                statement = Sql.getStatement(connection);
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

    //删除用户按钮
    public void dropBtnAction(ActionEvent actionEvent) {
        try {
            if (dropBtnStatus == 0) {   //当dropBtnStatus为0时,按钮为蓝色;为1时按钮为红色
                connection = Sql.connect(info[0], info[1]);
                statement = Sql.getStatement(connection);
                dropBtnStatus = 1;
                dropBtn.setText("删除");
                dropBtn.setStyle("-fx-background-color: rgb(254, 87, 34); -fx-text-fill: #fff;");
                String sql = "SELECT USER FROM mysql.user;";
                ResultSet resultSet = Sql.query(statement, sql);
                ArrayList<String> users = new ArrayList<>();
                while (resultSet.next()) {
                    String user = resultSet.getString("user");
                    Pattern pattern = Pattern.compile("_sync|root|_root");
                    Matcher matcher = pattern.matcher(user);
                    if (!matcher.matches())
                        users.add(user);
                }
                selectUserCombobox.setItems(FXCollections.observableArrayList(users));
                Sql.disconnect(connection);

            }else if (dropBtnStatus == 1){
                if (selectUserCombobox.getValue() == null){
                    AlertDiaog.alert(0, "错误", "你没有选中任何用户");
                }else{
                    connection = Sql.connect(info[0], info[1]);
                    statement = Sql.getStatement(connection);
                    String sql = "DROP USER '" + selectUserCombobox.getValue() +"';";
                    if (Sql.operate(statement, sql))
                        AlertDiaog.alert(3, "成功", "成功删除用户");
                    else
                        AlertDiaog.alert(0, "失败", "删除用户失败", "用户已不存在或你没有权限");
                    resetDropBtn();
                    Sql.disconnect(connection);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetDropBtn(){
        dropBtn.setText("检索用户");
        dropBtn.setStyle("-fx-background-color: rgb(30, 158, 255); -fx-text-fill: #fff;");
        dropBtnStatus = 0;
    }

    public void setLayoutVisible(int id){
        VBoxList.get(id).setVisible(true);
        for (int i=0 ; i<=6 ; i++){
            if (i == id)
                continue;
            VBoxList.get(i).setVisible(false);
        }
    }

}
