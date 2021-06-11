package boss;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import login.MySQL;
import user.UserDAO;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ResourceBundle;

public class CBossController implements Initializable, EventHandler {
   @FXML
   Button btnImage, btnSave,btnClean,btnBack;
   @FXML
   ImageView img;
   @FXML
   TextField txtName,txtAddress,txtPhone,txtId;
   @FXML
   Label lblFileName, lblId;

    UserDAO userDAO=new UserDAO(MySQL.getConnection());
    int id2=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status


    String filePath="";
    File path;
    BossDAO bossDAO=new BossDAO(MySQL.getConnection());
    boolean insertMode=true;
    boolean updateMode=false;
    int id;


    public CBossController(boolean insertMode, boolean updateMode, int id, ImageView img) {
        this.insertMode = insertMode;
        this.updateMode = updateMode;
        this.id = id;
        this.img=img;
    }

    public CBossController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnImage.setOnAction(this);
        btnSave.setOnAction(this);
        btnClean.setOnAction(this);
        btnBack.setOnAction(this);
        txtId.setEditable(false);
        txtId.setVisible(false);
        lblId.setVisible(false);
        if(updateMode){
            txtId.setVisible(true);
            lblId.setVisible(true);
            btnClean.setVisible(false);
            selectBoss();
        }
        if(insertMode){
            txtId.setVisible(false);
            lblId.setVisible(false);
            btnClean.setVisible(true);
        }
        txtPhone.setOnKeyTyped(this::keyTyped);
        img.setFitHeight(300);
        img.setFitWidth(300);


    }
    @Override
    public void handle(Event event) {
        java.util.Date dateAction = new java.util.Date();
        String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();
        if(event.getSource()==btnImage){
            try {
                selectImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(event.getSource()==btnSave){
            if(insertMode){
                if(validateFields()&&filePath.length()!=0){
                    insertBoss();
                    Stage stage = (Stage) btnSave.getScene().getWindow();
                    stage.close();
                    userDAO.insertAction("Accesó: Inserción Jefe", Time.valueOf(actionTime),id2);
                }
                else {
                    Alert al=new Alert(Alert.AlertType.WARNING);
                    al.setContentText("Debes adjuntar una foto y llenar todos los campos");
                    al.show();
                }
            }
            if(updateMode){
                if(validateFields()){
                    updateBoss();
                    Stage stage = (Stage) btnSave.getScene().getWindow();
                    stage.close();
                    userDAO.insertAction("Accesó: Modificación Jefe", Time.valueOf(actionTime),id2);
                }
                else {
                    Alert al=new Alert(Alert.AlertType.WARNING);
                    al.setContentText("Debes llenar todos los campos");
                    al.show();
                }
            }

        }
        if(event.getSource()==btnClean){
            clean();
        }
        if(event.getSource()==btnBack){
            Stage stage = (Stage) btnBack.getScene().getWindow();
            // do what you have to do
            stage.close();
        }

    }
    //Convierte los bytes en una imagen
    public Image convByteToImage(byte data[]){
        try{
            BufferedImage image=null;
            try{
                image=ImageIO.read(new ByteArrayInputStream(data));
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
    //abre el explorador de archivos y muestra en pantalla la imagen seleccionada
    public void selectImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Imagen");

        // Agregar filtros para facilitar la busqueda
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "."),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        // Obtener la imagen seleccionada
        File imgFile = fileChooser.showOpenDialog(null);
        if(imgFile!=null){
            lblFileName.setText(imgFile.getName()); //Obtiene la ruta del archivo seleccionado
            filePath=imgFile.getAbsolutePath();
            path = new File(filePath);

            //Pasar file a byte[]
            byte[] imagenSele = null;
            imagenSele = convFileToByte(imgFile);

            //Pasar byte[] a image
            Image IMGSeleccionada = convByteToImage(imagenSele);

            //Muestra la imagen seleccionada
            img.setImage(IMGSeleccionada);

            //Preparar imagen seleccionada para subir a base de datos
            try {
                SerialBlob imagenAsubir = new SerialBlob(imagenSele);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else{ //Valida que se haya seleccionado un archivo (No sé a donde moverlo xd)
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setContentText("No ha seleccionado un archivo");
            al.show();
        }

    }
    public byte [] convFileToByte(File path){
        try {
            byte[] imgPath = new byte[(int) path.length()];
            InputStream input = new FileInputStream(path);
            input.read(imgPath);
            return imgPath;

        } catch (IOException ex) {
            return null;
        }

    }

    public boolean validateFields(){
        boolean ban=false;
        if(!txtName.getText().equals("")&&!txtAddress.getText().equals("")&&!txtPhone.getText().equals("")){
            ban=true;
        }
        return ban;
    }
    public void clean() {
        txtName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        lblFileName.setText("");
        txtId.setText("");
        filePath="";
    }
    public void insertBoss() {

        Boss boss= new Boss();
        boss.setId(1);
        boss.setName(txtName.getText());
        boss.setAddress(txtAddress.getText());
        boss.setPhone(Integer.valueOf(txtPhone.getText()));
        try {

            byte[] imgPath = new byte[(int) path.length()];
            InputStream input = new FileInputStream(path);
            input.read(imgPath);
            boss.setImg(imgPath);

        } catch (IOException ex) {
            boss.setImg(null);
        }
        if(bossDAO.insert(boss)){
            clean();
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setContentText("Guardado correctamente");
            al.show();
        }
    }

    public void updateBoss(){
        Boss boss=bossDAO.selectBoss(Integer.valueOf(txtId.getText()));
        boss.setName(txtName.getText());
        boss.setAddress(txtAddress.getText());
        boss.setPhone(Integer.valueOf(txtPhone.getText()));
       //aquí
        try {

            byte[] imgPath = new byte[(int) path.length()];
            InputStream input = new FileInputStream(path);
            input.read(imgPath);
            boss.setImg(imgPath);

        } catch (IOException ex) {
            boss.setImg(null);
        }
        if(bossDAO.update(boss)){
            clean();
        }
    }

    public void selectBoss(){
        Boss boss=bossDAO.selectBoss(id);
        System.out.println(boss.getId());
        
        txtId.setText(String.valueOf(boss.getId()));
        txtId.setText(String.valueOf(boss.getId()));
        txtName.setText(boss.getName());
        txtAddress.setText(boss.getAddress());
        txtPhone.setText(String.valueOf(boss.getPhone()));
        txtId.setVisible(true);
        btnClean.setVisible(false);
        lblId.setVisible(true);
        img.setImage(convByteToImage(boss.getImg()));

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

}
