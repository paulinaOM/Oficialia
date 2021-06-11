package user;

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
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable, EventHandler {
    @FXML
    TableView<User> tblUser;
    @FXML
    Button btnSave,btnDelete,btnBack, btnNew, btnClean,btnPassword;
    @FXML
    TextField txtId,txtUserName;
    @FXML
    PasswordField txtPassword;
    @FXML
    ComboBox<String> cmbRol;
    @FXML
    TableColumn col1,col2,col3;
    @FXML
    Label lblId;

    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status
    boolean insertMode=true;
    boolean updateMode=false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setOnAction(this);
        btnBack.setOnAction(this);
        btnDelete.setOnAction(this);
        btnNew.setOnAction(this);
        btnClean.setOnAction(this);
        btnPassword.setOnAction(this);

        btnPassword.setDisable(true);
        txtId.setEditable(false);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("username"));
        col3.setCellValueFactory(new PropertyValueFactory<>("rol"));
        tblUser.setItems(userDAO.fetchAllUser());
        cmbRol.setItems(userDAO.fetchAllRol());
        btnDelete.setDisable(true);
        cmbRol.setPromptText("Rol");
        if(updateMode){
            txtId.setVisible(true);
            lblId.setVisible(true);
        }
        if(insertMode){
            txtId.setVisible(false);
            lblId.setVisible(false);
        }
        clickUser();
    }

    @Override
    public void handle(Event event) {
        java.util.Date dateAction = new java.util.Date();
        String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();

        if(event.getSource()==btnNew){
            insertMode=true;
            updateMode=false;
            btnDelete.setDisable(true);
            txtId.setVisible(false);
            lblId.setVisible(false);
            btnPassword.setDisable(true);
            clean();
        }
        if(event.getSource()==btnSave){
                if(insertMode){
                    if(!userDAO.checkUserName(txtUserName.getText())){
                        insertUser();
                        userDAO.insertAction("Accesó: Alta Usuario", Time.valueOf(actionTime),id);
                    }
                    else {
                        Alert al=new Alert(Alert.AlertType.INFORMATION);
                        al.setContentText("El nombre de usuario ya se encuentra registrado");
                        al.show();
                    }
                }
                else if (updateMode)
                {
                   updateUser();
                    userDAO.insertAction("Accesó: Modificación Usuario", Time.valueOf(actionTime),id);
                }
        }
        if(event.getSource()==btnPassword){
            btnPassword.setDisable(false);
            txtPassword.setEditable(true);
            txtPassword.setText("");
        }
        if(event.getSource()==btnDelete){
            Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Confirmation");
            confirmation.setContentText("¿Desea eliminar al usuario?");
            Optional<ButtonType> result= confirmation.showAndWait();
            if(result.get()==ButtonType.OK){
                User user=tblUser.getSelectionModel().getSelectedItem();
                userDAO.delete(user);
                reloadUsers();
                clean();
                userDAO.insertAction("Accesó: Baja Usuario", Time.valueOf(actionTime),id);

            }
        }
        if(event.getSource()==btnBack){
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }
        if(event.getSource()==btnClean){
            clean();
        }

    }
    public void clickUser(){
        tblUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    User user=tblUser.getSelectionModel().getSelectedItem();
                    txtId.setText(String.valueOf(user.getId()));
                    txtUserName.setText(user.getUsername());
                    txtPassword.setText(user.getPassword());
                    cmbRol.setValue(user.getRol());
                    btnDelete.setDisable(false);
                    btnSave.setDisable(false);
                    txtId.setVisible(true);
                    lblId.setVisible(true);
                    txtId.setEditable(false);
                    btnPassword.setDisable(false);
                    txtPassword.setEditable(false);
                    insertMode=false;
                    updateMode=true;

                }
            }
        });
    }
    public void reloadUsers(){
        tblUser.getItems().clear();
        tblUser.setItems(userDAO.fetchAllUser());
        tblUser.refresh();
    }
    public boolean validateFields(){
        boolean ban=false;
        if(!txtUserName.getText().equals("")&&!txtPassword.getText().equals("")&&cmbRol.getSelectionModel().getSelectedItem()!=null){
            ban=true;
        }
        return ban;
    }
    public void clean(){
        txtId.setText("");
        txtUserName.setText("");
        txtPassword.setText("");
        cmbRol.setValue(null);
        btnDelete.setDisable(true);
        txtId.setVisible(false);
        lblId.setVisible(false);
        btnPassword.setDisable(true);
        txtPassword.setEditable(true);
        insertMode=true;
        updateMode=false;
    }

    public void insertUser() {
        if(validateFields()){
            String cveRol=userDAO.fetchCveRol(cmbRol.getValue());
            User user=new User(1,txtUserName.getText(),txtPassword.getText(),cveRol);
            if(userDAO.insert(user)){
                reloadUsers();
                clean();
            }
        }
        else {
            Alert al=new Alert(Alert.AlertType.WARNING);
            al.setContentText("Por favor llene todos los campos");
            al.show();
        }
    }
    public void updateUser() {
        if(validateFields()){
            String cveRol=userDAO.fetchCveRol(cmbRol.getValue());
            User user=new User(Integer.valueOf(txtId.getText()),txtUserName.getText(),txtPassword.getText(),cveRol);
            if(userDAO.update(user)){
                reloadUsers();
                clean();
            }
        }
        else {
            Alert al=new Alert(Alert.AlertType.WARNING);
            al.setContentText("Llena todos los campos");
            al.show();
        }
    }
    public void keyTyped(KeyEvent e) {
        int limit=15;
        if (txtUserName.getText().length()== limit){
            e.consume();
        }
    }
}
