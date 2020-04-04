package mainPackage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ASUS
 */
public class Connector {
    /*
    java içerisindeki Connection ve Statement built-in fonksiyonlarından
    oluşturulan conn ve stmt nesneleri
    */
    private Connection conn;
    private Statement stmt;
    /*
    bu sınıftaki asıl önemli olan ilk iki nesne ve üçüncü olarak 
    kullanılan createConn metodu
    */
    public static final Connection createConn() throws SQLException{
        /*
        bağlantıyı alma ve conn isimli nesneye bağlantı sağlama için 
        kullanılır. sql url sini, kullanıcı adını ve şifresini içerir
        */
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey",
                "root","Yadeda9902.");
        return conn;
    }

    public Connector() {
    }

    public Connector(Connection conn, Statement stmt) {
        this.conn = conn;
        this.stmt = stmt;
    }

    
    public Connection getConn() {
        return conn;
    }

    public Statement getStmt() {
        return stmt;
    }
    
}
