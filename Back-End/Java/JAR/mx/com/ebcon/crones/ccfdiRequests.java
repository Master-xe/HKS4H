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
 * 11 NOV 2022
 * @author Angel Hernández
 * Cron para la solicitud por periodos de tiempo de CFDIs cancelados
 */
public class ccfdiRequests extends TimerTask {
    private String _cron;
    private String _executionType;
    private String _value;
    private String _unit;
    private int _minusDays;
    
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
                _spManager.cronesExec_save(0, LocalDateTime.now(), "RUN > CCFDIREQUESTS:ERROR", ex.getMessage() );
            } catch (Exception ex1) {
                Logger.getLogger(checker.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.out.println(ex.getMessage());
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
        
        //System.out.println( "CCFDIREQUESTS > execProcessByCompany >>> OBTENIENDO LISTA DE COMPAÑIAS "  );

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
                
                _spManager.cronesExec_save(0, LocalDateTime.now(), "CCFDIREQUESTS > execProcessByCompany:ERROR", msg );
                System.out.println(msg);
            }
        }
    }
    
    /*
    * Ejecuta el proceso de solicitudes 
    */
    private void execDMR(int receiverId, String RFC, InputStream PFX, String PFXPwd, String requestType) throws Exception{
        String origin;
        int requestId;
        
        LocalDateTime now  = LocalDateTime.now();
        
        // Obtener fecha inicial/final del día
        LocalDateTime initialDate = utils.getInitialDate(now);
        LocalDateTime finalDate = utils.getFinalDate(initialDate);
        Object id;
        Object fd;
        
        System.out.println( LocalDateTime.now() + " - CCFDIREQUESTS > execDMR: " + RFC + " (" + receiverId + ")");
        
        // La documentación del SAT dice textualmente
        // :::::::::::::::::::::::::::::::::::::::
        // En el caso para la descarga de XML, solo incluirán los CFDI vigentes. Por lo que, el servicio no descargará XML cancelados.
        // Es decir, cuando requestType = CFDI, solo se descargan CFDIs vigentes, y EstadoComprobante no se debe enviar

        // En el requerimiento actual se solicitan solo los cancelados, 
        // para lograrlo se usa requestType = "METADATA" y EstadoComprobante = 0, 
        // razón por la que se deja duro el parámetro requestType = "METADATA" y EstadoComprobante = 0
        requestType = "Metadata";// ccfdiRequestsWaiting.getString("requestType");
        
        DMR _DMR = new DMR( RFC, PFX, PFXPwd);
        _DMR.setReceiver(receiverId);
        _DMR.setSpManager(_spManager);
        
        // registrar la ejecución del cron
        //System.out.println( "Registrando ejecución del Cron CCFDIRequest para " + RFC + " >>> " + now );
        _spManager.cronesExec_save(receiverId, now, "EXEC > " + _cron, RFC + "|" + requestType );
        
        System.out.println(LocalDateTime.now() + " - CCFDIREQUESTS > execDMR > Obteniendo lista de periodos a solicitar >>> " );
        // ***** Ejecutar las solicitudes en reproceso
        CachedRowSet ccfdiRequestsWaiting = _spManager.ccfdiRequests_list(receiverId, initialDate, finalDate, requestType );
        
        System.out.println( LocalDateTime.now() + " - CCFDIREQUESTS > execDMR > Registros obtenidos: " + ccfdiRequestsWaiting.size() );

        while(ccfdiRequestsWaiting.next()){
            try{
                requestId = ccfdiRequestsWaiting.getInt("requestId");
                
                id = ccfdiRequestsWaiting.getObject("initialDate");
                fd = ccfdiRequestsWaiting.getObject("finalDate");
                origin = ccfdiRequestsWaiting.getString("origin");
                
                initialDate = (LocalDateTime)id;
                finalDate = (LocalDateTime)fd;
               
                String[] res = _DMR.getCanceledCFDIs (requestId, initialDate, finalDate, requestType, origin);
                int codStatus = (int)utils.strToNumeric(res[1]);

                // Si es un status crítico que impide continuar
                if ( ( codStatus>=300 && codStatus <= 305 ) ){
                    _spManager.cronesExec_save(0, LocalDateTime.now(), "getCanceledCFDIs > CCFDIREQUESTS:ERROR", String.join("\\|", res) );
                    ccfdiRequestsWaiting.last();
                }
            }catch(Exception ex){
                utils _utils = new utils();
                String msg = _utils.getStackTrace(ex);
                
                _spManager.cronesExec_save(0, LocalDateTime.now(), "execDMR > CCFDIREQUESTS:ERROR", msg );
                System.out.println(msg);
            }
        }
    }
}
