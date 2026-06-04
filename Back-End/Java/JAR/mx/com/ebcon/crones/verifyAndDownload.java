/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.crones;

import java.io.InputStream;
import java.util.TimerTask;
import mx.com.ebcon.db.spManager;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
/**
 * 2022 OCT 26
 * @author Angel
 */
public class verifyAndDownload extends TimerTask{
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
    public void run(){
        
        try{
            execProcessByCompany();
        }catch(Exception ex){
            try {
                _spManager.cronesExec_save(0, LocalDateTime.now(), "RUN > VERIFY_AND_DOWNLOAD:ERROR", ex.getMessage() );
            } catch (Exception ex1) {
                Logger.getLogger(checker.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.out.println(LocalDateTime.now() + " - " + ex.getMessage());
        }
    }
    
    public void execProcessByCompany() throws Exception{
        int receiverId;
        String RFC;
        InputStream PFX;
        String PFXPwd;

        CachedRowSet companies = _spManager.companies_list("N", null);

        while(companies.next()){
            try{
                receiverId = companies.getInt("cid");
                RFC = companies.getString("crfc");
                PFX = companies.getBinaryStream ("cfile");
                PFXPwd = companies.getString("cpswd");

                execDMR(receiverId,  RFC, PFX, PFXPwd);
            }catch(Exception ex){
                _spManager.cronesExec_save(0, LocalDateTime.now(), "execProcessByCompany > VERIFY_AND_DOWNLOAD:ERROR", ex.getMessage() );
                System.out.println(LocalDateTime.now() + " - " + ex.getMessage());
            }
        }
    }
    
    private void execDMR(int receiverId, String RFC, InputStream PFX, String PFXPwd) throws Exception{
        int verificationId;
        int requestId;
        String requestSATId;
        String origin;
        int originId;
        String parent;
        
        int _status = 12; // Obtiene los estatus con valor 1 o 2
        
        System.out.println( LocalDateTime.now() + " - VERIFY_AND_DOWNLOAD > execProcessByCompany: " + RFC + " (" + receiverId + ")" );
        
        DMR _DMR = new DMR(RFC, PFX, PFXPwd);
        _DMR.setSpManager(_spManager);
        
        // registrar la ejecución del cron
        System.out.println( LocalDateTime.now() + " - VERIFY_AND_DOWNLOAD > Registrando ejecución del Cron verifyAndDOwnload para " + RFC + " >>> " );
        _spManager.cronesExec_save(receiverId, LocalDateTime.now(), "EXEC > " + _cron, RFC );
        
        System.out.println( LocalDateTime.now() + " - VERIFY_AND_DOWNLOAD > Obteniendo lista de verificaciones a solicitar >>> " );
        CachedRowSet requestsWaiting = _spManager.verifications_list(receiverId, _status);
        
        while(requestsWaiting.next()){
            try{
                verificationId = requestsWaiting.getInt("verificationId");
                requestId = requestsWaiting.getInt("requestId");
                requestSATId = requestsWaiting.getString("requestSATId");
                origin = requestsWaiting.getString("origin");
                originId = requestsWaiting.getInt("originId");
                parent = requestsWaiting.getString("parent");
                
                String[] res = _DMR.verifyAndDownload(verificationId, requestId, origin, originId, requestSATId, parent);
                
                int codStatus = (int)utils.strToNumeric(res[2]);
                
                // Si es un status crítico que impide continuar
                if ( ( codStatus>=300 && codStatus <= 305 ) ){
                    _spManager.cronesExec_save(0, LocalDateTime.now(), "verifyAndDownload > VERIFY_AND_DOWNLOAD:ERROR", String.join("\\|", res) );
                    requestsWaiting.last();
                }
            }catch(Exception ex){
                _spManager.cronesExec_save(0, LocalDateTime.now(), "execDMR > VERIFY_AND_DOWNLOAD:ERROR", ex.getMessage() );
                System.out.println(LocalDateTime.now() + " - " + ex.getMessage());
            }
        }
    }
}

