package mainMenu;

import boss.Boss;
import boss.BossDAO;
import departments.DepartmentsDAO;
import departments.PDF2Departments;
import institutions.InstitutionPDF;
import institutions.InstitutionsDAO;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import login.MySQL;
import pdf5.Pdf5Access;
import pdf5.Pdf5DAO;
import pdf6.Pdf6DAO;
import pdf6.Pdf6Reports;
import pdf7.Pdf7Activity;
import pdf7.Pdf7DAO;
import user.UserDAO;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.sql.Time;
import java.util.ResourceBundle;

public class Controller implements Initializable, EventHandler{
    @FXML
    MenuItem mnuRegister, mnuConsFolio, mnuConsInstit, mnuInstitutions,
            mnuDept, mnuFormat, mnuTypes, mnuPdf1, mnuPdf2, mnuPdf3, mnuPdf4, mnuPdf5, mnuUsers, mnuOffice, mnuBoss,mnuPdf6,mnuPdf7,mnuPdf8;
    @FXML
    Button btnExit;
    @FXML
    Label lblAddress, lblSchedule,lblPhone,lblStateMun,lblMenu, lblBoss,lblPhoneBoss;
    @FXML
    SplitMenuButton splitAdmin, splitReports,splitCatalogs;

    String state,municipality,address,boss,schedule;
    int phone;

    public Controller(String state, String municipality, String address, String boss, String schedule, int phone) {
        this.state = state;
        this.municipality = municipality;
        this.address = address;
        this.boss = boss;
        this.schedule = schedule;
        this.phone = phone;
    }

    //pdf
    public static final String DEST1 = "oficialia/reports/institutions.pdf";
    public static final String DEST2="oficialia/reports/areasAyuntamiento.pdf";
    public static final String DEST5="oficialia/reports/bitacoraAccesos.pdf";
    public static final String DEST6="oficialia/reports/bitacoraReportesGenerados.pdf";
    public static final String DEST7="oficialia/reports/bitacoraActividades.pdf";

    String startTime, endTime;
    java.util.Date dateInit = new java.util.Date();
    UserDAO userDAO=new UserDAO(MySQL.getConnection()); //----------------------------------Para invocar metodos que cambiaré de DAO
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo para cambiar su status
    String rol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startTime=dateInit.getHours()+":"+dateInit.getMinutes()+":"+dateInit.getSeconds();
        System.out.println("start time "+Time.valueOf(startTime));
        userDAO.insertAccess(Time.valueOf(startTime),id);
        rol=userDAO.searchActiveRol();
        permission();
        String aux="Somos la oficina encargada de brindar servicios centralizados  de recepción y despacho de demandas, promociones, recursos de reclamación, recursos de apelación, recursos de revisión, demandas de amparo,consignaciones y correspondencia oficial.";
        lblMenu.setText(aux);

        mnuRegister.setOnAction(this);
        mnuConsFolio.setOnAction(this);
        mnuConsInstit.setOnAction(this);
        mnuInstitutions.setOnAction(this);
        mnuBoss.setOnAction(this);
        mnuDept.setOnAction(this);
        mnuFormat.setOnAction(this);
        mnuTypes.setOnAction(this);
        mnuPdf1.setOnAction(this);
        mnuPdf2.setOnAction(this);
        mnuPdf3.setOnAction(this);
        mnuPdf4.setOnAction(this);
        mnuPdf5.setOnAction(this);
        mnuPdf6.setOnAction(this);
        mnuPdf7.setOnAction(this);
        mnuPdf8.setOnAction(this);
        mnuUsers.setOnAction(this);
        mnuOffice.setOnAction(this);
        btnExit.setOnAction(this);

