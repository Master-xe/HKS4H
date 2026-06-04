/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.crones;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import mx.com.ebcon.db.spManager;




/**
 * Cron para consultar ante Konesh información de SAP cada ciertos periodos de tiempo
 * @author Angel
 */
public class koneshRequests extends TimerTask{
    private String _cron;
    private String _executionType;
    private String _value;
    private String _unit;
    private String _endPointWS;
    
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
       
    public void init( String cron, String executionType, String value, String unit) throws Exception{
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
                _spManager.cronesExec_save(0, LocalDateTime.now(), "RUN > KONESHREQUESTS:ERROR", ex.getMessage() );
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
        _endPointWS = _spManager.getKoneshEndPoint();
        
        CachedRowSet companies = _spManager.companies_list("N", null);

        while(companies.next()){
            // Si ocurre un error, va a registrar y continuara con el siguiente RFC
            // probar, dejando empty el PFX por ejem
            
            try{
                receiverId = companies.getInt("cid");

                execKONESH(receiverId);
            }catch(Exception ex){
                utils _utils = new utils();
                String msg = _utils.getStackTrace(ex);
                
                _spManager.cronesExec_save(0, LocalDateTime.now(), "execProcessByCompany > KONESHREQUESTS:ERROR", msg );
                System.out.println(LocalDateTime.now() + " - " + msg);
            }
        }
    }
    
    
    /*
    * Ejecuta el proceso de solicitudes 
    */
    private void execKONESH(int receiverId) throws Exception{
        LocalDateTime now  = LocalDateTime.now();
        
        System.out.println( LocalDateTime.now() + " - ::: INICIANDO PROCESO DE SOLICITUD A KONESH ::: companyId (" + receiverId + ") >>> " );
        
        KONESH _KONESH = new KONESH( );
        _KONESH.setReceiver(receiverId);
        _KONESH.setSpManager(_spManager);
        _KONESH.setEndPointWS(_endPointWS);
        
        // registrar la ejecución del cron
        _spManager.cronesExec_save(receiverId, now, "EXEC > " + _cron, receiverId + "|" + "KONESH" );
        
        CachedRowSet uuidsWaiting = _spManager.koneshRequest_list(receiverId);
        
        System.out.println( LocalDateTime.now() + " - execKONESH > Registros obtenidos para companyId (" + receiverId + ") >>> " + uuidsWaiting.size() );
        
        try{
            if (  uuidsWaiting.size() > 0 ) {
                _KONESH.getUUIDData(uuidsWaiting );
            }else{
                System.out.println( LocalDateTime.now() + " - execKONESH > No se encontraron UUIDs a procesar para la compañia >>> companyId (" + receiverId + ") >>> " );
                _spManager.cronesExec_save(0, LocalDateTime.now(), "KONESHREQUESTS:EXEC",  "No se encontraron UUIDs a procesar para la compañia ::: companyId (" + receiverId + ")" );
            }
        }catch(Exception ex){         
            utils _utils = new utils();
            String msg = _utils.getStackTrace(ex);

            _spManager.cronesExec_save(0, LocalDateTime.now(), "execDMR > KONESHREQUESTS:ERROR", msg );

            System.out.println(LocalDateTime.now() + " - " + msg);
        }
    }
}
