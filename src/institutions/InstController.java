package institutions;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import login.MySQL;
import user.UserDAO;

import java.net.URL;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;

public class InstController implements Initializable, EventHandler{
    @FXML
    Button btnSave, btnNew, btnDelete, btnBack,btnClean;
    @FXML
    TableView<Institutions> tblInstitutions;
    @FXML
    TableColumn col1, col2;
    @FXML
    TextField txtCveInst,txtName;

    InstitutionsDAO institutionsDAO=new InstitutionsDAO(MySQL.getConnection());
    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status

    boolean insertMode=true;
    boolean updateMode=false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(new PropertyValueFactory<>("cveInstitution"));
        col2.setCellValueFactory(new PropertyValueFactory<>("description"));
        ObservableList<Institutions> institutions=institutionsDAO.fetchAll();
        tblInstitutions.setItems(institutions);
        clickInstitution();
        btnNew.setOnAction(this);
        btnDelete.setOnAction(this);
        btnSave.setOnAction(this);
        btnBack.setOnAction(this);
        btnClean.setOnAction(this);
    }

    @Override
    public void handle(Event event) {
        java.util.Date dateAction = new java.util.Date();
        String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();
        if(event.getSource()==btnNew){
            insertMode=true;
            updateMode=false;
            btnDelete.setDisable(true);
            txtCveInst.setEditable(true);
            clean();
        }
        if(event.getSource()==btnSave){
            if(validateFields()){
                if(insertMode){
                    if(!institutionsDAO.checkCveInstitution(txtCveInst.getText())){
                        insertInstitution();
                        userDAO.insertAction("Accesó: Alta Institución", Time.valueOf(actionTime),id);
                    }
                    else {
                        Alert al=new Alert(Alert.AlertType.INFORMATION);
                        al.setContentText("La clave de la institución ya se encuentra registrada");
                        al.show();
                    }

                }
                else if (updateMode)
                {
                    updateInstitution();
                    userDAO.insertAction("Accesó: Modificación Institución", Time.valueOf(actionTime),id);

                }
            }
            else {
                Alert al=new Alert(Alert.AlertType.WARNING);
                al.setContentText("Por favor llena todos los campos");
                al.show();
            }
        }
        if(event.getSource()==btnClean){
            clean();
        }
        if(event.getSource()==btnDelete){
            if((institutionsDAO.validateInstitution(txtCveInst.getText()))==0){
                Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmación");
                confirmation.setContentText("¿Desea eliminar la institución?");
                Optional<ButtonType> result= confirmation.showAndWait();
                if(result.get()==ButtonType.OK){
                    Institutions institutions=tblInstitutions.getSelectionModel().getSelectedItem();
                    institutionsDAO.delete(institutions.getCveInstitution());
                    reloadInstitutions();
                    clean();
                    userDAO.insertAction("Accesó: Baja Institución", Time.valueOf(actionTime),id);
                }
            }
            else {
                Alert al=new Alert(Alert.AlertType.WARNING);
                al.setContentText("No puedes eliminarlo por cuestiones de integridad en los datos");
                al.show();
            }

        }
        if(event.getSource()==btnBack){
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }
    }

    public void clickInstitution(){
        tblInstitutions.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Institutions institutions=tblInstitutions.getSelectionModel().getSelectedItem();
                    txtCveInst.setText(institutions.getCveInstitution());
                    txtName.setText(institutions.getDescription());
                    txtCveInst.setEditable(false);
                    btnDelete.setDisable(false);
                    updateMode=true;
                    insertMode=false;
                }
            }
        });
    }
    public boolean validateFields(){
        boolean ban=false;
        if(!txtName.getText().equals("")&&!txtCveInst.getText().equals("")){
            ban=true;
        }
        return ban;
    }

    public void reloadInstitutions(){
        tblInstitutions.getItems().clear();
        tblInstitutions.setItems(institutionsDAO.fetchAll());
        tblInstitutions.refresh();
    }
    public void clean(){
        txtCveInst.setText("");
        txtName.setText("");
        txtCveInst.setEditable(true);
        btnDelete.setDisable(true);
    }

    public void insertInstitution() {
        Institutions institutions=new Institutions(txtCveInst.getText(),txtName.getText());
        if(institutionsDAO.insert(institutions)){
            reloadInstitutions();
            clean();
        }
    }
    public void updateInstitution() {
        Institutions institutions=new Institutions(txtCveInst.getText(),txtName.getText());
        if(institutionsDAO.update(institutions)){
            reloadInstitutions();
            clean();
        }
    }
    public void keyTyped(KeyEvent e) {
        int limit=4;
        if (txtCveInst.getText().length()== limit){
            e.consume();
        }
    }
}