        lblBoss.setText(boss);
        BossDAO bossDAO=new BossDAO(MySQL.getConnection());
        Boss b;
        int id=bossDAO.fetchBoss(boss);
        b=bossDAO.selectBoss(id);
        lblPhoneBoss.setText(String.valueOf(b.getPhone()));
        lblAddress.setText(address);
        lblPhone.setText("Tel. "+String.valueOf(phone));
        lblSchedule.setText("Horario: "+schedule);
        lblStateMun.setText(municipality+", "+state);

    }

    @Override
    public void handle(Event event) {
        java.util.Date dateAction = new java.util.Date();
        String actionTime=dateAction.getHours()+":"+dateAction.getMinutes()+":"+dateAction.getSeconds();
        System.out.println("dateAction "+Time.valueOf(actionTime));

        if (event.getSource() == mnuRegister) {
            try {
                userDAO.insertAction("Accesó: Alta documento",Time.valueOf(actionTime),id);
                Parent root = FXMLLoader.load(getClass().getResource("/documents/document.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root, 1000, 600);
                scene.getStylesheets().add("style.css");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (event.getSource() == mnuConsFolio) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/consFolio/consFolio.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root, 1000, 600);
                scene.getStylesheets().add("style.css");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                userDAO.insertAction("Accesó: Consulta por folio",Time.valueOf(actionTime),id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (event.getSource() == mnuConsInstit) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/consInst/consInst.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root, 1000, 600);
                scene.getStylesheets().add("style.css");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                userDAO.insertAction("Accesó: Consulta por Inst.",Time.valueOf(actionTime),id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (event.getSource() == mnuInstitutions) {
            String path = "/institutions/institutions.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú Instituciones",Time.valueOf(actionTime),id);
        }
        if (event.getSource() == mnuDept) {
            String path = "/departments/departments.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú Áreas Ayto.",Time.valueOf(actionTime),id);
        }
        if (event.getSource() == mnuFormat) {
            String path = "/docFormat/format.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú Formatos",Time.valueOf(actionTime),id);
        }
        if (event.getSource() == mnuTypes) {
            String path = "/DocumentTypes/types.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú Tipos Doc.",Time.valueOf(actionTime),id);
        }

        if (event.getSource() == mnuPdf1) {
            File file = new File(DEST1);
            file.getParentFile().mkdirs();
            try {
                Alert al = new Alert(Alert.AlertType.INFORMATION);
                al.setHeaderText("");
                al.setTitle("Oficialía");
                al.setContentText("Archivo generado exitosamente");
                InstitutionsDAO institutionsDAO = new InstitutionsDAO(MySQL.getConnection());
                new InstitutionPDF().createPdf(DEST1, institutionsDAO.fetchAll());
                al.show();
                userDAO.insertAction("Generó PDF1",Time.valueOf(actionTime),id);
                int lastAccess=userDAO.getLastAccess();
                userDAO.insertReportCreation(lastAccess,1,Time.valueOf(actionTime));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (event.getSource() == mnuPdf2) {
            File file = new File(DEST2);
            file.getParentFile().mkdirs();
            DepartmentsDAO departmentsDAO =new DepartmentsDAO(MySQL.getConnection());
            try {
                System.out.println(departmentsDAO.fetchAllDepartments().toString());
                new PDF2Departments().createPdf(DEST2,departmentsDAO.fetchAllDepartments());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("");
            al.setContentText("Archivo generado exitosamente");
            al.show();

            userDAO.insertAction("Generó PDF2",Time.valueOf(actionTime),id);
            int lastAccess=userDAO.getLastAccess();
            userDAO.insertReportCreation(lastAccess,2,Time.valueOf(actionTime));
        }
        if (event.getSource() == mnuPdf3) {
            String path="/pdf3/pdf3.fxml";
            try {
                Parent root = FXMLLoader.load(getClass().getResource(path));
                Stage stage = new Stage();
                Scene scene = new Scene(root, 450, 200);
                scene.getStylesheets().add("style.css");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            userDAO.insertAction("Accesó: Menú PDF3",Time.valueOf(actionTime),id);
        }
        if (event.getSource() == mnuPdf4) {
            String path="/pdf4/pdf4.fxml";
            try {
                Parent root = FXMLLoader.load(getClass().getResource(path));
                Stage stage = new Stage();
                Scene scene = new Scene(root, 450, 200);
                scene.getStylesheets().add("style.css");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            userDAO.insertAction("Accesó: Menú PDF4",Time.valueOf(actionTime),id);
        }

        if (event.getSource()==mnuPdf5){ //-----------Bitacora acceso-------
            File file = new File(DEST5);
            file.getParentFile().mkdirs();
            Pdf5DAO pdf5DAO =new Pdf5DAO(MySQL.getConnection());
            try {
                System.out.println(pdf5DAO.fetchAllAccess().toString());
                new Pdf5Access().createPdf(DEST5,pdf5DAO.fetchAllAccess());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("");
            al.setContentText("Archivo generado exitosamente");
            al.show();

            userDAO.insertAction("Generó PDF5",Time.valueOf(actionTime),id);
            int lastAccess=userDAO.getLastAccess();
            userDAO.insertReportCreation(lastAccess,5,Time.valueOf(actionTime));
        }
        if (event.getSource()==mnuPdf6){//------------------Reportes generados-----------------
            File file = new File(DEST6);
            file.getParentFile().mkdirs();
            Pdf6DAO pdf6DAO =new Pdf6DAO(MySQL.getConnection());
            try {
                System.out.println(pdf6DAO.fetchAllReports().toString());
                new Pdf6Reports().createPdf(DEST6,pdf6DAO.fetchAllReports());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("");
            al.setContentText("Archivo generado exitosamente");
            al.show();

            userDAO.insertAction("Generó PDF6",Time.valueOf(actionTime),id);
            int lastAccess=userDAO.getLastAccess();
            userDAO.insertReportCreation(lastAccess,6,Time.valueOf(actionTime));
        }
        if (event.getSource()==mnuPdf7){//------------------Seguimiento de  actividad-----------------
            File file = new File(DEST7);
            file.getParentFile().mkdirs();
            Pdf7DAO pdf7DAO =new Pdf7DAO(MySQL.getConnection());
            try {
                System.out.println(pdf7DAO.fetchAllAcivities().toString());
                new Pdf7Activity().createPdf(DEST7,pdf7DAO.fetchAllAcivities());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert al=new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("");
            al.setContentText("Archivo generado exitosamente");
            al.show();

            userDAO.insertAction("Generó PDF7",Time.valueOf(actionTime),id);
            int lastAccess=userDAO.getLastAccess();
            userDAO.insertReportCreation(lastAccess,7,Time.valueOf(actionTime));
        }
        if (event.getSource()==mnuPdf8){
            String path="/pdf8/pdf8.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú Pdf8",Time.valueOf(actionTime),id);
        }
        if (event.getSource()==mnuUsers){
           String path="/user/user.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú alta Usuarios",Time.valueOf(actionTime),id);
        }
        if (event.getSource()==mnuOffice){
            String path="/official/official.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú alta Oficialía",Time.valueOf(actionTime),id);
        }
        if (event.getSource() == mnuBoss) {
            String path = "/boss/boss.fxml";
            showStage(path);
            userDAO.insertAction("Accesó: menú Jefes",Time.valueOf(actionTime),id);
        }
        if (event.getSource()==btnExit){
            userDAO.desactivateStatus(id);
            java.util.Date endingDate = new java.util.Date();
            endTime=endingDate.getHours()+":"+endingDate.getMinutes()+":"+endingDate.getSeconds();
            System.out.println(Time.valueOf(endTime));
            int lastAccess=userDAO.getLastAccess();
            System.out.println(lastAccess);
            userDAO.updateAccess(lastAccess,Time.valueOf(endTime));
            System.exit(0);
        }
    }

    private void showStage(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void permission(){
        if(rol.equals("EDIT")){
            splitAdmin.setVisible(false);
        }else if(rol.equals("VIST")){
            splitAdmin.setVisible(false);
            splitCatalogs.setVisible(false);
            splitReports.setVisible(false);
            mnuRegister.setVisible(false);
            mnuPdf8.setVisible(false);
        }
    }

}