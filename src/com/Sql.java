package com;

import java.sql.*;

public class Sql {
    Connection connection = null;
    Statement statement = null;

    //连接数据库
    public boolean conn(String user, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://180.76.145.143/PersonnelManagementSystem" +
                    "?useSSL=false&serverTimezone=UTC", user, password);
            statement = connection.createStatement();
            return true;
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //断开数据库
    public void disconn(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询表
    public ResultSet queryTable(String sql){
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(sql);
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //对数据进行增删改
    public boolean operateData(String sql){
        try {
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
