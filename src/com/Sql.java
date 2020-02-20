package com;

import java.sql.*;

    public class Sql {

        //连接数据库
        public static Connection connect(String user, String password){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://180.76.145.143/PersonnelManagementSystem" +
                        "?useSSL=false&serverTimezone=UTC", user, password);
                return connection;
            }catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        //断开数据库
        public static void disconnect(Connection connection){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //查询表
        public static ResultSet queryTable(Statement statement, String sql){
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
        public static boolean operateData(Statement statement, String sql){
            try {
                statement.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
