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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private Admin admin = new Admin();
    
    @FXML
    private TextField code;

    @FXML
    private Button save;

    @FXML
    private Button update;
    
    @FXML
    private TextField id;
    
    @FXML
    private Button exit;
    
    @FXML
    private Button exitMain;
    
    @FXML
    private AnchorPane userAnchor;
 
    @FXML
    private CheckBox female;

    @FXML
    private CheckBox male;
    
    @FXML
    private AnchorPane anchor;
    
    @FXML
    private PasswordField pass;

    @FXML
    private TextField age;
    
    @FXML
    private Button logIn;

    @FXML
    private TextField user;
    
    @FXML
    private CheckBox recovered;
    

    @FXML
    void logInAct(ActionEvent event) throws SQLException, IOException {
        Node node = null;
        if(user.getText() == null || pass.getText() == null){
            //error
        }
        String username = user.getText();
        String password = pass.getText();
        User User = new User(username, password);
        if(username.equals(admin.getUsername()) && password.equals(admin.getPassword())){
            node = FXMLLoader.load(getClass().getResource("adminScreen.fxml"));
            anchor.getChildren().setAll(node);
        }
        else if(User.exists()){
            node = FXMLLoader.load(getClass().getResource("userScreen.fxml"));
            anchor.getChildren().setAll(node);
        }
        else{
            System.out.println("false");
        }
        user.clear();
        pass.clear();
    }
    @FXML
    void exitProgram(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    void saveRecord(ActionEvent event) throws SQLException {
        conn = connector.createConn();
        stmt = conn.createStatement();
        if(id.getText() == null || code.getText() == null){
            //error
        }
        String gender = null;
        String recover = null;
        String ID = id.getText();
        String recipeCode = code.getText();
        Patient patient = new Patient(ID, recipeCode);
        if(patient.exists()){
            
            return;
        }
        if(male.isSelected()) gender = "Male";
        else if(female.isSelected()) gender = "Female";
        else{ /* not selected error */}
        if(recovered.isSelected()) recover = "Yes";
        else if(!recovered.isSelected()) recover = "No";
        String recipeType = patient.getRecipeType(recipeCode);
        int Age = Integer.parseInt(age.getText());
        String sql = "INSERT INTO INFORMATIONTABLE VALUES('" + gender + "'" + 
                ",'" + recipeType + "'" + ",'" + Age + "')";
        Statement newStmt = conn.createStatement();
        newStmt.executeUpdate(sql);
        patient.insert(recover);
        
        //işlem bittikten sonra sıfırlama
        age.clear();male.setSelected(false);female.setSelected(false);
        id.clear();code.clear();recovered.setSelected(false);
        newStmt.close();
    }
    
    @FXML
    void updateRecord(ActionEvent event) throws SQLException {
        String recover = null;
        String ID = id.getText();
        if(recovered.isSelected()) recover = "Yes";
        else if(!recovered.isSelected()) recover = "No";
        Patient patient = new Patient(ID);
        if(!patient.exists()){
            
            return;
        }
        patient.update(recover);
        id.clear();recovered.setSelected(false);
    }
    @FXML
    void exitUserAct(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        userAnchor.getChildren().setAll(node);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
    }    
    
}
