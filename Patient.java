/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.sql.Connection;
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
    //zaman ve tarih formatları
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.now();
    
    /*
    hastanın reçete kodunun parametre olarak alan 
    ve ilk karakterini en aşağıda oluşturulmuş
    map ten, key-value mantığı ile
    harfe göre atanmış olan reçete tipini, renk olarak döndürür
    */
    public String getRecipeType(String recipeCode){
        char c = recipeCode.charAt(0);
        //stringten character tipine dönüş sağlanıyor
        String ch = Character.toString(c);
        String type = hashMap().get(ch);
        return type;
    }
    /*
    hastanın daha once kaydının olup olmadığının kontrolünü yapan fonksiyon
    user sınıfı tarafından kontrol amacı ile kullanılır
    */
    public boolean exists(){
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = connector.createConn();
            stmt = conn.createStatement();
            //hastaları vt den çeken sql komutu
            String sql = "SELECT * FROM recipeList WHERE ID='" + getID() + 
                    "';";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                /*sonuc kümesiyle içinde bulunulan hastanın 
                id si eşleşme sağlanırsa true döndürür
                */
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
    /*
    hastanın ilk kez kaydının yapılması durumunda 
    gerekli eklemeleri yapacak fonksiyon
    user sınıfı tarafından kullanılır
    */
    public void insert(String result) throws SQLException{
        //bilgi mesajı verecek olan string değişkeni
        String msg;
        //sqle bağlantı
        Connection conn = connector.createConn();
        Statement stmt = null;
        stmt = conn.createStatement();
        /*
        patient sınıfından bir nesne ile çağırılacağı için 
        getID ve getRecipeCode ile hasta verileri alınır
        default olarak check_in ve check_out değişkenleri 
        anlık zaman olarak atanır
        result parametresi hastanın iyileşmesi durumunu temsil eder
        user sınıfının girdiği veriye göre şekil alır
        onun için user sınıfı tarafından kullanılırken, result oradan alınır.
        */
        String sql = "INSERT INTO recipeList VALUES('" + getID() +"','" + 
            getRecipeCode()+ "','" + dtf.format(localDate) + "','" +
            dtf.format(localDate) + "','" + result + "');";
        stmt.executeUpdate(sql);
        //hastanın eklenmesi durumunda gösterilecek 
        //information tipindeki alert ekranı
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
    /*
    veri tabanında var olan hastanın kaydını 
    güncellemek için kullanılacak fonksiyon
    result parametresi recovered check box inin durumunu temsil eder
    */
    public void update(String result) throws SQLException{
        //bilgi mesajı verecek olan string değişkeni
        String msg;
        //sql e bağlantı
        Connection conn = connector.createConn();
        Statement stmt = null;
        stmt = conn.createStatement();
        /*
        hastanın son gelis tarihini güncelleyecek sql komutu
        son geliş tarihi günlük alınan tarih ile değiştirilir
        patient sınıfından bir nesne ile çağırılacağı için 
        getID ile içinde bulunulan hastanın id si alınır.
        */
        String sql = "UPDATE recipelist SET lastDate ='" + dtf.format(localDate) + "',"
               + " recovered ='" + result + "' WHERE ID ='" + getID() + "';";
        stmt.executeUpdate(sql);
        //hastanın güncellenmesi durumunda gösterilecek 
        //information tipindeki alert ekranı
        msg = "Data updated successfully...";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText("INFORMATION");
        alert.setTitle("");
        alert.showAndWait();
    }
    //reçete tipine atanmış olan harfe göre renk eşleşmesi
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
