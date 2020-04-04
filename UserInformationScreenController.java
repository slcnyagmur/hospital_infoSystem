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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class UserInformationScreenController implements Initializable {

    Connector connector = new Connector();
    Connection conn;
    Statement stmt;
    
    @FXML
    private AnchorPane userInfoAnchor;
    
    @FXML
    private Button importDateHours;

    @FXML
    private Button importUserHours;

    @FXML
    private TextField getDateInfo;

    @FXML
    private TextField getUserNameInfo;

    @FXML
    private Button back;

    @FXML
    private Button importAllHours;

    private TableView<ObservableList> tableView;
    
    Stage stage;
    
    ObservableList<ObservableList>list;
    
        
    @FXML
    void importUserHoursAct(ActionEvent event) throws SQLException {
        String username = getUserNameInfo.getText();
        stage = new Stage();
        list = FXCollections.observableArrayList();
        //conn = connector.createConn();
        tableView = new TableView<>();
        
        String sql = "SELECT userInformation.username, userInformation.dateTime,"
                + " userInformation.checkIn, userInformation.checkOut,"
                + "userShift.mustCheckIn, userShift.mustCheckOut " +
                    "FROM userInformation " +
                    "INNER JOIN userShift ON userInformation.username=userShift.username " +
                    "WHERE userInformation.username ='" + username + "' AND userShift.dateKey=" + 
                "userInformation.dateTime % 5;";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        
        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
            final int j = i;                
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setPrefWidth(125);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(j).toString());                        
                }                    
            });

        tableView.getColumns().addAll(col); 
        
        while(rs.next()){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int k=1 ; k<=rs.getMetaData().getColumnCount(); k++){
                    row.add(rs.getString(k));
                }
                list.add(row);
            }
            tableView.setItems(list);
        }
        
        Scene scene = new Scene(tableView,750,500);
        stage.setScene(scene);
        getUserNameInfo.clear();
        stage.show();
    }
    
    @FXML
    void importAllHoursAct(ActionEvent event) throws SQLException {
        stage = new Stage();
        list = FXCollections.observableArrayList();
        //conn = connector.createConn();
        //stmt = conn.createStatement();
        tableView = new TableView<>();
        
        
        String sql = "SELECT userInformation.username, userInformation.dateTime,"
                + " userInformation.checkIn, userInformation.checkOut,"
                + "userShift.mustCheckIn, userShift.mustCheckOut " +
                    "FROM userInformation " +
                    "INNER JOIN userShift ON userInformation.username=userShift.username AND userShift.dateKey=" + 
                "userInformation.dateTime % 5;";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        
        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
            final int j = i;                
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setPrefWidth(125);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(j).toString());                        
                }                    
            });

        tableView.getColumns().addAll(col); 
        
        
        tableView.setRowFactory( tv -> {
            TableRow row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                String msg;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("INFORMATION");
                
                if (e.getClickCount() == 2 && (! row.isEmpty()) ) {
                    try{
                        Object obj = tableView.getSelectionModel().getSelectedItems().get(0);
                        String firstColumn = obj.toString().split(",")[0].substring(1);
                        String secondColumn = obj.toString().split(",")[1].substring(1);
                        
                        String userName = firstColumn;
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(secondColumn);
                        
                        if(!existsUpdatedRecord(userName, date)){
                            String sqlUpdatedUser = "INSERT INTO updatedUserRecords VALUES('" + 
                            firstColumn + "','" + secondColumn + "');";
                            stmt.executeUpdate(sqlUpdatedUser);
                            String sqlAddScore = "UPDATE userList SET score=score + 1 "
                            + "WHERE username='" + firstColumn + "';";
                            stmt.executeUpdate(sqlAddScore);
                            msg = "Score updated successfully...";
                            row.setStyle("-fx-background-color : lightblue");
                            alert.setContentText(msg);
                            alert.showAndWait();
                            }
                        else{
                            row.setStyle("-fx-background-color : #ff4d4d");
                            msg = "Score had already been updated...";
                            alert.setContentText(msg);
                            alert.showAndWait();
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            return row ;
        });
        
        while(rs.next()){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int k=1 ; k<=rs.getMetaData().getColumnCount(); k++){
                    row.add(rs.getString(k));
                }
                list.add(row);
                }
            
            tableView.setItems(list);
            
        }
        
        Scene scene = new Scene(tableView,750,500);
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML
    void importDateHoursAct(ActionEvent event) throws SQLException {
        if(getDateInfo.getText().equals("")){
            
        }
        
        else{
            String date = getDateInfo.getText();
            stage = new Stage();
            list = FXCollections.observableArrayList();
            //conn = connector.createConn();
            tableView = new TableView<>();
            if(!checkDateFormat(date)){
                
            }
            //uyuşan verileri getirmek için 
            else{
                String sql = "SELECT userInformation.username, userInformation.dateTime,"
                    + " userInformation.checkIn, userInformation.checkOut,"
                    + "userShift.mustCheckIn, userShift.mustCheckOut " +
                        "FROM userInformation " +
                        "INNER JOIN userShift ON userInformation.username=userShift.username " +
                        "WHERE userInformation.dateTime='" + date + "' AND userShift.dateKey=" + 
                    "userInformation.dateTime % 5;;";

                ResultSet rs = conn.createStatement().executeQuery(sql);

                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    final int j = i;                
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setPrefWidth(125);
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                        }                    
                    });

                tableView.getColumns().addAll(col); 

                while(rs.next()){
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int k=1 ; k<=rs.getMetaData().getColumnCount(); k++){
                            row.add(rs.getString(k));
                        }
                        list.add(row);
                    }
                    tableView.setItems(list);
                }

                Scene scene = new Scene(tableView,750,500);
                stage.setScene(scene);
                stage.show();
                }

        }
        getDateInfo.clear();
        
    }
    
    public boolean existsUpdatedRecord(String userName, Date dateTime) throws SQLException{
        String updatedSQL = "SELECT * FROM updatedUserRecords";
        ResultSet updatedRs = conn.createStatement().executeQuery(updatedSQL);
        while(updatedRs.next()){
            if(updatedRs.getString("username").equals(userName) &&
                    updatedRs.getDate("dateTime").equals(dateTime)){
                    return true;
                }
        }
        return false;
    }

    @FXML
    void turnBack(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("adminScreen.fxml"));
        userInfoAnchor.getChildren().setAll(node);
    }
    /**
     * Initializes the controller class.
     */
    
    private boolean checkDateFormat(String date){
        if(date.length() < 10){
            return false;
        }
        else if(date.charAt(4) != '-' && date.charAt(7) != '-'){
            return false;
        }
        return true;
    }
    
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
            Logger.getLogger(UserInformationScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
