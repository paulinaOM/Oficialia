package login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import user.UserDAO;


import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/*
NOTAS: En home.fxml el padding es el espacio entre el contenedor y sus hijos
 */


public class loginController implements Initializable, EventHandler<ActionEvent> {
    @FXML
    TextField txtUserName, txtPassword;
    @FXML
    Button btnAcept,btnClose;


    java.util.Date date = new java.util.Date();
    String today,time;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAcept.setOnAction(this);
        btnClose.setOnAction(this);

    }

    @Override
    public void handle(ActionEvent event) {
        if(event.getSource()==btnAcept){
            validateCredentials();
        }
        if(event.getSource()==btnClose){
            System.exit(0);
        }
    }

    public void validateCredentials(){

        UserDAO userDAO = new UserDAO(MySQL.getConnection());//Recibe la conexión
        int id=0;
        id =userDAO.validUser(txtUserName.getText(),txtPassword.getText());
        if(id!=0){
            userDAO.activateStatus(id);
            showStage();
            ((Stage)(btnAcept.getScene().getWindow())).hide();//Cerrar la ventana actual, mediante método hide().Accediendo al Stage
            //al cual pertenece el botón btnAceptar.
        }
        else{
            Alert al=new Alert(Alert.AlertType.WARNING);
            al.setContentText("Credenciales incorrectas");
            al.show();
        }


    }

    //PARA CREAR UNA NUEVA STAGE
    public void showStage(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/configuration/configuration.fxml"));
            Stage stage=new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene=new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
