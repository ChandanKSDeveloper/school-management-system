package com.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties props = new Properties();

    static {
        loadProperties();
        System.out.println("properties loaded");
    }
    public DBConnection() throws ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/school_management_system";
        String username = "root";
        String password = "chandan@root";
        Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("connection built : " + url);
            System.out.println(connection);
        } catch (SQLException e) {
            System.out.println("connection failed : " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        String dbname = props.getProperty("DBNAME");
        String url = "jdbc:mysql://localhost:3306/" + dbname;
        String username = props.getProperty("USERNAME");
        String password = props.getProperty("PASSWORD");
        Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection(url,username,password);

        if(connection == null){
            throw new RuntimeException("connection is null ");
        }

        return connection;


    }
    private static void loadProperties(){
        try(InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("dbConfig.properties")){
            if(inputStream == null){
                throw new RuntimeException("dbconfig.properties not found in classPath or not get loaded");
            }

            props.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProps(String prop){
        String val =  props.getProperty(prop);
        if(val == null){
            System.out.println("Property '" + prop + "' not found. Available properties: " + props.keySet());
            throw new RuntimeException("Property '" + prop + "' not found. Available properties: " + props.keySet());
        }

        return  val.trim();

    }
}
