package consInst;

import consFolio.ConsFolioDAO;
import documents.Addressee;
import documents.Document;
import documents.DocumentDAO;
import documents.Origin;
import institutions.Institutions;
import institutions.InstitutionsDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import login.MySQL;
import user.UserDAO;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class consInstController implements Initializable, EventHandler {
    @FXML
    Button btnBack, btnDownload,btnClean;
    @FXML
    ComboBox<String> cmbInstitution;
    @FXML
    TableView<Document> tblDocuments;
    @FXML
    TableColumn col1,col2,col3;
    @FXML
    TextField txtFolio, txtNumDocument,txtDateDocument,txtDateReception,txtFormat,txtType,txtInstituteOrigin,txtSender,txtMarket,txtAdressee,txtAffair,txtObservations,txtTownHall,txtInstruction,txtPriority,txtDateLimit,txtStatus,txtDateDelivery,txtRecive, txtDocName;

    ConsInstDAO consInstDAO=new ConsInstDAO(MySQL.getConnection());
    InstitutionsDAO institutionsDAO=new InstitutionsDAO(MySQL.getConnection());
    DocumentDAO documentDAO=new DocumentDAO(MySQL.getConnection());
    ConsFolioDAO consFolioDAO=new ConsFolioDAO(MySQL.getConnection());

    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Institutions> institutions=institutionsDAO.fetchAll();
        ObservableList<String> institution= FXCollections.observableArrayList();
        for (int i=0; i<institutions.size();i++){
            institution.add(institutions.get(i).getDescription());
        }
        cmbInstitution.setItems(institution);
        col1.setCellValueFactory(new PropertyValueFactory<>("noFolio"));
        col2.setCellValueFactory(new PropertyValueFactory<>("noDoc"));
        col3.setCellValueFactory(new PropertyValueFactory<>("docName"));

        addColum("Visualizar","/images/pdfIcon.png",1);
        addColum("Descargar","/images/dd.png",2);

        cmbInstitution.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                java.util.Date dateAction = new java.util.Date();
                String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();
                System.out.println(newValue);
                System.out.println(consInstDAO.fetchDoc(newValue).toString());
                tblDocuments.setItems(consInstDAO.fetchDoc(newValue));
                userDAO.insertAction("Accesó: Consulta doc. de "+newValue, Time.valueOf(actionTime),id);
            }
        });
        clickDocument();
        btnClean.setOnAction(this);
        btnBack.setOnAction(this);

    }

    @Override
    public void handle(Event event) {
        if (event.getSource()==btnClean){
            clean();
        }
        if(event.getSource()==btnBack){
            Stage stage= (Stage) btnBack.getScene().getWindow();
            stage.close();
        }

    }

   public void clickDocument(){
       tblDocuments.setOnMouseClicked(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               java.util.Date dateAction = new java.util.Date();
               String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();
               if (event.getClickCount()==2){
                   Document document= tblDocuments.getSelectionModel().getSelectedItem();
                   showInfo(document.getNoFolio(),document.getNoDoc());
               }
           }
       });
   }
    public boolean validate(){
        if (cmbInstitution.getSelectionModel().getSelectedItem().equals(null)){
            return false;
        }
        else {
            return true;
        }
    }
    private void addColum(String nameCol, String image,int opt) {
        TableColumn colBtn = new TableColumn(nameCol);

        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(final TableColumn param) {
                final TableCell cell = new TableCell() {

                    Button btn= new Button();
                    {
                        Image icon=new Image(image);
                        ImageView imageView=new ImageView(icon);
                        imageView.setFitWidth(30);
                        imageView.setFitHeight(30);
                        btn.setGraphic(imageView);
                        btn.setOnAction((ActionEvent event) -> {
                        /*FontAwesomeIconView icon1= new FontAwesomeIconView(FontAwesomeIcon.FILE_PDF_ALT);
                        icon1.setGlyphSize(25);
                        icon1.setFill(Paint.valueOf("red"));
                        btn.setGraphic(icon1);*/
                            Document document = (Document)getTableView().getItems().get(getIndex());
                            System.out.println(document.getNoFolio()+ " y "+document.getNoDoc());

                            switch (opt){
                                case 1:
                                    visualizePDF(document.getNoFolio(),document.getNoDoc());
                                    try {
                                        Desktop.getDesktop().open(new File("new.pdf")); //Inicia la aplicación asociada para abrir el archivo.
                                    } catch (IOException e1) {
                                        System.out.print("En desktop: ");
                                        e1.printStackTrace();
                                    }
                                    break;
                                case 2:
                                    pdfDownload(document.getNoFolio(),document.getNoDoc());
                                    break;
                            }
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        tblDocuments.getColumns().add(colBtn);
    }


    public void visualizePDF(int noFolio,int noDoc){
        try {
            byte[] pdf=documentDAO.getDocument(noFolio, noDoc);
            System.out.println(pdf);
            OutputStream out = new FileOutputStream("new.pdf");
            out.write(pdf);

            //abrir archivo
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pdfDownload(int noFolio,int noDoc) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo");

        //Seleccionar la extensión para los archivos de texto
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Guardar archivo seleccionado
        File file = fileChooser.showSaveDialog(null); //File es una representación abstracta de los nombres de archivos y directorios. Recibe una ruta de acceso
        // proporcionada por fileChooser.showSaveDialog(null);
        if (file != null) {
            byte[] doc=documentDAO.getDocument(noFolio, noDoc);
            System.out.println(doc);
            try {
                OutputStream out = new FileOutputStream(file);
                out.write(doc);

                //abrir archivo
                out.close();
            } catch (IOException e) {
                System.out.println("En visualizar pdf: ");
                e.printStackTrace();
            }
        }
    }
    private void showInfo(int nofolio, int noDoc) {
        Document document =consFolioDAO.fetchDoc(nofolio, noDoc);
        txtFolio.setText(document.getNoFolio()+"");
        txtNumDocument.setText(document.getNoDoc()+"");
        txtDocName.setText(document.getDocName());
        txtDateDocument.setText(document.getDocDate()+"");
        txtDateReception.setText(document.getReceptionDate()+"");
        String format=consFolioDAO.getNameFormat(document.getFormatCode()); //Debe realizar una consulta para obtener el nombre del formato a partir de cveFormato
        txtFormat.setText(format);
        String type=consFolioDAO.getNameType(document.getTypeCode()); //Debe realizar una consulta para obtener el nombre del tipo de doc a partir de cveTipo
        txtType.setText(type);

        //Realizar una consulta para obtener un objeto de tipo procedencia y obtener info de sus atributos a traves de cveProcedencia
        Origin origin=consFolioDAO.getOrigin(document.getOriginCode());

        String nameInstitute=consFolioDAO.getNameInstitute(origin.getInstitutionCode());//Busqueda de la descripcion por cveInstitucion
        txtInstituteOrigin.setText(nameInstitute);
        txtSender.setText(origin.getSender());
        txtMarket.setText(origin.getPosition());
        txtAdressee.setText(origin.getAddressee());
        txtAffair.setText(origin.getAffair());
        txtObservations.setText(origin.getObservations());


        //Realizar una consulta para obtener un objeto de tipo Destinatario y obtener info de sus atributos a traves de cveDestinatario
        Addressee addressee=consFolioDAO.getAddressee(document.getAddresseeCode());
        String townHall,instruction,priority;

        townHall=consFolioDAO.getNameTownHall(addressee.getAreaCode());
        txtTownHall.setText(townHall);
        instruction=consFolioDAO.getInstruction(addressee.getInstructionCode());
        txtInstruction.setText(instruction);
        priority=consFolioDAO.getPriority(addressee.getPriorityCode());
        txtPriority.setText(priority);
        txtDateLimit.setText(addressee.getLimitDate()+"");
        txtDateDelivery.setText(addressee.getDeliverDate()+"");
        txtRecive.setText(addressee.getReceive());
        txtStatus.setText(addressee.getStatus());
    }


    public void clean(){
        txtFolio.setText("");
        txtNumDocument.setText("");
        txtDateDocument.setText("");
        txtDateReception.setText("");
        txtFormat.setText("");
        txtType.setText("");
        txtInstituteOrigin.setText("");
        txtSender.setText("");
        txtMarket.setText("");
        txtAdressee.setText("");
        txtAffair.setText("");
        txtObservations.setText("");
        txtTownHall.setText("");
        txtInstruction.setText("");
        txtPriority.setText("");
        txtDateLimit.setText("");
        txtDateDelivery.setText("");
        txtRecive.setText("");
        txtStatus.setText("");
        txtDocName.setText("");
        tblDocuments.getItems().removeAll();
        cmbInstitution.setValue(null);
    }

}
