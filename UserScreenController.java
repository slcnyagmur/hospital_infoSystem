/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class UserScreenController implements Initializable {

    
    Connector connector = new Connector();
    private Connection conn;
   
    
    private Statement stmt = null;
    @FXML
    private AnchorPane userAnchor;
    @FXML
    private TextField id;
    @FXML
    private TextField code;
    @FXML
    private CheckBox male;
    @FXML
    private CheckBox female;
    @FXML
    private Button save;
    @FXML
    private Button exit;
    @FXML
    private TextField age;
    @FXML
    private CheckBox recovered;
    @FXML
    private Button update;
    @FXML
    private Label userPanel;
    
    private User user;

    /*
    save button action
    hasta kaydını sağlayacak olan fonksiyon
    eğer hasta ilk kez kayıt olacaksa tüm girdilerin girilmesi 
    sağlanarak kayıt işlemi gerçekleştirilir
    */
    @FXML
    private void saveRecord(ActionEvent event) throws SQLException {
        String gender = null;
        String recover = null;
        //text field ların boş bırakılmasına engel olur
        if(id.getText().equals("") || code.getText().equals("") || age.getText().equals("")
                || id.getText().length() < 11){
            //error
        }
        else{
            /*
            if statetinin içerisinde; hem kadın hem erkek seçilmesi veya
            hiçbirinin seçilmemesi durumu kontrol edilir.
            bunun yanında yukarıdaki CHARFIRST ile 
            girilen reçetenin kodunun ilk harfi alınır. charControl() fonksiyonu
            ile doğru aralıkta girilip girilmediğine bakılır.
            */
            String charFirst = String.valueOf(code.getText().charAt(0));
            if((!male.isSelected() && !female.isSelected()) || 
                (male.isSelected() && female.isSelected()) || !charControl(charFirst)){
                //error
                //selected nothing or both of them
            }
            /*
            eğer yukarıda bahsedilen durumlar doğru ise diğer kontrollere geçilir
            ilk kontrol exists() fonksiyonu ile, girilen hastanın 
            aynı kod ve id ile daha once girilip girilmemesi, eğer girilmiş ise 
            tekrar girilemez, return ile fonksiyondan çıkılır.
            */
            else{
                String ID = id.getText();
                String recipeCode = code.getText();
                Patient patient = new Patient(ID, recipeCode);
                if(patient.exists()){
                    return;
                }
                //sadece erkek veya kadın cinsiyet seçimi
                if(male.isSelected() && !female.isSelected()) gender = "Male";
                else if(female.isSelected() && male.isSelected()) gender = "Female";
                else{
                /* not selected error */
                }
                /*
                default olarak recovered kısmında NOT RECOVERED seçilidir
                eğer check box a tıklanırsa YES olarak değişir
                */
                if(recovered.isSelected()) recover = "Yes";
                else recover = "No";
                /*
                recipeType, reçetenin tipini belli eden değişken
                patient sınıfındaki getRecipeType fonksiyonu ile alınır
                */
                String recipeType = patient.getRecipeType(recipeCode);
                /*
                bu fonksiyonda, alınan reçete tipine göre
                birbirlerine uyumlu ve aynı sınıfta yer ilaçlar
                reçeteye atanır.
                */
                medicineRecipeMatch(charFirst);
                //hastanın yaşı alınıyor
                int Age = Integer.parseInt(age.getText());
                /*
                veri tabanına ekleme yapacak sql komutu
                */
                String sql = "INSERT INTO INFORMATIONTABLE VALUES('" + gender + "'" + 
                        ",'" + recipeType + "'" + ",'" + Age + "')";
                Statement newStmt = conn.createStatement();
                newStmt.executeUpdate(sql);
                patient.insert(recover);//patient sınıfındaki ekleme fonksiyonu
                newStmt.close();
                }
        }
        //işlem bittikten sonra sıfırlama, clear fonksiyonu
        age.clear();male.setSelected(false);female.setSelected(false);
        id.clear();code.clear();recovered.setSelected(false);
    }
    /*
    giriş yapan ve işlemleri gerçekleştiren (recent user) 
    exit butonuna bastığında, çıkış verileri userInformation isimli
    veri tabanına eklenir. 
    */
    @FXML
    private void exitUserAct(ActionEvent event) throws IOException, SQLException {
        LocalTime time = LocalTime.now(); //anlık zaman alınıyor
        //zaman değişkenini uygun formata getirmek için formatter kullanılıyor
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        //güncellemeyi yapacak olan sql komutu
        String sql = "UPDATE userinformation SET checkOut='" + time + "' WHERE "
                + "username='" + user.getUsername() + "' AND dateTime='"
                + dtf.format(localDate) + "';";
        stmt.executeUpdate(sql);
        Node node = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        userAnchor.getChildren().setAll(node);
    }
    /*
    update button action
    hasta kaydında güncelleme yapacak olan fonksiyon
    hasta daha once kayıtlı ise, aynı reçete kodu ile güncelleme yapılır
    burada girilmesi gereken veriler sadece id ve recovered check box tir.
    */
    @FXML
    private void updateRecord(ActionEvent event) throws SQLException {
        String recover = null;
        String ID = id.getText();
        if(recovered.isSelected()) recover = "Yes";
        else if(!recovered.isSelected()) recover = "No";
        Patient patient = new Patient(ID);
        //eğer hasta veri tabanına kayıtlı değil ise fonksiyondan çıkılır
        if(!patient.exists()){
            return;
        }
        //patient sınıfındaki update fonksiyonu, vt ye güncelleme yapar
        patient.update(recover);
        //sıfırlama
        id.clear();recovered.setSelected(false);
    }
    /*
    random sayıda istenen reçete kodunun ilaç listesinden
    yine random bir ilaç atamasının yapıldığı fonksiyon
    */
    private void medicineRecipeMatch(String recipeCode) throws SQLException{
        Medicine medicine = new Medicine(recipeCode);
        String [] medicineList = medicine.getList(medicine.getCodeChar());
        String [] selectedList = Medicine.assignList(medicineList);
        /*
        seçilen ilaçlar medicineList isimli vt da güncelleniyor
        ilaçların miktarı 1 azalıyor
        */
        for(int i = 0; i<selectedList.length; i++){
            String sql = "UPDATE medicineList SET quantity=quantity - 1 WHERE "
                + "name='" + selectedList[i] + "';"; 
            //System.out.println(selectedList[i]);
            stmt.executeUpdate(sql);
        }
    }
    /*
    ekleme yapılacak olan hastanın reçete tipini alarak
    doğru aralıkta girilip girilmediğini kontrol eder
    */
    private boolean charControl(String recipeType){
        return "A".equals(recipeType) || "B".equals(recipeType) || "C".equals(recipeType) ||
                "D".equals(recipeType) || "E".equals(recipeType);
    }

    // welcome labeli için 
    //aktif kullanıcının adını gösterir
    public void setUser(User user){
        this.user = new User(user);
        userPanel.setText(user.getUsername());
    }
    public User user(){
        return user;
    }//
        /**
     * Initializes the controller class.
     */
    //sql bağlantısı 
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
            Logger.getLogger(UserScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
