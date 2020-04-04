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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    public User(User user){
        this.username = user.username;
        this.password = user.password;
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
    /*
    time ve date değişkenlerinin alınması 
    doğru formatta yazılabilmesi için yardımcı ögeler
    */
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    LocalDate localDate = LocalDate.now();
    
    LocalTime time = LocalTime.now();
    
    //check in saati aynı date var ise değişmez
    /*
    kullanıcı giriş isteğinde bulunduğunda kullanılacak fonksiyon
    sonucunda kullanıcının vt de olup olmama durumuna göre true ya da false döndürür
    eğer kullanıcı var ise; giriş yapılan gün saat bilgileri de vt ye kaydedilir
    default olarak ilk anda giriş ve çıkış verileri, şu ana eşittir
    daha sonra çıkış butonuna basıldığında check_out kısmına güncelleme yapılır
    */
    public boolean exists() throws SQLException{
        //sql e komut bildirecek olan değişken, null olarak atandı
        Statement stmt = null;
        try {
            //sql e bağlantı sağlanıyor
            conn = connector.createConn();
            stmt = conn.createStatement();
            /*
            fonksiyon user nesnesi ile çalıştığından
            getUsername ile içinde bulunulan username alınır
            buna eşit olan sql verileri toplanır
            */
            String sql = "SELECT * FROM userList WHERE username='" + getUsername() + "';";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                //eğer username ve password değişkenleri var ve;
                String username = rs.getString("username");
                String password = rs.getString("password");
                //bu değişkenler doğru ise
                if(username.equals(getUsername()) &&
                        password.equals(getPassword())){
                    String controlSql = "SELECT username FROM userinformation WHERE username ='"
                            + getUsername() + "' AND dateTime='" +
                            dtf.format(localDate) + "';";
                    ResultSet newRs = stmt.executeQuery(controlSql);
                    /*
                    eğer aynı gün içinde kullanıcı daha once giriş yapmış ise
                    check_in değişmez, daha once giriş durumunu anlamak için
                    isBeforeFirst metodu kullanıldı, eğer yapılmadıysa
                    ilk değer olarak hem check_in hem de check_out kısmına 
                    su anı temsil eden saat atıldı
                    */
                    if(!newRs.isBeforeFirst()){
                        String sql1 = "INSERT INTO userInformation VALUES('" + getUsername() + "','"
                            + dtf.format(localDate) + "','" + time + "','" + time + "');";
                        stmt.executeUpdate(sql1);
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //kullanıcı bulunamadığı durum
        return false;
    }
}
