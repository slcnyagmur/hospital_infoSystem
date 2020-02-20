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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import javafx.scene.control.Alert;

/**
 *
 * @author ASUS
 */
public class Patient {
    
    Connector connector = new Connector();
    private String ID;
    private String recipeCode;

    public Patient() {
    }

    public Patient(String ID){
        this.ID = ID;
    }
    
    public Patient(String ID, String recipeCode) {
        this.ID = ID;
        this.recipeCode = recipeCode;
    }

    public String getID() {
        return ID;
    }

    public String getRecipeCode() {
        return recipeCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        else if(obj.getClass() != Patient.class){
            return false;
        }
        else{
            Patient patient = (Patient) obj;
            return patient.ID.equals(getID()) &&
                    patient.recipeCode.equals(getRecipeCode());
        }
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.now();
    
    // reçetenin başlangıç harfinde renginin çıktısı
    public String getRecipeType(String recipeCode){
        char c = recipeCode.charAt(0);
        String ch = Character.toString(c);
        String type = hashMap().get(ch);
        return type;
    }
    public boolean exists(){
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connector.createConn();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM recipeList WHERE ID='" + getID() + 
                    "';";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String ID = rs.getString("ID");
                if(ID.equals(getID())){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void insert(String result) throws SQLException{
        String msg;
        Connection conn = connector.createConn();
        Statement stmt = null;
        stmt = conn.createStatement();
        String sql = "INSERT INTO recipeList VALUES('" + getID() +"','" + 
            getRecipeCode()+ "','" + dtf.format(localDate) + "','" +
            dtf.format(localDate) + "','" + result + "');";
        stmt.executeUpdate(sql);
        msg = "Data inserted successfully...";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText("INFORMATION");
        alert.setTitle("");
        alert.showAndWait();
        
        conn.close();
        if(stmt != null){
            stmt.close();
        }
    }
    
    public void update(String result) throws SQLException{
        String msg;
        Connection conn = connector.createConn();
        Statement stmt = null;
        stmt = conn.createStatement();
        String sql = "UPDATE recipelist SET lastDate ='" + dtf.format(localDate) + "',"
               + " recovered ='" + result + "' WHERE ID ='" + getID() + "';";
        stmt.executeUpdate(sql);
        msg = "Data updated successfully...";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText("INFORMATION");
        alert.setTitle("");
        alert.showAndWait();
    }
    private static HashMap <String,String> hashMap(){
        HashMap<String,String>hashMap = new HashMap<>();
        hashMap.put("A", "Red");
        hashMap.put("B", "Purple");
        hashMap.put("C", "Orange");
        hashMap.put("D", "Green");
        hashMap.put("E", "White");
        return hashMap;
    }
}
