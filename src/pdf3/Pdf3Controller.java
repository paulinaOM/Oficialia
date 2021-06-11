package pdf3;

import institutions.InstitutionsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import login.MySQL;
import user.UserDAO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;



public class Pdf3Controller implements Initializable{

    @FXML
    ComboBox cmbInstitution;
    @FXML
    Button btnGenerate;

    String DEST1="oficialia/reports/documentosProcedencia.pdf", creationTime;

    InstitutionsDAO institutionsDAO=new InstitutionsDAO(MySQL.getConnection());


    UserDAO userDAO=new UserDAO(MySQL.getConnection()); //----------------------------------Para invocar metodos de insertar actividad e insertar reporte
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> listDept= institutionsDAO.fetch();
        cmbInstitution.getItems().addAll(listDept);
        cmbInstitution.setPromptText("Todos");
        cmbInstitution.getItems().add("Todos");

        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                java.util.Date date = new java.util.Date();

                File file = new File(DEST1);
                file.getParentFile().mkdirs();
                Pdf3DAO pdf3DAO=new Pdf3DAO(MySQL.getConnection());
                System.out.println(cmbInstitution.getValue().toString());
                try {
                    if(cmbInstitution.getSelectionModel().getSelectedItem().equals("Todos")) {

                        new Pdf().createPdf(DEST1, pdf3DAO.getPDF3All(cmbInstitution.getValue().toString()));
                        Alert al=new Alert(Alert.AlertType.INFORMATION);
                        al.setHeaderText("");
                        al.setContentText("Archivo generado exitosamente");
                        al.show();
                        creationTime=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                        System.out.println("creation time "+Time.valueOf(creationTime));
                        userDAO.insertAction("Generó Pdf3", Time.valueOf(creationTime),id); //Inserta la acción en la bitácora de seguimientp
                        int lastAccess=userDAO.getLastAccess();
                        userDAO.insertReportCreation(lastAccess,3,Time.valueOf(creationTime));
                    }else{

                        if(pdf3DAO.getPDF3(cmbInstitution.getValue().toString()).size()!=0) {
                            new Pdf().createPdf(DEST1, pdf3DAO.getPDF3(cmbInstitution.getValue().toString()));
                            System.out.println("Si se puede crear el documento");
                            Alert al=new Alert(Alert.AlertType.INFORMATION);
                            al.setHeaderText("");
                            al.setContentText("Archivo generado exitosamente");
                            al.show();

                            creationTime=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                            System.out.println("creation time "+Time.valueOf(creationTime));
                            userDAO.insertAction("Generó Pdf3", Time.valueOf(creationTime),id); //Inserta la acción en la bitácora de seguimientp
                            int lastAccess=userDAO.getLastAccess();
                            userDAO.insertReportCreation(lastAccess,3,Time.valueOf(creationTime));
                        }else{
                            System.out.println("No se puede crear el documento");
                            Alert al=new Alert(Alert.AlertType.INFORMATION);
                            al.setHeaderText("");
                            al.setContentText("No existen Documentos");
                            al.show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
