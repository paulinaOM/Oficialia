package pdf8;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import login.MySQL;
import user.UserDAO;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Pdf8Controller implements Initializable, EventHandler {

    @FXML
    TableView<Pdf8Class> tblDocuments;
    @FXML
    TableColumn col1,col2,col3,col4,col5,col6;
    @FXML
    TextField txtReceived;
    @FXML
    DatePicker dpDelivery;
    @FXML
    Button btnSave,btnCancel, btnGenerate,btnBack;
    @FXML
    GridPane gridPane;

    java.util.Date dateToday=new java.util.Date();
    DateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");
    String today= dateFormat.format(dateToday);

    ObservableList<Pdf8Class> reportsList= FXCollections.observableArrayList();
    Pdf8DAO pdf8DAO=new Pdf8DAO(MySQL.getConnection());
    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo

    String Dest="oficialia/reports/reportesNoEntregados.pdf";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clean();

        col1.setCellValueFactory(new PropertyValueFactory<>("noFolio"));
        col2.setCellValueFactory(new PropertyValueFactory<>("noDoc"));
        col3.setCellValueFactory(new PropertyValueFactory<>("docName"));
        col4.setCellValueFactory(new PropertyValueFactory<>("receptionDate"));
        col5.setCellValueFactory(new PropertyValueFactory<>("inst"));
        col6.setCellValueFactory(new PropertyValueFactory<>("addresse"));
        reportsList= pdf8DAO.fetchAllReports(today);
        tblDocuments.setItems(reportsList);

        tblDocuments.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount()==2){
                    gridPane.setDisable(false);
                    btnSave.setDisable(false);
                    btnCancel.setDisable(false);
                }
            }
        });

        btnCancel.setOnAction(this);
        btnSave.setOnAction(this);
        btnBack.setOnAction(this);
        btnGenerate.setOnAction(this);
    }

    @Override
    public void handle(Event event) {
        java.util.Date dateAction = new java.util.Date();
        String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();
        System.out.println("dateAction "+Time.valueOf(actionTime));
        if(event.getSource()==btnBack){
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }
        if (event.getSource()==btnSave){
            saveChanges();
            reportsList=pdf8DAO.fetchAllReports(today);
            tblDocuments.setItems(reportsList);
            clean();
        }
        if (event.getSource()==btnCancel){
            clean();
        }
        if (event.getSource()==btnGenerate){
            File file = new File(Dest);
            file.getParentFile().mkdirs();
            Pdf8DAO pdf8DAO =new Pdf8DAO(MySQL.getConnection());
            try {
                System.out.println(pdf8DAO.fetchAllReports(today).toString());
                new Pdf8Reports().createPdf(Dest,pdf8DAO.fetchAllReports(today));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("");
            al.setContentText("Archivo generado exitosamente");
            al.show();

            int lastAccess=userDAO.getLastAccess();
            userDAO.insertReportCreation(lastAccess,8,Time.valueOf(actionTime));
            userDAO.insertAction("Generó PDF8", Time.valueOf(actionTime),id);
        }
    }

    public void saveChanges(){
        Pdf8Class report=tblDocuments.getSelectionModel().getSelectedItem();
        if (validate()){
            //pdf8DAO.updateStatus(report.getNoFolio(),report.getNoDoc(),txtReceived.getText(), Date.valueOf(dpDelivery.getValue()));
            //*
            Date deliverDate=Date.valueOf(dpDelivery.getValue());
            deliverDate.setDate(deliverDate.getDate()+1);
            pdf8DAO.updateStatus(report.getNoFolio(),report.getNoDoc(),txtReceived.getText(),deliverDate);
            //*
            clean();
        }
        else {
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText(null);
            al.setContentText("Debes llenar todos los campos");
            al.show();
        }

    }

    public boolean validate(){
        if (!txtReceived.getText().equals("")&&dpDelivery.getValue()!=null){
            return true;
        }
        else {
            return false;
        }
    }

    public void clean(){
        btnSave.setDisable(true);
        btnCancel.setDisable(true);
        gridPane.setDisable(true);
        txtReceived.setText("");
        dpDelivery.setValue(null);
    }
}