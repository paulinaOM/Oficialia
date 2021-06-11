package consFolio;

import documents.Addressee;
import documents.Document;
import documents.Origin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("Duplicates")

public class ConsFolioDAO {
    Connection conn;
    public ConsFolioDAO(Connection conn){
        this.conn= conn;
    }

    public ObservableList<Document> fetchAllDoc(int searchFolio){ //Devuelve los datos de todos los documentos.
        ObservableList<Document> docList= FXCollections.observableArrayList();
        Document doc=null;
        try {
            String query="select * from documento where noFolio="+searchFolio;
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                doc= new Document(rs.getInt("noFolio"),rs.getInt("noDoc"),rs.getString("nomDoc"),rs.getDate("fechaDoc"),
                        rs.getDate("fechaRecep"),rs.getBytes("archivoDoc"),rs.getInt("cveProcedencia"),
                        rs.getInt("cveDestinatario"),rs.getString("cveFormato"),rs.getString("cveTipo"));
                docList.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docList;
    }

    public Document fetchDoc(int noFolio, int noDoc){ //Devuelve todos los datos de un documento buscando por su numFolio
        Document doc = null;
        try {
            String query="select * from documento where noFolio="+noFolio+" and noDoc="+noDoc;
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                doc=new Document(rs.getInt("noFolio"),rs.getInt("noDoc"),rs.getString("nomDoc"),rs.getDate("fechaDoc"),
                        rs.getDate("fechaRecep"),rs.getBytes("archivoDoc"),rs.getInt("cveProcedencia"),
                        rs.getInt("cveDestinatario"),rs.getString("cveFormato"),rs.getString("cveTipo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doc;
    }

    //Obtiene nombre del formato a partir de su código
    public String getNameFormat(String formatCode){
        String nameFormat="";
        try {
            String query="select descripcion from formato where cveFormato='"+formatCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            nameFormat=rs.getString(1);
            System.out.println("ConsFolioDAO nameFormat= "+nameFormat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameFormat;
    }

    public String getNameTownHall(String townHallCode){
        String nameTownHall="";
        try {
            String query="select descripcion from areasAyuntamiento where cveArea='"+townHallCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            nameTownHall=rs.getString(1);
            System.out.println("ConsFolioDAO nameTownhall= "+nameTownHall);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameTownHall;
    }

    public String getInstruction(String instructionCode){
        String instruction="";
        try {
            String query="select descripcion from instruccion where cveInstruccion='"+instructionCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            instruction=rs.getString(1);
            System.out.println("ConsFolioDAO instruction= "+instruction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instruction;
    }

    public String getPriority(String priorityCode){
        String priority="";
        try {
            String query="select descripcion from prioridad where cvePrioridad='"+priorityCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            priority=rs.getString(1);
            System.out.println("ConsFolioDAO priority= "+priority);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priority;
    }

    public String getNameInstitute(String instituteCode){
        String nameInstitute="";
        try {
            String query="select descripcion from institucion where cveInstitucion='"+instituteCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            nameInstitute=rs.getString(1);
            System.out.println("ConsFolioDAO nameInstitute= "+nameInstitute);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameInstitute;
    }

    //Obtiene nombre del tipo documento a partir de su código
    public String getNameType(String typeCode){
        String typeDescript="";
        try {
            String query="select descripcion from tipoDoc where cveTipo='"+typeCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            typeDescript=rs.getString(1);
            System.out.println("ConsFolioDAO nameType= "+typeDescript);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeDescript;
    }

    public Origin getOrigin(int originCode){//Obtiene un objeto de tipo Destinatario
        Origin objOrigin=null;
        try {
            String query="select * from procedencia where cveProcedencia="+originCode;
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                objOrigin=new Origin(rs.getString("remitente"),rs.getString("asunto"),rs.getString("puesto"),
                        rs.getString("destinatario"),rs.getString("observaciones"),rs.getString("cveInstitucion"));
            }
            System.out.println("ConsFolioDAO getOrigin= "+objOrigin.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objOrigin;
    }

    public Addressee getAddressee(int addrCode){//Obtiene un objeto de tipo Procedencia
        Addressee objAddr=null;
        try {
            String query="select * from destinatario where cveDestinatario="+addrCode;
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                objAddr=new Addressee(rs.getString("nombreRecibe"),rs.getDate("fechaEntrega"),rs.getDate("fechaLimite"),
                        rs.getString("status"),rs.getString("cveArea"),rs.getString("cveInstruccion"),rs.getString("cvePrioridad"));
            }
            System.out.println("ConsFolioDAO getAddressee= "+addrCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objAddr;
    }

}
