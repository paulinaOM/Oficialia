package configuration;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.MySQL;
import mainMenu.Controller;
import official.Official;

import java.net.URL;
import java.util.ResourceBundle;



public class CfgController implements Initializable, EventHandler {
    @FXML
    Button btnContinue;
    @FXML
    TextField txtAdress,txtPhone,txtBoss,txtSchedule;
    @FXML
    TableView<Official> tblOficial;
    @FXML
    TableColumn col1,col2;
    @FXML
    ComboBox<String> cmbState,cmbMunicipality;
    @FXML

    ConfigurationDAO configurationDAO=new ConfigurationDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbState.setItems(configurationDAO.fetchState());
        cmbState.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<String> filterData= configurationDAO.fetchMunicipality(newValue);
                cmbMunicipality.setItems(filterData);
                clean();
            }
        });
        col1.setCellValueFactory(new PropertyValueFactory<>("address"));
        col2.setCellValueFactory(new PropertyValueFactory<>("boss"));

        cmbMunicipality.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblOficial.setItems(configurationDAO.fetchOfficial(newValue));
                txtBoss.setText("");
                txtAdress.setText("");
                txtPhone.setText("");
                txtSchedule.setText("");
            }
        });

        clickOfficial();
        txtBoss.setEditable(false);
        txtSchedule.setEditable(false);
        txtPhone.setEditable(false);
        txtAdress.setEditable(false);
        btnContinue.setOnAction(this);


    }
    @Override
    public void handle(Event event) {
        if(event.getSource()==btnContinue){
            if(validate()){
                try{
                    showMainMenu(cmbState.getValue(),cmbMunicipality.getValue(),txtAdress.getText(),txtBoss.getText(),txtSchedule.getText(),Integer.valueOf(txtPhone.getText()));
                    ((Stage)(btnContinue.getScene().getWindow())).hide();
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else {
                Alert al=new Alert(Alert.AlertType.WARNING);
                al.setContentText("Debes seleccionar una Oficialía");
                al.show();
            }
        }


    }
    public boolean validate(){
        if(!txtAdress.getText().equals("")&&!txtPhone.getText().equals("")&&!txtSchedule.getText().equals("")&&!txtBoss.getText().equals("")&&cmbMunicipality.getSelectionModel().getSelectedItem()!=null&&cmbState.getSelectionModel().getSelectedItem()!=null){
            return true;
        }
        else {
            return false;
        }
    }

    public void clickOfficial(){
        tblOficial.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Official official=tblOficial.getSelectionModel().getSelectedItem();
                    txtAdress.setText(official.getAddress());
                    txtBoss.setText(official.getBoss());
                    txtPhone.setText(String.valueOf(official.getPhone()));
                    txtSchedule.setText(official.getSchedule());
                }
            }
        });
    }
    public void clean()
    {
        txtBoss.setText("");
        txtSchedule.setText("");
        txtAdress.setText("");
        txtPhone.setText("");
        cmbMunicipality.setValue(null);
    }
    private void showMainMenu( String state,String municipality,String address,String boss,String schedule,int phone) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainMenu/sample.fxml"));
            Stage stage=new Stage();
            Controller controller=new Controller(state,municipality,address,boss,schedule,phone);
            loader.setController(controller);
            Parent root;
            root=loader.load();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene=new Scene(root,1200,600);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
