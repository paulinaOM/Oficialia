package official;

import boss.Boss;
import boss.BossDAO;
import boss.CBossController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.MySQL;
import user.UserDAO;

import java.net.URL;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;



public class officialController implements Initializable, EventHandler {
    @FXML
    Button btnSave, btnNew, btnDelete,btnBoss, btnRefresh,btnBack,btnClean;
    @FXML
    TextField txtAdress,txtPhone,txtBoss,txtSchedule;
    @FXML
    TableView<Official> tblOficial;
    @FXML
    TableColumn col1,col2;
    @FXML
    ComboBox<String> cmbState,cmbMunicipality;
    @FXML

    OfficialDAO officialDAO=new OfficialDAO(MySQL.getConnection());
    BossDAO bossDAO=new BossDAO(MySQL.getConnection());
    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status
    int idOfficial;

    Boolean updateMode=false;
    Boolean insertMode=true;
    int value1,value2,idJefe=1, idMun;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbState.setItems(officialDAO.fetchState());
        btnRefresh.setDisable(true);
        cmbState.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<String> filterData= officialDAO.fetchMunicipality(newValue);
                cmbMunicipality.setItems(filterData);
            }
        });
        col1.setCellValueFactory(new PropertyValueFactory<>("address"));
        col2.setCellValueFactory(new PropertyValueFactory<>("boss"));
        cmbMunicipality.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tblOficial.setItems(officialDAO.fetchOfficial(newValue));
                txtSchedule.setText("");
                txtPhone.setText("");
                txtAdress.setText("");
                txtBoss.setText("");
            }
        });

        clickOfficial();
        txtBoss.setEditable(false);
        btnNew.setOnAction(this);
        btnDelete.setOnAction(this);
        btnSave.setOnAction(this);
        txtBoss.setEditable(false);
        btnBoss.setOnAction(this);
        btnRefresh.setOnAction(this);
        btnBack.setOnAction(this);
        btnClean.setOnAction(this);
        txtPhone.setOnKeyTyped(this::keyTyped);

    }
    @Override
    public void handle(Event event) {
        java.util.Date dateAction = new java.util.Date();
        String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();
        if(event.getSource()==btnNew){
            insertMode=true;
            updateMode=false;
            btnDelete.setDisable(true);
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.PLUS_CIRCLE);
            icon.setGlyphSize(25);
            icon.setFill(Paint.valueOf("#259e4b"));
            btnBoss.setGraphic(icon);
            btnBoss.setDisable(false);
            clean();
        }
        if(event.getSource()==btnSave){
            if(insertMode){
                insertOfficial();
                userDAO.insertAction("Accesó: Alta Oficialía", Time.valueOf(actionTime),id);
            }
            else if (updateMode)
            {
                updateOfficial();
                userDAO.insertAction("Accesó: Modificación Oficialía", Time.valueOf(actionTime),id);
            }

        }
        if(event.getSource()==btnDelete){
            Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmación");
            confirmation.setContentText("¿Desea eliminar la Oficialía?");
            Optional<ButtonType> result= confirmation.showAndWait();
            if(result.get()==ButtonType.OK){
                Official official=tblOficial.getSelectionModel().getSelectedItem();
                officialDAO.delete(String.valueOf(official.getId()));
                reloadOfficial();
                clean();
                userDAO.insertAction("Accesó: Baja Oficialía", Time.valueOf(actionTime),id);
            }
        }
        if(event.getSource()==btnBoss){
            if(insertMode) {
                showStageInsert();
                value1=bossDAO.countBoss();
            }
            if(updateMode){
                idJefe=bossDAO.fetchBoss(txtBoss.getText());
                showStageUpdate(idJefe);
                reloadOfficial();
            }
        }
        if(event.getSource()==btnRefresh){
            if (insertMode){
                value2=bossDAO.countBoss();
                int dif=value2-value1;
                if(dif==1){
                    Boss boss=bossDAO.lastBoss();
                    txtBoss.setText(boss.getName());
                    btnBoss.setDisable(true);

                }
                if(dif==0){
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Aún no ha registrado los datos del jefe");
                    alert.show();
                }
            }
            if (updateMode){
                txtBoss.setText(officialDAO.getBossName(idOfficial));
            }
        }
        if(event.getSource()==btnClean){
            clean();
        }
        if(event.getSource()==btnBack){
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }

    }

    public void clickOfficial(){
        tblOficial.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Official official=tblOficial.getSelectionModel().getSelectedItem();
                    System.out.println("2:"+official);
                    idMun=official.getIdMun();
                    txtAdress.setText(official.getAddress());
                    txtBoss.setText(official.getBoss());
                    txtPhone.setText(String.valueOf(official.getPhone()));
                    txtSchedule.setText(official.getSchedule());
                    btnDelete.setDisable(false);
                    updateMode=true;
                    insertMode=false;
                    FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);
                    icon.setGlyphSize(25);
                    icon.setFill(Paint.valueOf("#259e4b"));
                    btnBoss.setGraphic(icon);
                    idOfficial=official.getId();
                    btnRefresh.setDisable(false);

                }
            }
        });
    }
    public void reloadOfficial(){
        tblOficial.getItems().clear();
        tblOficial.setItems(officialDAO.fetchOfficial(cmbMunicipality.getSelectionModel().getSelectedItem()));
        tblOficial.refresh();

    }
    public void clean(){
        txtAdress.setText("");
        txtBoss.setText("");
        txtPhone.setText("");
        txtSchedule.setText("");
        btnDelete.setDisable(true);
        btnBoss.setDisable(false);
        btnRefresh.setDisable(false);
        cmbMunicipality.setValue(null);
        cmbState.setValue(null);
    }

    public boolean validate(){
        boolean ban=false;
        if(!txtBoss.getText().equals("")&&!txtAdress.getText().equals("")&&!txtPhone.getText().equals("")&&!txtSchedule.getText().equals("")&&cmbState.getSelectionModel().getSelectedItem()!=null&&cmbMunicipality.getSelectionModel().getSelectedItem()!=null){
            ban=true;
        }
        return ban;
    }
    public void insertOfficial() {

        if(validate()){
            String idMun;
            int idBoss;
            idBoss=officialDAO.fetchBoss(txtBoss.getText());
            idMun=officialDAO.fetchIdMun(cmbState.getSelectionModel().getSelectedItem(),cmbMunicipality.getSelectionModel().getSelectedItem());
            Official official=new Official(1,cmbState.getSelectionModel().getSelectedItem(),cmbMunicipality.getSelectionModel().getSelectedItem(),txtAdress.getText(),Integer.valueOf(txtPhone.getText()),txtBoss.getText(),txtSchedule.getText(),Integer.valueOf(idMun),Integer.valueOf(idBoss));
            if(officialDAO.insert(official)){
                reloadOfficial();
                clean();
            }
        }
        else {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Debe llenar todos los campos");
            alert.show();
            txtBoss.setText("");
            reloadOfficial();
        }

    }
    public void updateOfficial() {

        Official official=new Official(idOfficial,cmbState.getSelectionModel().getSelectedItem(),cmbMunicipality.getSelectionModel().getSelectedItem(),txtAdress.getText(),Integer.valueOf(txtPhone.getText()),txtBoss.getText(),txtSchedule.getText(),idMun,idJefe);
        System.out.println("2:"+official);
        if(officialDAO.update(official)){
            reloadOfficial();
            clean();
        }
    }

    private void showStageInsert() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boss/createBoss.fxml"));
            Stage stage=new Stage();
            CBossController cBossController=new CBossController();
            loader.setController(cBossController);
            Parent root;
            root=loader.load();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene=new Scene(root,900,600);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void showStageUpdate(int id) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boss/createBoss.fxml"));
            Stage stage=new Stage();
            ImageView imageView=new ImageView();
            CBossController cBossController=new CBossController(false,true,id,imageView);
            loader.setController(cBossController);
            Parent root;
            root=loader.load();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene=new Scene(root,900,600);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void keyTyped(KeyEvent e) {
        char caracter = e.getCharacter().charAt(0);

        // Verificar si la tecla pulsada no es un digito
        if(((caracter < '0') ||
                (caracter > '9')) &&
                (caracter != '\b'))
        {
            e.consume();  // ignorar el evento de teclado
        }
        int limit=7;
        if (txtPhone.getText().length()== limit){

            e.consume();
        }
    }

    public void keyTypedString(KeyEvent ke) {

        char c=ke.getCharacter().charAt(0);

        if(Character.isDigit(c)) {
            ke.consume();

        }


    }



}
