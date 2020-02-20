/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;

/**
 *
 * @author ASUS
 */
public class User {
    
    Connector connector = new Connector();
    Connection conn = null;
    private String username;
    private String password;

    public User() {
    }

    public User(String username){
        this.username = username;
    }
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        else if(obj.getClass() != User.class){
            return false;
        }
        else{
            User user = (User) obj;
            return user.getPassword().equals(this.password) &&
                    user.getUsername().equals(this.username);
        }
    }
    public boolean exists() throws SQLException{
        Statement stmt = null;
        try {
            conn = connector.createConn();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM userList WHERE username='" + getUsername() + "';";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String username = rs.getString("username");
                if(username.equals(getUsername())){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
}
