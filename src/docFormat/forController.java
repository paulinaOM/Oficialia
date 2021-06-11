package docFormat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import login.MySQL;
import user.UserDAO;

import java.net.URL;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;


public class forController implements Initializable {
    @FXML
    Button btnAdd,btnDelete,btnCancel,btnReturn;

    @FXML
    TextField txtFormatKey,txtDescription;

    @FXML
    TableView<DocFormat> tblFormat;

    @FXML
    TableColumn col1,col2;

    DocFormatDAO docformatDAO=new DocFormatDAO(MySQL.getConnection());
    boolean insertMode=true;
    boolean updateMode=false;

    UserDAO userDAO=new UserDAO(MySQL.getConnection()); //----------------------------------Para invocar metodos que cambiaré de DAO
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su statusque tengan ustedes, porfa, es para agregar las actividades a la bitácora.

    java.util.Date dateAction = new java.util.Date();
    String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(new PropertyValueFactory<>("cveFormato"));
        col2.setCellValueFactory(new PropertyValueFactory<>("descripcion"));


        ObservableList<DocFormat> docformatList=docformatDAO.fetchAll();//Obtiene todos los registros de la BD
        tblFormat.setItems(docformatList);

        limitTextField(txtFormatKey,4);

        btnReturn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Stage)btnDelete.getScene().getWindow()).hide();


            }

        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean insertMode=true;
                boolean updateMode=false;
                btnDelete.setDisable(true);
                txtFormatKey.setEditable(true);
                clearForm();
            }
        });

        tblFormat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //generar evento al doble click
                if(event.getClickCount()==2){
                    updateMode=true;
                    insertMode=false;
                    txtFormatKey.setEditable(false);
                    btnDelete.setDisable(false);
                    tblFormat.getSelectionModel().getSelectedItem();
                    DocFormat docformat= tblFormat.getSelectionModel().getSelectedItem();

                    txtFormatKey.setText(docformat.getCveFormato());
                    txtDescription.setText(docformat.getDescripcion());

                }
            }
        });

        btnAdd.setOnAction(handlerSaveDocFormat);

        btnDelete.setOnAction(handlerDeleteDocFormat);

    }

    public void clearForm() {
        txtFormatKey.setText("");
        txtDescription.setText("");
        txtFormatKey.setEditable(true);
        btnDelete.setDisable(true);
    }

    EventHandler<ActionEvent> handlerSaveDocFormat=new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if(!docformatDAO.checkKey(txtFormatKey.getText())) {
                if(!txtFormatKey.getText().equals("") && !txtDescription.getText().equals("")) {
                    if (insertMode) {
                        insertDocFormat();
                        userDAO.insertAction("Accesó: Alta Formato documento", Time.valueOf(actionTime),id);
                    } else if (updateMode) {
                        updateDocFormat();
                        userDAO.insertAction("Accesó: Modificación Formato documento", Time.valueOf(actionTime),id);
                    }
                }else{
                    Alert al=new Alert(Alert.AlertType.INFORMATION);
                    al.setContentText("Debes llenar todos los campos");
                    al.show();
                }
            }else{
                Alert al=new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("La clave de formato ya ha sido registrada");
                al.show();
            }
        }


    };

    public void insertDocFormat(){
        DocFormat docformat=new DocFormat(txtFormatKey.getText(),
                txtDescription.getText());
        if(docformatDAO.insert(docformat)){
            reloadDocFormatList();
            clearForm();
        }
    }

    public void updateDocFormat(){
        DocFormat docformat=new DocFormat(txtFormatKey.getText(),
                txtDescription.getText());
        if(docformatDAO.update(docformat)){
            reloadDocFormatList();
            clearForm();
        }
    }

    public static void limitTextField(TextField textField, int limit) {
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter(textLimitFilter));
    }

    EventHandler<ActionEvent>handlerDeleteDocFormat=new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            if((docformatDAO.validateFormat(txtFormatKey.getText()))==0){
                Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmación");
                confirmation.setContentText("¿Desea eliminar el tipo de documento?");
                Optional<ButtonType> result= confirmation.showAndWait();
                if(result.get()==ButtonType.OK){
                    DocFormat docFormat=tblFormat.getSelectionModel().getSelectedItem();
                    docformatDAO.delete(docFormat.getCveFormato());
                    reloadDocFormatList();
                    clearForm();
                    userDAO.insertAction("Accesó: Baja Formato Documento", Time.valueOf(actionTime),id);
                }
            }
            else {
                Alert al=new Alert(Alert.AlertType.WARNING);
                al.setContentText("No puedes eliminarlo por cuestiones de integridad en los datos");
                al.show();
            }
        }
    };



    private void reloadDocFormatList() {
        tblFormat.getItems().clear();
        tblFormat.setItems(docformatDAO.fetchAll());//Muestra todos los datos
    }

    private void showStage(String path) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage=new Stage();
            Scene scene=new Scene(root);
            //scene.getStylesheets().add("tema.css");
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
