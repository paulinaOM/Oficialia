package consFolio;

import documents.Addressee;
import documents.Document;
import documents.DocumentDAO;
import documents.Origin;
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

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("Duplicates")

public class FolioController implements Initializable, EventHandler {
    @FXML
    Button btnBack,btnClean,btnSearch;
    @FXML
    TextField txtSearchFolio,txtFolio,txtNumDocument,txtDocName,txtDateDocument,txtDateReception,txtFormat,txtType,txtInstituteOrigin,txtSender,txtPosition,
    txtAdressee,txtAffair,txtObservations,txtTownHall,txtInstruction,txtPriority,txtDateLimit,txtDateDelivery,txtRecive,txtStatus;
    @FXML
    TableView<Document> tblDocuments;
    @FXML
    TableColumn col1,col2,col3;

    DocumentDAO documentDAO=new DocumentDAO(MySQL.getConnection());
    ConsFolioDAO consFolioDAO=new ConsFolioDAO(MySQL.getConnection());
    ObservableList<Document> docList;
    //File pdfDownload;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtSearchFolio.setPromptText("Folio");

        col1.setCellValueFactory(new PropertyValueFactory<>("noFolio"));
        col2.setCellValueFactory(new PropertyValueFactory<>("noDoc"));
        col3.setCellValueFactory(new PropertyValueFactory<>("docName"));

        addColum("Visualizar","/images/pdfIcon.png",1);
        addColum("Descargar","/images/dd.png",2);

        tblDocuments.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount()==2){
                    Document document= tblDocuments.getSelectionModel().getSelectedItem();
                    showInfo(document.getNoFolio(),document.getNoDoc());
                    txtSearchFolio.setDisable(true);
                    btnSearch.setDisable(true);
                }
            }
        });

        btnSearch.setOnAction(this);
        btnClean.setOnAction(this);
        btnBack.setOnAction(this);
    }

    @Override
    public void handle(Event event) {
        if (event.getSource()==btnSearch){
            if (validate()){
                docList=consFolioDAO.fetchAllDoc(Integer.valueOf(txtSearchFolio.getText()));
                if (docList.size()>0){
                    System.out.println("DocList "+docList.toString());
                    tblDocuments.setItems(docList);
                }
                else {
                    Alert al = new Alert(Alert.AlertType.INFORMATION);
                    al.setContentText("Folio no registrado");
                    al.show();
                }
                txtSearchFolio.setText("");
            }
            else{
                Alert al=new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("Debes llenar todos los campos");
                al.show();
            }
        }
        if (event.getSource()==btnClean){
            clear();
        }
        if(event.getSource()==btnBack){
            Stage stage= (Stage) btnBack.getScene().getWindow();
            stage.close();
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
        txtPosition.setText(origin.getPosition());
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

    public boolean validate(){
        if (!txtSearchFolio.getText().equals("")){
            return true;
        }
        else {
            return false;
        }
    }

    public void clear(){
        txtSearchFolio.setText("");
        txtFolio.setText("");
        txtNumDocument.setText("");
        txtDateDocument.setText("");
        txtDateReception.setText("");
        txtFormat.setText("");
        txtType.setText("");
        txtInstituteOrigin.setText("");
        txtSender.setText("");
        txtPosition.setText("");
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
        txtSearchFolio.setDisable(false);
        btnSearch.setDisable(false);
        txtSearchFolio.requestFocus();
        tblDocuments.getItems().removeAll(docList);
    }

    public void keyTyped(){

    }
}
