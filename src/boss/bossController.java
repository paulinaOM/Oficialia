package boss;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import login.MySQL;
import user.UserDAO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;

public class bossController implements Initializable, EventHandler {
    @FXML
    Button  btnSave, btnDelete,btnBack,btnClean;
    @FXML
    ImageView img;
    @FXML
    TextField txtName,txtAddress,txtPhone,txtId;
    @FXML
    TableView<Boss> tblBoss;
    @FXML
    TableColumn col1,col2,col3,col4;

    BossDAO bossDAO= new BossDAO(MySQL.getConnection());

    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setOnAction(this);
        btnBack.setOnAction(this);
        btnDelete.setOnAction(this);
        btnClean.setOnAction(this);
        txtId.setEditable(false);
        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("address"));
        col3.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col4.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblBoss.setItems(bossDAO.fetchAllBoss());
        btnDelete.setDisable(true);
        btnSave.setDisable(true);
        txtId.setEditable(false);
        txtName.setEditable(false);
        txtAddress.setEditable(false);
        txtPhone.setEditable(false);
        clickOfficial();

    }


    @Override
    public void handle(Event event) {
        java.util.Date dateAction = new java.util.Date();
        String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();

        if(event.getSource()==btnBack){
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }
        if(event.getSource()==btnSave){
            if(validateFields()){
                updateBoss();
                userDAO.insertAction("Accesó: Modificación Jefe", Time.valueOf(actionTime),id);
            }
            else {
                Alert al=new Alert(Alert.AlertType.WARNING);
                al.setContentText("Llena todos los campos");
                al.show();
            }
        }
        if(event.getSource()==btnClean){
            clean();
        }
        if(event.getSource()==btnDelete){
            if(bossDAO.validateBoss(Integer.valueOf(txtId.getText()))==0){
                Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmación");
                confirmation.setContentText("¿Desea eliminar al jefe?");
                Optional<ButtonType> result= confirmation.showAndWait();
                if(result.get()==ButtonType.OK){
                    Boss boss=tblBoss.getSelectionModel().getSelectedItem();
                    bossDAO.delete(Integer.valueOf(boss.getId()));
                    reloadBoss();
                    clean();
                    userDAO.insertAction("Accesó: Baja Jefe", Time.valueOf(actionTime),id);
                }
            }
            else {
                Alert al=new Alert(Alert.AlertType.WARNING);
                al.setContentText("No puedes eliminarlo ya que actualmente es jefe de una oficialía");
                al.show();
            }
        }
    }
    public void clickOfficial(){
        tblBoss.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Boss boss=tblBoss.getSelectionModel().getSelectedItem();
                    txtId.setText(String.valueOf(boss.getId()));
                    txtName.setText(boss.getName());
                    txtAddress.setText(boss.getAddress());
                    txtPhone.setText(String.valueOf(boss.getPhone()));
                    txtName.setEditable(true);
                    txtAddress.setEditable(true);
                    txtPhone.setEditable(true);
                    btnDelete.setDisable(false);
                    btnSave.setDisable(false);
                    img.setImage(convByteToImage(boss.getImg()));
                }
            }
        });
    }
    //Convierte los bytes en una imagen
    public Image convByteToImage(byte data[]){
        try{
            BufferedImage image=null;
            try{
                image= ImageIO.read(new ByteArrayInputStream(data));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            Image img= SwingFXUtils.toFXImage(image,null);

            return img;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }
    public void updateBoss(){
        Boss boss=bossDAO.selectBoss(Integer.valueOf(txtId.getText()));
        boss.setName(txtName.getText());
        boss.setAddress(txtAddress.getText());
        boss.setPhone(Integer.valueOf(txtPhone.getText()));
        if(bossDAO.update(boss)){
            clean();
            reloadBoss();
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
    public boolean validateFields(){
        boolean ban=false;
        if(!txtName.getText().equals("")&&!txtAddress.getText().equals("")&&!txtPhone.getText().equals("")){
            ban=true;
        }
        return ban;
    }
    public void reloadBoss(){
        tblBoss.getItems().clear();
        tblBoss.setItems(bossDAO.fetchAllBoss());
        tblBoss.refresh();

    }
    public void clean() {
        txtName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtId.setText("");
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        Image image=new Image("/images/boss.png");
        img.setImage(image);
    }

}
