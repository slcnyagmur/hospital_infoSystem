/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author ASUS
 */
public class MainScreenController implements Initializable {
    
    Connector connector = new Connector();
    private Connection conn;
    private Statement stmt = null;
    private User User;
    
    //çıkış butonu
    @FXML
    private Button exitMain;
    //giriş butonu
    @FXML
    private Button logIn;
    //Anchor pane sınıfı en rahat kullanıma sahip olduğu için 
    //anchor pane kullanıldı, anchor nesnesi
    @FXML
    private AnchorPane anchor;
    //şifre alanı, şifre yazılırken yazılar noktalanır
    @FXML
    private PasswordField pass;
    //kullanıcı adı alanı
    @FXML
    private TextField user;
    //giriş yapan kullanıcının adını gösteren label
    @FXML
    private Label userPanel = new Label();
    
    @FXML
    void logInAct(ActionEvent event) throws SQLException, IOException {
        /*
        ileriki aşamada admin veya user olma durumuna göre
        geçiş yapılacak olan screen i parametre olarak alacak olan
        node nesnesi
        */
        Node node = null;
        /*
        text in null olup olmama durumunun kontrolü
        */
        if(user.getText() == null || pass.getText() == null){
            //error
        }
        /*
        text in alınıp kullanıcı adı ve şifre yerine yazılması
        giriş yapmaya yetkisi olduğu kesinleşmiş nesnenin
        user olduğu kesin olduğu için User sınıfından user nesnesi oluşturuldu
        parametre olarak username ve password atandı
        */
        String getUsername = user.getText();
        String getPassword = pass.getText();
        User User = new User(getUsername, getPassword);
        String password = null;
        /*
        adminin giriş yapması durumunda
        şifre kontrolü userlist isimli sql den çekilerek yapılır
        */
        if(getUsername.equalsIgnoreCase("admin")){
            String sql = "SELECT password FROM userlist WHERE username='admin'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                password = rs.getString("password");
            }
            /*
            şifre doğru ise adminScreen fxml ine geçiş yapılır
            */
            if(getPassword.equals(password)){
                node = FXMLLoader.load(getClass().getResource("adminScreen.fxml"));
                anchor.getChildren().setAll(node);
            }
            /*
            şifrenin yanlış girildiğinin anlaşılması için konsola bastırdım
            bu silinebilir...
            */
            else{
                System.out.println("false");
            }
        }
        /*
        eğer giriş yapan nesne sql içerisinde var ise
        userScreen fxml ine geçiş yapılır
        */
        else if(User.exists()){
            /*
            buradaki loader ve root nesneleri
            FXML ekranının yönlendirilmesi için kullanılıyor, bunlar sabit
            */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userScreen.fxml"));
            AnchorPane root = loader.load();
            /*
            burada oluşturduğum userScreenController sınıfından bir controller
            nesnesi oluşturdum
            amacım userScreen fxml inde görünecek olan, kullanıcının adını gösteren
            labelin içerisine giriş yapan kullanıcının adının yazılması
            (setUser metodu ile, bu metod o sınıfın içerisinde)
            */
            UserScreenController controller = loader.<UserScreenController>getController();
            controller.setUser(User);
            anchor.getChildren().setAll(root);
        }
        /*
        giriş reddedildiğinde yine konsola false yazısı
        */
        else{
            System.out.println("false");
        }
        /*
        tekrardan ana ekrana dönme durumunda 
        kullanıcı adı ve şifre alanlarının sıfırlanması
        */
        user.clear();
        pass.clear();
    }
    /*
    ana ekrandan çıkış : sistem sonlandırılıyor
    */
    @FXML
    void exitProgram(ActionEvent event) throws SQLException {
        System.exit(0);
    }
    /*
    diğer controller sınıflarındaki ortak bileşenler
    */
    private void fixConnection() throws SQLException{
        conn = connector.createConn();
        stmt = conn.createStatement();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            fixConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
    
