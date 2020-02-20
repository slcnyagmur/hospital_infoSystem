/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class AdminScreenController implements Initializable {

    private Admin admin = new Admin();
    /**
     * Initializes the controller class.
     */
    
    @FXML
    private AnchorPane adminAnchor = new AnchorPane();
    
    @FXML
    private AnchorPane insertDeleteAnchor = new AnchorPane();
    
    @FXML
    private Button add;

    @FXML
    private TextField infoUser;

    @FXML
    private Button back;

    @FXML
    private Button delete;

    @FXML
    private Button exit;

    @FXML
    private Button gender;

    @FXML
    private Button recipeType;

    @FXML
    private Button insertDelete;

    @FXML
    private Button age;

    @FXML
    void insertUserAct(ActionEvent event) throws SQLException {
        String username = infoUser.getText();
        User user = new User(username);
        if(user.exists()){
            //var ise ekleyemez
            return;
        }
        else{
            admin.insertUser(user);
        }
        infoUser.clear();
    }

    @FXML
    void deleteUserAct(ActionEvent event) throws SQLException {
        String username = infoUser.getText();
        User user = new User(username);
        if(!user.exists()){
            //olmayan kaydı silemez
            return;
        }
        else{
            admin.deleteUser(user);
        }
        infoUser.clear();
    }

    @FXML
    void adminUserAct(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("insertDeleteUserScreen.fxml"));
        adminAnchor.getChildren().setAll(node);
    }
    
    @FXML
    void turnBack(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("adminScreen.fxml"));
        insertDeleteAnchor.getChildren().setAll(node);
    }
    
    @FXML
    void exitAdminAct(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        adminAnchor.getChildren().setAll(node);
    }
    
    @FXML
    void importGender(ActionEvent event) {

    }

    @FXML
    void importAge(ActionEvent event) {

    }

    @FXML
    void importRecipeType(ActionEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
