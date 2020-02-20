/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;

/**
 *
 * @author ASUS
 */
public class Admin {
    
    private final String username = "admin";
    private final String password = "medicine9902";
    Connector connector = new Connector();
    Statement stmt = null;

    public Admin() {
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
    public void insertUser(User user) throws SQLException{
        Connection conn = connector.createConn();
        stmt = conn.createStatement();
        String sql = "INSERT INTO userList VALUES('" + user.getUsername() + "','" 
                + "null" + "');";
        stmt.executeUpdate(sql);
        String msg = "User added successfully...";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText("INFORMATION");
        alert.setTitle("");
        alert.showAndWait();
        conn.close();
        stmt.close();
    }
    public void deleteUser(User user) throws SQLException{
        Connection conn = connector.createConn();
        stmt = conn.createStatement();
        String msg = "User deleted successfully...";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText("INFORMATION");
        alert.setTitle("");
        alert.showAndWait();
        String sql = "DELETE FROM userList WHERE username='" + user.getUsername() + "';";
        stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
    }
    
}
