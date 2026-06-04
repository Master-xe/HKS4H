/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.crones;

import java.io.InputStream;
import java.time.*;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.com.ebcon.db.spManager;
import javax.sql.rowset.CachedRowSet;

/**
 * 2022 OCT 28 
 * @author Angel
 * Realiza de manera automática el proceso de solicitud de CFDI mediante UUID
 */
public class uuidRequests extends TimerTask{
    private String _cron;
    private String _executionType;
    private String _value;
    private String _unit;
    
    private spManager _spManager;
    
    /* Accesores */
    
    public String getCron(){
        return _cron;
    }
    
    public void setCron( String value ){
        _cron = value;
    }
    
    public String getExecutionType(){
        return _executionType;
    }
    
    public void setExecutionType(String value){
        _executionType = value;
    }
    public String getValue(){
        return _value;
    }
    
    public void setValue(String value){
        _value = value;
    }
    public String getUnit(){
        return _unit;
    }
    
    public void setUnit(String value){
        _unit = value;
    }
       
    public void init( String cron, String executionType, String value, String unit){
        _cron = cron;
        _executionType = executionType;
        _value = value;
        _unit = unit;
    }
    
    public void setSpManager(spManager spManager){
        _spManager = spManager;
    }
    
    @Override
    public void run() {
        
        try{
            execProcessByCompany();
        }catch(Exception ex){
            try {
                _spManager.cronesExec_save(0, LocalDateTime.now(), "RUN > UUIDREQUESTS:ERROR", ex.getMessage() );
            } catch (Exception ex1) {
                Logger.getLogger(checker.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.out.println(LocalDateTime.now() + " - " + ex.getMessage());
        }
    }

    /*
    * Ejecuta el proceso por cada compañia registrada y activa
    */
    public void execProcessByCompany() throws Exception{
        int receiverId;
        String RFC;
        InputStream PFX;
        String PFXPwd;
        String requestType;

        CachedRowSet companies = _spManager.companies_list("N", null);

        while(companies.next()){
            // Si ocurre un error lo va a registar y continuaría con el siguiente RFC
            // probar, dejando empty el PFX por ejem
            
            try{
                receiverId = companies.getInt("cid");
                RFC = companies.getString("crfc");
                PFX = companies.getBinaryStream("cfile");
                PFXPwd =   companies.getString("cpswd");
                requestType = companies.getString("reqType");

                execDMR(receiverId, RFC, PFX, PFXPwd, requestType);
            }catch(Exception ex){
                utils _utils = new utils();
                String msg = _utils.getStackTrace(ex);
                
                _spManager.cronesExec_save(0, LocalDateTime.now(), "execProcessByCompany > UUIDREQUESTS:ERROR", msg );
                System.out.println(LocalDateTime.now() + " - " + msg);
            }
        }
    }
    
    /*
    * Ejecuta el proceso de solicitudes 
    */
    private void execDMR(int receiverId, String RFC, InputStream PFX, String PFXPwd, String requestType) throws Exception{
        LocalDateTime now  = LocalDateTime.now();
        
        String origin = "";
        int originId =0;
        int requestId = 0;
        String UUID = "";
        
        System.out.println( LocalDateTime.now() + " - UUIDRequest > execProcessByCompany  " + RFC + " (" + receiverId +  ")" + " >>> " );
        
        DMR _DMR = new DMR( RFC, PFX, PFXPwd);
        _DMR.setReceiver(receiverId);
        _DMR.setSpManager(_spManager);
        
        // registrar la ejecución del cron
        System.out.println( LocalDateTime.now() + " - UUIDRequest > Registrando ejecución del Cron UUIDRequest para " + RFC + "(" + receiverId + ") >>> " );
        
        _spManager.cronesExec_save(receiverId, now, "EXEC > " + _cron, RFC + "|" + requestType );
        
        System.out.println( LocalDateTime.now() + " - UUIDRequest > Obteniendo lista de UUIDs a solicitar >>> " );
        CachedRowSet uuidsWaiting = _spManager.uuidRequest_list(receiverId);
        System.out.println( LocalDateTime.now() + " - UUIDRequest > Registros obtenidos para " + RFC + "(" + receiverId + ") >>> " + uuidsWaiting.size() );
        
        while(uuidsWaiting.next()){
            try{
                origin = uuidsWaiting.getString("origin");
                originId = uuidsWaiting.getInt("originId");
                requestId = uuidsWaiting.getInt("requestId");
                UUID = uuidsWaiting.getString("uuid");
                
                int exist = _spManager.verifyUUIDRequest(receiverId, UUID, requestType);
                
                if (exist == 0) {
                    String[] res = _DMR.getUUIDData(origin, originId, requestId, UUID, requestType);
                    int codStatus = (int)utils.strToNumeric(res[1]);

                    // Si es un status crítico que impide continuar
                    if ( ( codStatus>=300 && codStatus <= 305 ) ){
                        _spManager.cronesExec_save(0, LocalDateTime.now(), "getUUIDData > UUIDREQUESTS:ERROR", String.join("\\|", res) );
                        uuidsWaiting.last();
                    }
                }else{
                    System.out.println( LocalDateTime.now() + " - UUIDRequest > Ya existe una solicitud de información al SAT para el UUID >>> " + UUID );
                }
                
            }catch(Exception ex){
                utils _utils = new utils();
                String msg = _utils.getStackTrace(ex);
                
                _spManager.cronesExec_save(0, LocalDateTime.now(), "execDMR > UUIDREQUESTS:ERROR", msg );
                
                System.out.println(LocalDateTime.now() + " - " + msg);
            }
        }
    }
}
