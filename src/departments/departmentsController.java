package departments;

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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class departmentsController implements Initializable, EventHandler {

    @FXML
    Button btnNew,btnSave,btnDelete,btnReturn;
    @FXML
    TextField txtNoDept,txtDeptName;
    @FXML
    TableView<Departments> tblDepartments;
    @FXML
    TableColumn col1,col2;

    DepartmentsDAO departmentsDAO=new DepartmentsDAO(MySQL.getConnection());
    ObservableList<Departments> departmentsList=null;
    boolean insertMode=true;
    boolean updateMode=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtNoDept.requestFocus();

        col1.setCellValueFactory(new PropertyValueFactory<>("areaCode"));
        col2.setCellValueFactory(new PropertyValueFactory<>("areaName"));
        departmentsList=departmentsDAO.fetchAllDepartments();
        tblDepartments.setItems(departmentsList);

        tblDepartments.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount()==2){
                    updateMode=true;
                    insertMode=false;
                    txtNoDept.setDisable(true);
                    txtDeptName.setDisable(false);
                    btnSave.setDisable(false);
                    Departments dept=tblDepartments.getSelectionModel().getSelectedItem();
                    txtNoDept.setText(dept.getAreaCode());
                    txtDeptName.setText(dept.getAreaName());
                }
            }
        });

        btnNew.setOnAction(this);
        btnSave.setOnAction(this);
        btnDelete.setOnAction(this);
        btnReturn.setOnAction(this);
    }
    @Override
    public void handle(Event event) {
        if (event.getSource()==btnNew){
            clear();
            insertMode=true;
            updateMode=false;
            txtNoDept.setDisable(false);
            txtDeptName.setDisable(false);
            btnSave.setDisable(false);
        }
        if(event.getSource()==btnSave){
            if (validateFields()){
                if (insertMode){
                    if (!departmentsDAO.checkAreaCode(txtNoDept.getText())){
                        insertDepartment();
                        reloadTable();
                        clear();
                        btnSave.setDisable(true);
                    }
                    else {
                        Alert al=new Alert(Alert.AlertType.INFORMATION);
                        al.setContentText("La clave del ayuntamiento ya se encuentra registrada");
                        al.show();
                    }
                }
                else if(updateMode){
                    updateDepartment();
                    reloadTable();
                    clear();
                    btnSave.setDisable(true);
                }
            }
            else {
                Alert al=new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("Debes llenar todos los campos");
                al.show();
            }
        }
        if(event.getSource()==btnDelete){
            if (validateFields()){
                if(!departmentsDAO.checkForeignKey(txtNoDept.getText())){
                    Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                    al.setTitle("Confirmar eliminación");
                    al.setContentText("¿Está seguro de eliminar el dato?");
                    Optional<ButtonType> result=al.showAndWait();//Show and wait muestra y espera una respuesta.
                    if (result.get()==ButtonType.OK){
                        delete();
                        reloadTable();
                        clear();
                        btnSave.setDisable(true);
                    }
                }
                else {
                    Alert al=new Alert(Alert.AlertType.INFORMATION);
                    al.setHeaderText("No es posible eliminar el dato");
                    al.setContentText("La clave del ayuntamiento es clave foránea en un destinatario");
                    al.show();
                }

            }
            else{
                Alert al=new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("Seleccione un elemento a eliminar");
                al.show();
            }
        }
        if (event.getSource()==btnReturn){
            Stage stage= (Stage) btnReturn.getScene().getWindow();
            stage.close();
        }
    }

    private void insertDepartment() {
        Departments department= new Departments(txtNoDept.getText(),txtDeptName.getText());
        departmentsDAO.insertDep(department);
    }

    private void updateDepartment() {
        departmentsDAO.updateDep(txtNoDept.getText(),txtDeptName.getText());
    }

    private void delete() {
        Departments dept=tblDepartments.getSelectionModel().getSelectedItem();
        departmentsDAO.deleteDept(dept.getAreaCode());
    }

    private void reloadTable() {
        tblDepartments.getItems().removeAll();
        departmentsList=departmentsDAO.fetchAllDepartments();
        tblDepartments.setItems(departmentsList);
        tblDepartments.refresh();
    }

    public boolean validateFields(){
        boolean ban=false;
        if (!txtNoDept.getText().equals("")&&!txtDeptName.getText().equals("")){
            ban=true;
        }
        return ban;
    }

    public void clear(){
        txtNoDept.setText("");
        txtDeptName.setText("");
        //insertMode=false;
        //updateMode=false;
        txtNoDept.setDisable(true);
        txtDeptName.setDisable(true);
    }
    public void keyTyped(KeyEvent e) {
        int limit=4;
        if (txtNoDept.getText().length()== limit){
            e.consume();
        }
    }
}