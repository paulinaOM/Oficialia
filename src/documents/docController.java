package documents;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import login.MySQL;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class docController implements Initializable, EventHandler {
    @FXML
    TextField txtFolio,txtNumDocument,txtSender,txtPosition,txtAddressee,txtAffair,txtObservations,txtReceiveBy;
    @FXML
    DatePicker dpDateDocument,dpDateReception,dpDateLimit,dpDateDelivery;
    @FXML
    ComboBox cmbFormat,cmbType,cmbInstituteOrigin,cmbTownHall,cmbInstruction,cmbPriority;
    @FXML
    Button btnAttach,btnSave,btnCancel,btnBack;
    @FXML
    ToggleButton tglDelivery;
    @FXML
    Label lblFileName, lblFilePath;

    String filePath="";
    File path;
    Boolean ban=false;
    AddresseeDAO addresseeDAO =new AddresseeDAO(MySQL.getConnection());
    OriginDAO originDAO= new OriginDAO(MySQL.getConnection());
    DocumentDAO documentDAO=new DocumentDAO(MySQL.getConnection());


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clear();
        cmbTownHall.setItems(addresseeDAO.fetchAllAreas());
        cmbInstruction.setItems(addresseeDAO.fetchAllInstructions());
        cmbPriority.setItems(addresseeDAO.fetchAllPriority());
        cmbInstituteOrigin.setItems(originDAO.fetchAllInstOrigin());
        cmbType.setItems(documentDAO.fetchAllTypes());
        cmbFormat.setItems(documentDAO.fetchAllFormats());

        tglDelivery.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("oldValue: "+oldValue+" New Value: "+newValue);
                ban=newValue;
                if(newValue){
                    dpDateDelivery.setDisable(false);
                    txtReceiveBy.setDisable(false);
                }
                else {
                    dpDateDelivery.setDisable(true);
                    txtReceiveBy.setDisable(true);
                }
            }
        });
        dpDateDelivery.setEditable(false);
        dpDateDocument.setEditable(false);
        dpDateLimit.setEditable(false);
        dpDateReception.setEditable(false);
        btnAttach.setOnAction(this);
        btnSave.setOnAction(this);
        btnCancel.setOnAction(this);
        btnBack.setOnAction(this);
    }

    @Override
    public void handle(Event e) {
        if (e.getSource()==btnAttach){
            selectPDF();
        }
        if(e.getSource()==btnBack){
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }
        if (e.getSource()==btnSave){
            if(!documentDAO.checkFolio(Integer.valueOf(txtFolio.getText()),Integer.valueOf(txtNumDocument.getText()))){//Verifica que no se haya dado de alta ese folio mediante el método check
                //que devuelve true si ya existe ese folio, por eso se niega, para que no entre
                if (filePath.trim().length() != 0 && validateFields()&&validateTgl()){ //Si el string de la ruta no está vacío.
                    // trim es un método que elimina los caracteres blancos iniciales y finales de la cadena, devolviendo una copia de la misma.
                    insertOrigin();
                    insertAddreessee();

                    System.out.println("Filepath.trim.length"+filePath.trim().length());

                    convFileToByte(path);
                    filePath = "";
                    System.out.println("LLegó");
                    //Desactivar  boton de guardar u otros
                    clear();
                } else {
                    Alert al=new Alert(Alert.AlertType.INFORMATION);
                    al.setContentText("Debes adjuntar un documento y llenar todos los campos");
                    al.show();
                }
            }
            else {
                Alert al=new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("El documento con ese identificador ya se encuentra registrado");
                al.show();
            }
        }
        if (e.getSource()==btnCancel){
            clear();
        }
    }

    public void selectPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Archivo");

        // Agregar filtros para facilitar la busqueda
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        // Obtener archivo seleccionado
        File pdf = fileChooser.showOpenDialog(null);

        if(pdf!=null){
            lblFileName.setText(pdf.getName());//Obtiene el nombre del archivo que se ha seleccionado
            lblFilePath.setText(pdf.getAbsolutePath()); //Obtiene la ruta del archivo seleccionado
            filePath=pdf.getAbsolutePath();
            path = new File(filePath);
            btnSave.setDisable(false);
        }
        else{ //Valida que se haya seleccionado un archivo (No sé a donde moverlo xd)
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setContentText("No ha seleccionado un archivo");
            al.show();
        }
    }

    private void convFileToByte(File path) {
        DocumentDAO documentDAO = new DocumentDAO(MySQL.getConnection());
        Document document=new Document();

        document.setNoFolio(Integer.valueOf(txtFolio.getText()));
        document.setNoDoc(Integer.valueOf(txtNumDocument.getText()));
        document.setDocName(lblFileName.getText());
        //document.setDocDate(Date.valueOf(dpDateDocument.getValue()));
        //document.setReceptionDate(Date.valueOf(dpDateReception.getValue()));
        //*
        Date docDate=Date.valueOf(dpDateDocument.getValue());
        docDate.setDate(docDate.getDate()+1);
        System.out.println("doc date +1: "+docDate);
        document.setDocDate(Date.valueOf(docDate.toLocalDate()));
        Date receptDate= Date.valueOf(dpDateReception.getValue());
        receptDate.setDate(receptDate.getDate()+1);
        System.out.println("recept date +1: "+receptDate);
        document.setReceptionDate(Date.valueOf(receptDate.toLocalDate()));
        //*
        document.setOriginCode(Integer.valueOf(originDAO.getLastCodeOrigin()));
        document.setAddresseeCode(Integer.valueOf(addresseeDAO.getLastCodeAddressee()));
        document.setFormatCode(documentDAO.formatCode(cmbFormat.getSelectionModel().getSelectedItem().toString()));
        document.setTypeCode(documentDAO.typeCode(cmbType.getSelectionModel().getSelectedItem().toString()));
        System.out.println(document.toString());
        System.out.println((int)path.length());
        try {
            byte[] pdfPath = new byte[(int) path.length()];
            InputStream input = new FileInputStream(path);
            input.read(pdfPath);
            document.setDocPDF(pdfPath);
        } catch (IOException ex) {
            document.setDocPDF(null);
        }
        documentDAO.insert(document);
    }

    public void insertOrigin(){
        Origin origin=new Origin();
        origin.setSender(txtSender.getText());
        origin.setAffair(txtAffair.getText());
        origin.setPosition(txtPosition.getText());
        origin.setAddressee(txtAddressee.getText());
        origin.setObservations(txtObservations.getText());
        origin.setInstitutionCode(originDAO.instCode(cmbInstituteOrigin.getSelectionModel().getSelectedItem().toString()));
        originDAO.insert(origin);
    }



    public void insertAddreessee(){
        Addressee addressee=new Addressee();
        //addressee.setLimitDate(Date.valueOf(dpDateLimit.getValue()));
        //*
        Date limitDate= Date.valueOf(dpDateLimit.getValue());
        limitDate.setDate(limitDate.getDate()+1);
        System.out.println("limit date +1: "+limitDate);
        addressee.setLimitDate(limitDate);
        //*
        addressee.setAreaCode(addresseeDAO.areaCode(cmbTownHall.getSelectionModel().getSelectedItem().toString()));
        addressee.setInstructionCode(addresseeDAO.instructionCode(cmbInstruction.getSelectionModel().getSelectedItem().toString()));
        addressee.setPriorityCode(addresseeDAO.priorityCode(cmbPriority.getSelectionModel().getSelectedItem().toString()));
        if(tglDelivery.isSelected()){
            if(dpDateDelivery.getValue()!=null&& !txtReceiveBy.getText().equals(null)){
                System.out.println("Es dif de null");
                addressee.setStatus("Sí");
                //addressee.setDeliverDate(Date.valueOf(dpDateDelivery.getValue()));
                //*
                Date deliverDate= Date.valueOf(dpDateDelivery.getValue());
                deliverDate.setDate(deliverDate.getDate()+1);
                System.out.println("deliver date +1: "+deliverDate);
                addressee.setDeliverDate(deliverDate);
                //*
                addressee.setReceive(txtReceiveBy.getText());
                addresseeDAO.insert(addressee);
            }
            else{
                Alert al=new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("Debes llenar todos los campos");
                al.show();
            }
        }
        else {
            System.out.println("Es igual a null");
            addressee.setStatus("No");
            addressee.setDeliverDate(null);
            addressee.setReceive("No recibido");
            addresseeDAO.insert(addressee);
        }
    }


    private boolean validateFields() {
        boolean ban=false;

        if(!txtFolio.getText().equals("") && !txtNumDocument.getText().equals("") && !txtSender.getText().equals("") && !txtPosition.getText().equals("")
                && !txtAddressee.getText().equals("")&& !txtAffair.equals("") && !txtObservations.equals("")&&dpDateDocument.getValue()!=null&&dpDateReception.getValue()!=null
                &&cmbFormat.getSelectionModel().getSelectedItem()!=null&&cmbType.getSelectionModel().getSelectedItem()!=null
                &&cmbInstituteOrigin.getSelectionModel().getSelectedItem()!=null &&cmbTownHall.getSelectionModel().getSelectedItem()!=null
                &&cmbInstruction.getSelectionModel().getSelectedItem()!=null&&cmbPriority.getSelectionModel().getSelectedItem()!=null&&dpDateLimit.getValue()!=null){
            ban=true;
        }
        System.out.println("ban "+ ban);
        return ban;
    }

    public boolean validateTgl(){
        boolean ban1=false;
        if(ban){
            if(dpDateDelivery.getValue()!=null&&!txtReceiveBy.getText().equals(null)){
                ban1=true;
            }
            else{
                Alert al=new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("Debes llenar todos los campos");
                al.show();
            }
        }
        else{
            ban1=true;
        }
        return ban1;
    }

    private void clear() {
        txtFolio.setText("");
        txtNumDocument.setText("");
        dpDateDocument.setValue(null);
        dpDateReception.setValue(null);
        cmbFormat.valueProperty().setValue(null);
        cmbType.valueProperty().setValue(null);
        cmbInstituteOrigin.valueProperty().setValue(null);
        txtSender.setText("");
        txtPosition.setText("");
        txtAddressee.setText("");
        txtAffair.setText("");
        txtObservations.setText("");
        cmbTownHall.valueProperty().setValue(null);
        cmbInstituteOrigin.valueProperty().setValue(null);
        cmbPriority.valueProperty().setValue(null);
        dpDateLimit.setValue(null);
        tglDelivery.selectedProperty().setValue(false);
        dpDateDelivery.setValue(null);
        dpDateDelivery.setDisable(true);
        txtReceiveBy.setText("");
        txtReceiveBy.setDisable(true);
        btnAttach.setDisable(false);
        btnSave.setDisable(true);
        cmbTownHall.setPromptText("Seleccione");
        cmbInstruction.setPromptText("Seleccione");
        cmbPriority.setPromptText("Seleccione");
        cmbInstituteOrigin.setPromptText("Seleccione");
        cmbFormat.setPromptText("Seleccione");
        cmbType.setPromptText("Seleccione");
        txtFolio.requestFocus();
        lblFilePath.setText("Ruta");
        lblFileName.setText("Nombre de archivo");
    }

}