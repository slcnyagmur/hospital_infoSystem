/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPackage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class AdminScreenController implements Initializable {

    private Admin admin = new Admin();
    Connector connector = new Connector();
    Connection conn;
    Statement stmt;
    Stage stage;
    AnchorPane pane;
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
    private PieChart chart;

    @FXML
    void insertUserAct(ActionEvent event) throws SQLException {
        String username = infoUser.getText();
        User user = new User(username);
        if(user.control(user)){
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
        if(!user.control(user)){
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
    void importGender(ActionEvent event) throws SQLException {
        stage = new Stage();
        double male = 0, female = 0;
        conn = connector.createConn();
        stmt = conn.createStatement();
        String sql = "SELECT * FROM informationtable;";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String result = rs.getString("gender");
            if(result.equals("Male")) male++;
            else female++;
        }
        ObservableList<PieChart.Data> pieGender = FXCollections.observableArrayList(
        new PieChart.Data("Female", female),
                new PieChart.Data("Male", male));
        chart = new PieChart(pieGender);
        Group group = new Group(chart);
        Scene scene = new Scene(group,500,500);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void importAge(ActionEvent event) throws SQLException {
        stage = new Stage();
        double age0 = 0, age7 = 0, age18 = 0, age25 = 0, age45 = 0, age65 = 0;
        conn = connector.createConn();
        stmt = conn.createStatement();
        String sql = "SELECT * FROM informationtable;";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String result = rs.getString("age");
            int intResult = Integer.parseInt(result);
            if(intResult < 7) age0++;
            else if(intResult < 18) age7++;
            else if(intResult < 25) age18++;
            else if(intResult < 45) age25++;
            else if(intResult < 65) age45++;
            else age65++;
        }
        ObservableList<PieChart.Data> pieRecipeType = FXCollections.observableArrayList(
        new PieChart.Data("0-6", age0),
                new PieChart.Data("7-17", age7),
                new PieChart.Data("18-24", age18),
                new PieChart.Data("25-44", age25),
                new PieChart.Data("45-64", age45),
                new PieChart.Data("65++", age65));
        chart = new PieChart(pieRecipeType);
        Group group = new Group(chart);
        Scene scene = new Scene(group,500,500);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void importRecipeType(ActionEvent event) throws SQLException {
        stage = new Stage();
        double red = 0, green = 0, white = 0, purple = 0, orange = 0;
        conn = connector.createConn();
        stmt = conn.createStatement();
        String sql = "SELECT * FROM informationtable;";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            String result = rs.getString("recipeType");
            if(result.equals("Red")) red++;
            else if(result.equals("Green")) green++;
            else if(result.equals("Orange")) orange++;
            else if(result.equals("Purple")) purple++;
            else white++;
        }
        ObservableList<PieChart.Data> pieRecipeType = FXCollections.observableArrayList(
        new PieChart.Data("White", white),
                new PieChart.Data("Green", green),
                new PieChart.Data("Orange", orange),
                new PieChart.Data("Purple", purple),
                new PieChart.Data("Red", red));
        chart = new PieChart(pieRecipeType);
        Group group = new Group(chart);
        Scene scene = new Scene(group,500,500);
        stage.setScene(scene);
        stage.show();
    }
    
    private void createChart(String name){
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
