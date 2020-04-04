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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class AdminScreenController implements Initializable {

    //sql bağlantısını sağlayacak olan atama
    Connector connector = new Connector();
    Connection conn;
    Statement stmt;
    //yeni bir ekran oluşumu
    Stage stage;
    
    AnchorPane pane;
    //liste görünümü 
    ObservableList<ObservableList>list;
    //admin sayfasından oluşturulmuş nesne
    //admin sayfasına geçişi sağlar
    @FXML 
    private AnchorPane adminAnchor = new AnchorPane();
    
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
    private Button userHours;
    
    //tablo görünümü
    private TableView<ObservableList> tableView;
    
    /*
    admin paneli açıldığında ilk girişte
    sayısı azalmış ilaçları gösterecek olan label
    */
    @FXML
    private Label medicineInfo;
    
    @FXML
    private Button order;
    
    @FXML
    private Button userScores;
    
    //admin panelinden çıkış için (button action)
    @FXML
    void exitAdminAct(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        adminAnchor.getChildren().setAll(node);
    }
    /*
    admin panelindeki, hasta erkek/kadın oranlarını
    gösterecek olan pie chart (button action)
    */
    @FXML
    void importGender(ActionEvent event) throws SQLException {
        //yeni ekran için nesne oluşturuldu
        stage = new Stage();
        double male = 0, female = 0;       
        /*
        tüm hasta verilerinin saklandığı informationtable sqlinden
        tüm verileri çekmek için kullanılacak sql komutu 
        */
        String sql = "SELECT * FROM informationtable;";
        /*
        elde edilen sql verileri resultset isimli sınıftan 
        oluşturulan rs nesnesinde saklanır. Burada stmt, Statement sınıfından
        oluşturulmuş bir nesne ve executeQuery komutu ile
        parametre olarak gönderilen sql komutunun sonucunu tutuyor
        */
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            /*
            rs nin içerisinde bulunan tüm sütun özellikleri olduğundan
            sadece "gender" sütunundaki verileri almak için
            rs.getString metodu kullanılıyor
            */
            String result = rs.getString("gender");
            //male ya da female olma durumuna göre arttırma
            if(result.equals("Male")) male++;
            else female++;
        }
        /*
        burada pie chart oluşturuluyor, 
        bu oluşturma sabit, 
        getPieValue sql deki sütunların isimlendirmesinin 
        daha sonra kullanılacak olan tablo nesnesine aktarılması için var
        */
        ObservableList<PieChart.Data> pieGender = FXCollections.observableArrayList(
        new PieChart.Data("Female", female),
                new PieChart.Data("Male", male));
        chart = new PieChart(pieGender){
        @Override
            protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
              if (getLabelsVisible()) {
                getData().forEach(d -> {
                  Optional<Node> opTextNode = chart.lookupAll(".chart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
                  if (opTextNode.isPresent()) {
                    ((Text) opTextNode.get()).setText(d.getName() + " " + d.getPieValue() + " Persons");
                  }
                });
              }
              super.layoutChartChildren(top, left, contentWidth, contentHeight);
            }
          };
        /*
        stage görünümünün düzenli olmasını sağlayan Group sınıfından
        group nesnesi kullanıldı ve chart eklendi
        500e 500 bir ekran görüntüsü var
        stage.show ile ekran kullanıcı tarafından gösteriliyor
        */
        Group group = new Group(chart);
        Scene scene = new Scene(group,500,500);
        stage.setScene(scene);
        stage.show();
    }

    /*
    import age ve import recipeType import gender ile
    aynı işlevde, benzer şekilde oluşturuldu,
    sadece pie chart içerisindeki eleman isimleri farklılık gösteriyor
    */
    @FXML
    void importAge(ActionEvent event) throws SQLException {
        stage = new Stage();
        double age0 = 0, age7 = 0, age18 = 0, age25 = 0, age45 = 0, age65 = 0;
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
        chart = new PieChart(pieRecipeType){
            @Override
            protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
              if (getLabelsVisible()) {
                getData().forEach(d -> {
                  Optional<Node> opTextNode = chart.lookupAll(".chart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
                  if (opTextNode.isPresent()) {
                    ((Text) opTextNode.get()).setText(d.getName() + " " + d.getPieValue() + " Persons");
                  }
                });
              }
              super.layoutChartChildren(top, left, contentWidth, contentHeight);
            }
          };
        Group group = new Group(chart);
        Scene scene = new Scene(group,500,500);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void importRecipeType(ActionEvent event) throws SQLException {
        stage = new Stage();
        double red = 0, green = 0, white = 0, purple = 0, orange = 0;
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
        chart = new PieChart(pieRecipeType){
        @Override
            protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
              if (getLabelsVisible()) {
                getData().forEach(d -> {
                  Optional<Node> opTextNode = chart.lookupAll(".chart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
                  if (opTextNode.isPresent()) {
                    ((Text) opTextNode.get()).setText(d.getName() + " " + d.getPieValue() + " Persons");
                  }
                });
              }
              super.layoutChartChildren(top, left, contentWidth, contentHeight);
            }
          };
        Group group = new Group(chart);
        Scene scene = new Scene(group,500,500);
        stage.setScene(scene);
        stage.show();
    }
    /*
    adminin eksik olan ilaçların siparişini verebilmesi
    için kullanılacak olan metod
    default olarak 30 ilaç siparişi ayarlandı, yani order butonuna 
    basıldığında, 3 olan bir ilacın miktarı 30 a çıkacak
    */
     @FXML
    void orderMedicine(ActionEvent event) throws SQLException {
        /*
        sipariş verilmesi gereken bir ilaç yok ise
        all is well yazısı 
        */
        if(medicineInfo.getText().equals("")){
            medicineInfo.setText("---ALL IS WELL---");
        }
        /*
        sipariş verilecek ilaçlar medicineInfo.getText ile labelden alınıp
        bir listenin içerisine, \n ile yani satır satır ayrılıp atılır
        */
        else{
            String label = medicineInfo.getText();
            String [] list = label.split("\n");
            /*
            update ve set komutlarıyla aranan ilacın bulunup
            miktarının 30 a çıkarılması için kullanılan sql komutu
            where burada ilac isminin eşleştirilmesi için var
            */
            for(int i = 0; i<list.length; i++){
                String sql = "UPDATE medicineList SET quantity='" + 30 + "' WHERE"
                + " name='" + list[i] + "';";
                stmt.executeUpdate(sql);
            }
            /*
            başarılı bir şekilde eklenme yapıldıysa
            admine bir alert çıkartılır, daha sonra ise
            medicineInfo labeline all is well yazılır
            */
            String msg = "Order completed...";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
            alert.setTitle("");
            alert.setHeaderText("INFORMATION");
            alert.showAndWait();
            medicineInfo.setText("---ALL IS WELL---");
        }
    }
    /*
    adminin, kullanıcılarının giriş çıkış saatlerine erişmesi için
    import hours butonuna basmasıyla userInformationScreen isimli fxml sayfasına
    gidişini sağlar
    */
    @FXML
    void importHours(ActionEvent event) throws SQLException, IOException {
        Node node = FXMLLoader.load(getClass().getResource("userInformationScreen.fxml"));
        adminAnchor.getChildren().setAll(node);
        
    }
    
    @FXML
    void importScores(ActionEvent event) throws SQLException {
        
        stage = new Stage();
        list = FXCollections.observableArrayList();
        conn = connector.createConn();
        tableView = new TableView<>();
        
        String sql = "SELECT username, score FROM userlist WHERE username != 'admin';";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        
        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
            final int j = i;                
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setPrefWidth(375);
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
        
        Scene scene = new Scene(tableView,750,175);
        stage.setScene(scene);
        stage.show();
    }
    
    private void fixConnection() throws SQLException{
        /*
        ilk anda oluşturulan conn ve stmt nesneleri.
        sql ile bağlantıyı sağlayıp
        createStatement metodu ile sql komutu yürütebilecek
        bir stmt nesnesi oluşturuldu
        */
        conn = connector.createConn();
        stmt = conn.createStatement();
    }
    /*
    admin paneline girildiği anda
    (ilk girişte, herhangi bir şey yapmadan görünecek olan
    eksik ilaç listenin oluşturulduğu metod
    bu metod en aşağıda bulunan INITIALIZE metodunun içinde, 
    bu metod ekran ilk yüklendiğinde yapılacak olanları içerir
    */
    private void fixMedicineLabel() throws SQLException{
        //ilaçların atılacağı arraylist
        ArrayList absentMedicineList = new ArrayList();
        /*
        medicineList isimli sql den quantity yani miktarının 5 ten küçük olduğu
        ilaç isimlerini select komutu ile seçme
        */
        String sql = "SELECT name FROM medicineList WHERE quantity<'" + 
                5 + "';";
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()){
            /*
            tablodan sadece "name" isimli sütunun verilerini çekmek için 
            kullanılacak sql komutu resultSet ile alınıyor
            */
            absentMedicineList.add(rs.getString("name"));
        }
        /*
        elde edilen ilaçların daha düzenli görünümü için
        StringBuilder sınıfından builder nesnesi ile
        boş bir string oluşturulup, daha sonra absentMedicineList ismiyle
        olusturulan arraylistten veriler get metodu ile alınıp
        bu builder in içine atıldı
        */
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<absentMedicineList.size(); i++){
            builder.append(absentMedicineList.get(i)+ "\n");
        }
        /*
        string builder dan tekrar string sınıfına geçildi,
        (çünkü setText metodu sadece String sınıfıyla çalışıyor
        */
        String absentMedicine = builder.toString();
        medicineInfo.setText(absentMedicine);
        medicineInfo.setVisible(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //yukarıda belirtilen ilkleme işlemleri
            //sql bağlantısı sağlandı ve ilaç bilgisi verildi
            fixConnection();
            fixMedicineLabel();
        } catch (SQLException ex) {
        }
    }    
}
