/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.crones;



import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.com.ebcon.db.spManager;
import javax.sql.rowset.CachedRowSet;


/**
 * 2022 OCT 24 
 * @author Angel
 * Es el Cron principal que inicia/actualiza los crones secundarios: 
 * Cron Konesh: Responsable de la descarga de info. desde Konesh
 * Cron CCFDIRequest: Responsable de la solicitud al SAT de CFDIs cancelados  (Solicitud por período de tiempo )
 * Cron UUIDRequest: Responsable de la solicitud al SAT de CFDIs (Solicitud por UUID)
 * Cron verifyAndDownload: Responsable de realizar ante el SAT la verificación de la solicitud y descargar el paquete en caso de estar disponible
 */
public class checker extends TimerTask {
    private spManager _spManager;
    
    private Timer _uuidRequestTimer = new Timer(); // cron de Solicitudes por UUID
    private Timer _ccfdiRequestTimer = new Timer(); // Cron de CFDIs cancelados
    private Timer _verifyAndDownloadTimer = new Timer(); // Cron de verificacion y descarga
    private Timer _koneshRequestTimer = new Timer(); // Cron de Konesh
    
    private uuidRequests _uuidRequestsTask;
    private ccfdiRequests _ccfdiRequestsTask;
    private verifyAndDownload _verifyAndDownloadTask;
    private koneshRequests _koneshRequestsTask;
    
    private String UUID_REQUEST_NAME = "UUID_REQUESTS";
    private String CCFDI_REQUEST_NAME = "CCFDI_REQUESTS";
    private String VERIFY_NAME = "VERIFY_AND_DOWNLOAD";
    private String KONESH_REQUEST_NAME = "KONESH_REQUEST";
    
    
    /*
    * Prepara e inicia los crones 
    */
    public void init(String strCnn, String user, String pwd) throws Exception{
        _spManager = new spManager(strCnn, user, pwd);
        
        String cron = "";
        String executionType = "";
        String value = "";
        String unit = "";
        
        _spManager.cronesExec_save(0, LocalDateTime.now(), "INIT > CHECKER", "");

        CachedRowSet crones = _spManager.crones_list();

        while ( crones.next()){
            try {
                cron = crones.getString("cron");
                executionType = crones.getString("executionType");
                value= crones.getString("value");
                //unit = "";//crones.getString("unit");

                //Levantar los crones
                if ( cron.equals( UUID_REQUEST_NAME) ){
                    updateCronUuidRequests(cron, executionType, value, unit);
                } else if ( cron.equals( CCFDI_REQUEST_NAME) ){
                    updateCronCcfdiRequests(cron, executionType, value, unit);
                } else if ( cron.equals( KONESH_REQUEST_NAME) ){
                    updateCronKoneshRequests(cron, executionType, value, unit);
                } else if (cron.equals(VERIFY_NAME)){
                    updateCronVerify(cron, executionType, value, unit);
                }
            }catch(Exception ex){
                _spManager.cronesExec_save(0, LocalDateTime.now(), "INIT > CHECKER:ERROR", ex.getMessage() );
                ex.printStackTrace();
            }
        }
    }
    
    
    /*
    * Verifica si hubo cambio en la configuración de algún cron y en caso de que sí, actualiza el cron correspondiente.
    */
    @Override
    public void run(){
        String cron;
        String executionType;
        String value;
        String unit = "";
        
        try{
            // Registrar la ejecución
            _spManager.cronesExec_save(0, LocalDateTime.now(), "EXEC > CHECKER", "");
            System.out.println(LocalDateTime.now() + " - CHECKER > Verificando cambios de configuración en DB ");
            
            CachedRowSet crones = _spManager.crones_list();

            while ( crones.next()){
                
                try{
                    cron = crones.getString("cron");
                    executionType = crones.getString("executionType");
                    value= crones.getString("value");
                    //unit = "";//crones.getString("unit");

                    // Verificar si hubo cambio en la configuración de algún cron
                    if ( cron.equals( UUID_REQUEST_NAME) ){
                        if ( !_uuidRequestsTask.getExecutionType().equals(executionType) 
                            || !_uuidRequestsTask.getValue().equals(value) 
                            //|| !_uuidRequestsTask.getUnit().equals(unit) 
                            ){

                                System.out.println(LocalDateTime.now() + " - Actualizando cron " + _uuidRequestsTask.getCron());
                                updateCronUuidRequests(cron, executionType, value, unit);
                        }
                    } else if ( cron.equals( CCFDI_REQUEST_NAME) ){
                        if ( !_ccfdiRequestsTask.getExecutionType().equals(executionType) 
                            || !_ccfdiRequestsTask.getValue().equals(value) 
                            //|| !_ccfdiRequestsTask.getUnit().equals(unit) 
                            ){

                                System.out.println(LocalDateTime.now() + " - Actualizando cron " + _ccfdiRequestsTask.getCron());
                                updateCronCcfdiRequests(cron, executionType, value, unit);
                        }
                    } else if ( cron.equals( KONESH_REQUEST_NAME) ){
                        if ( !_koneshRequestsTask.getExecutionType().equals(executionType) 
                            || !_koneshRequestsTask.getValue().equals(value) 
                            //|| !_koneshRequestsTask.getUnit().equals(unit) 
                            ){
                                System.out.println(LocalDateTime.now() + " - Actualizando cron " + _koneshRequestsTask.getCron());
                                updateCronKoneshRequests(cron, executionType, value, unit);
                        }
                    } else if (cron.equals(VERIFY_NAME)){
                        if ( !_verifyAndDownloadTask.getExecutionType().equals(executionType) 
                            || !_verifyAndDownloadTask.getValue().equals(value) 
                            //|| !_verifyAndDownloadTask.getUnit().equals(unit) 
                            ){

                                System.out.println(LocalDateTime.now() + " - Actualizando cron " + _verifyAndDownloadTask.getCron());
                                updateCronVerify(cron, executionType, value, unit);
                        }
                    }
                }catch(Exception ex){
                    _spManager.cronesExec_save(0, LocalDateTime.now(), "RUN > CHECKER:ERROR", ex.getMessage() );
                    System.out.println(ex.getMessage());
                }
            }
            
            System.out.println(LocalDateTime.now() + " - ===== CHECKER finalizado ===== ");
        }catch(Exception ex){
            try {
                _spManager.cronesExec_save(0, LocalDateTime.now(), "RUN > CHECKER:ERROR", ex.getMessage() );
            } catch (Exception ex1) {
                Logger.getLogger(checker.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.out.println(ex.getMessage());
        }
    }
    
   
    /*
    * Actualiza el cron de solicitudes
    */
    private void updateCronUuidRequests( String cron, String executionType, String value, String unit ) throws Exception{
        _uuidRequestTimer.cancel();
        _uuidRequestTimer.purge();
        
        _uuidRequestTimer = new Timer();
        _uuidRequestsTask = new uuidRequests();

        _uuidRequestsTask.init(cron, executionType, value, unit);
        _uuidRequestsTask.setSpManager(_spManager);
        
        int frecuency = 0;
        
        String[] params = new String[3];
        
        params[0] = executionType;
        params[1] = value;
        params[2] = unit;
        
        if (executionType.equals("H")){
            // Configuración por horario
            String[] schedule = utils.getSchedule(value).split(":");
            
            // PRODUCCIÓN --->  
            frecuency = utils.getFrecuency( value );
            
            Date firstExec = utils.getSchedule( (int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1]));
            
            _uuidRequestTimer.scheduleAtFixedRate( _uuidRequestsTask ,firstExec, frecuency);
            System.out.println( utils.getSchedule((int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1])) );
        } else if (executionType.equals("F")){
            // Configuración por frecuencia
            int delay = 0;
            frecuency = utils.getFrecuency( utils.strToNumeric(value), 0);
            _uuidRequestTimer.scheduleAtFixedRate( _uuidRequestsTask ,delay, frecuency);
        }
        
        // Registrar evento
        _spManager.cronesExec_save(0, LocalDateTime.now(), "UPD > " + _uuidRequestsTask.getCron(), "PARAMS > " + String.join("|", params) );
    }
    
    
    /*
    * Actualiza el cron de solicitudes
    */
    private void updateCronCcfdiRequests( String cron, String executionType, String value, String unit ) throws Exception{
        _ccfdiRequestTimer.cancel();
        _ccfdiRequestTimer.purge();
        
        _ccfdiRequestTimer = new Timer();
        _ccfdiRequestsTask = new ccfdiRequests();

        _ccfdiRequestsTask.init(cron, executionType, value, unit);
        _ccfdiRequestsTask.setSpManager(_spManager);
        
        int frecuency = 0;
        
        String[] params = new String[3];
        
        params[0] = executionType;
        params[1] = value;
        params[2] = unit;
        
        if (executionType.equals("H")){
            // Configuración por horario
            String[] schedule = utils.getSchedule(value).split(":");
            
            // PRODUCCIÓN --->  
            frecuency = utils.getFrecuency( value );
            
            Date firstExec = utils.getSchedule( (int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1]));
            
            _ccfdiRequestTimer.scheduleAtFixedRate( _ccfdiRequestsTask ,firstExec, frecuency);
            System.out.println( utils.getSchedule((int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1])) );
        } else if (executionType.equals("F")){
            // Configuración por frecuencia
            int delay = 0;
            frecuency = utils.getFrecuency( utils.strToNumeric(value), 0);
            _ccfdiRequestTimer.scheduleAtFixedRate( _ccfdiRequestsTask ,delay, frecuency);
        }
        
        // Registrar evento
        _spManager.cronesExec_save(0, LocalDateTime.now(), "UPD > " + _ccfdiRequestsTask.getCron(), "PARAMS > " + String.join("|", params) );
    }
    
    
    /*
    * Actualiza el cron de solicitudes de konesh
    */
    private void updateCronKoneshRequests( String cron, String executionType, String value, String unit ) throws Exception{
        _koneshRequestTimer.cancel();
        _koneshRequestTimer.purge();
        
        _koneshRequestTimer = new Timer();
        _koneshRequestsTask = new koneshRequests();

        _koneshRequestsTask.init(cron, executionType, value, unit);
        _koneshRequestsTask.setSpManager(_spManager);
        
        int frecuency = 0;
        
        String[] params = new String[3];
        
        params[0] = executionType;
        params[1] = value;
        params[2] = unit;
        
        if (executionType.equals("H")){
            // Configuración por horario
            String[] schedule = utils.getSchedule(value).split(":");
            
            // PRODUCCIÓN --->  
            frecuency = utils.getFrecuency( value );
            
            Date firstExec = utils.getSchedule( (int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1]));
            
            _koneshRequestTimer.scheduleAtFixedRate( _koneshRequestsTask ,firstExec, frecuency);
            System.out.println( utils.getSchedule((int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1])) );
        } else if (executionType.equals("F")){
            // Configuración por frecuencia
            int delay = 0;
            frecuency = utils.getFrecuency( utils.strToNumeric(value), 0);
            _koneshRequestTimer.scheduleAtFixedRate( _koneshRequestsTask ,delay, frecuency);
        }
        
        // Registrar evento
        _spManager.cronesExec_save(0, LocalDateTime.now(), "UPD > " + _koneshRequestsTask.getCron(), "PARAMS > " + String.join("|", params) );
    }
    
    
    /*
    * Actualiza el cron de verificación y descarga
    */
    private void updateCronVerify(String cron, String executionType, String value, String unit ) throws Exception{
        _verifyAndDownloadTimer.cancel();
        _verifyAndDownloadTimer.purge();
        
        _verifyAndDownloadTimer = new Timer();
        _verifyAndDownloadTask = new verifyAndDownload();

        _verifyAndDownloadTask.init(cron, executionType, value, unit);
        _verifyAndDownloadTask.setSpManager(_spManager);
        
        int frecuency = 0;
        
        String[] params = new String[3];
        
        params[0] = executionType;
        params[1] = value;
        params[2] = unit;

        if (executionType.equals("H")){
            // Configuración por horario
            String[] schedule = utils.getSchedule(value).split(":");
            frecuency = utils.getFrecuency( value );
            
            Date firstExec = utils.getSchedule( (int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1]));
            
            _verifyAndDownloadTimer.scheduleAtFixedRate( _verifyAndDownloadTask ,firstExec, frecuency);
            
            System.out.println( utils.getSchedule((int)utils.strToNumeric(schedule[0]), (int)utils.strToNumeric(schedule[1])) );
        } else if (executionType.equals("F")){
            // Configuración por frecuencia
            int delay = 0;
            frecuency = utils.getFrecuency( utils.strToNumeric(value), 0);
            _verifyAndDownloadTimer.scheduleAtFixedRate( _verifyAndDownloadTask ,delay, frecuency);
        }
        
        // Registrar evento
        _spManager.cronesExec_save(0, LocalDateTime.now(), "UPD > " + _verifyAndDownloadTask.getCron(), "PARAMS > " + String.join("|", params) );
    }
}
