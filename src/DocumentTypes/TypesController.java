package DocumentTypes;

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

public class TypesController implements Initializable,EventHandler{
    @FXML
    Button btnSave, btnNew, btnDelete, btnBack,btnClean;
    @FXML
    TableView<DocumentType> tblTypes;
    @FXML
    TableColumn col1, col2;
    @FXML
    TextField txtCveType,txtName;

    DocumentTypeDAO documentTypeDAO=new DocumentTypeDAO(MySQL.getConnection());
    boolean insertMode=true;
    boolean updateMode=false;

    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(new PropertyValueFactory<>("cveType"));
        col2.setCellValueFactory(new PropertyValueFactory<>("description"));
        ObservableList documentTypes=documentTypeDAO.fetchAll();
        tblTypes.setItems(documentTypes);
        clickType();

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
            txtCveType.setEditable(true);
            clean();
        }
        if(event.getSource()==btnSave){
            if(validateFields()){
                if(insertMode){
                    if(!documentTypeDAO.checkCveType(txtCveType.getText())) {
                        insertType();
                        userDAO.insertAction("Accesó: Alta Tipo documento", Time.valueOf(actionTime),id);
                    }
                    else {
                        Alert al=new Alert(Alert.AlertType.INFORMATION);
                        al.setContentText("La clave del tipo de documento ya se encuentra registrada");
                        al.show();
                    }

                }
                else if (updateMode)
                {
                    updateType();
                    userDAO.insertAction("Accesó: Modificación Tipo documento", Time.valueOf(actionTime),id);
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
            if((documentTypeDAO.validateType(txtCveType.getText()))==0){
                Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmación");
                confirmation.setContentText("¿Desea eliminar el tipo de documento?");
                Optional<ButtonType> result= confirmation.showAndWait();
                if(result.get()==ButtonType.OK){
                    DocumentType documentType=tblTypes.getSelectionModel().getSelectedItem();
                    documentTypeDAO.delete(documentType.getCveType());
                    reloadTypes();
                    clean();
                    userDAO.insertAction("Accesó: Baja Tipo documento", Time.valueOf(actionTime),id);
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

    public void clickType(){
        tblTypes.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    DocumentType documentType=tblTypes.getSelectionModel().getSelectedItem();
                    txtCveType.setText(documentType.getCveType());
                    txtName.setText(documentType.getDescription());
                    txtCveType.setEditable(false);
                    btnDelete.setDisable(false);
                    updateMode=true;
                    insertMode=false;
                }
            }
        });
    }

    public void reloadTypes(){
        tblTypes.getItems().clear();
        tblTypes.setItems(documentTypeDAO.fetchAll());
        tblTypes.refresh();
    }
    public void clean(){
        txtCveType.setText("");
        txtName.setText("");
        txtCveType.setEditable(true);
        btnDelete.setDisable(true);
    }

    public void insertType() {
        if(validateFields()){
            DocumentType documentType=new DocumentType(txtCveType.getText(),txtName.getText());
            if(documentTypeDAO.insert(documentType)){
                reloadTypes();
                clean();
            }
        }
    }
    public void updateType() {
        DocumentType documentType=new DocumentType(txtCveType.getText(),txtName.getText());
        if(documentTypeDAO.update(documentType)){
            reloadTypes();
            clean();
        }
    }
    public boolean validateFields(){
        boolean ban=false;
        if(!txtName.getText().equals("")&&!txtCveType.getText().equals("")){
            ban=true;
        }
        return ban;
    }
    public void keyTyped(KeyEvent e) {
        int limit=4;
        if (txtCveType.getText().length()== limit){
            e.consume();
        }
    }

}
