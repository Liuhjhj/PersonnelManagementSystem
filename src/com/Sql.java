package com;

import java.sql.*;

    public class Sql {

        //连接数据库
        public static Connection getConnection(String user, String password){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://180.76.145.143/PersonnelManagementSystem" +
                        "?useSSL=false&serverTimezone=UTC", user, password);
                System.out.println("SQL Database Connected!");
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
                System.out.println("SQL Database Disconnected!");
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

        public static boolean createUser(Statement statement, String username, String password){
            try{
                String sql = "CREATE USER '" + username + "'@'%' IDENTIFIED BY '" + password +"';";
                System.out.println(sql);
                statement.execute(sql);
                sql = "GRANT SELECT ON PersonnelManagementSystem.* TO '"+username+"'@'%';";
                statement.execute(sql);
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        public static Statement getStatement(Connection connection){
            try {
                return connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static boolean createAdmin(Statement statement, String username){
            try {
                String sql = "GRANT ALL ON PersonnelManagementSystem.* TO '"+username+"'@'%';";
                System.out.println(sql);
                statement.execute(sql);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public static boolean changePassword(Statement statement, String username, String newPassword){
            try {
                String sql = "SET PASSWORD FOR '"+username+"'@'%' = PASSWORD('"+newPassword+"');";
                System.out.println(sql);
                statement.execute(sql);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }
