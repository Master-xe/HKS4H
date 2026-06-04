/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.crones;

import java.io.InputStream;
import mx.com.ebcon.db.spManager;
import java.time.LocalDateTime;
import mx.com.ebcon.dm.base.SATlib;

/**
 * 2022 OCT 29 
 * @author Angel
 * Librería para consumir los servicios del SAT para descarga masiva, se agregan utilerías para el guardado en DB del proceso de descarga 
 */
public class DMR {
    private SATlib _lib;
    private spManager _spManager;
    
    private int _receiverId;
    private String _RFC;
    
    
    private int _Aceptada = 1;  // Hay que realizar las verificaciones
    private int _EnProceso = 2; // Necesarias hasta obtener un estado 3
    private int _Terminada = 3; // Listo para descargar
    private int _Error = 4;
    private int _Rechazada = 5; // Se hicieron más de 3 solicitudes con el mismo rango de fechas
     // El SAT también devuelve este estatus cuando el UUID está cancelado y se solicita CFDI, con Metadata la info si se obtiene correctamente.
    private int _Vencida = 6; // La descarga caducó ¡¡?

    public DMR( String RFC, InputStream PFX, String passPFX)throws Exception{
        _RFC = RFC;
        _lib = new SATlib(RFC, PFX, passPFX);
    }
    public void setReceiver(int receiverId){
        _receiverId = receiverId;
    }
    
    public void setSpManager(spManager spManager){
        _spManager = spManager;
    }
    
    /*
    * Ejecuta el proceso de solicitud, verificación y descarga de CFDIs, siempre cuando los últimos dos puedan llevarse a cabo
    */
    public String[] getUUIDData(String origin, int originId, int requestId, String UUID, String requestType) throws Exception{
        int currId;
        
        LocalDateTime now  = LocalDateTime.now();
        
        System.out.println( now + " - DMR > Realizando solicitud de UUID al SAT:" + UUID + " Tipo Solicitud:" + requestType + " >>> " );
        
        String req = SATlib.getRequest(UUID, requestType);
        String[] res = req.split("\\|");
        
        currId = _spManager.request_save(_receiverId, LocalDateTime.now(), origin, originId, requestId, UUID, requestType, res[0], res[1], res[2]);
        
        if ( !res[0].isEmpty() ){
            verifyAndDownload(0, currId, origin, originId, res[0], "U");
        }
        
        return res;
    }
    
    
    /*
    * Ejecuta el proceso de solicitud, verificación y descarga de CFDIs, siempre cuando los últimos dos puedan llevarse a cabo
    */
    public String[] getCanceledCFDIs(int requestId, LocalDateTime initialDate, LocalDateTime finalDate, String requestType, String origin) throws Exception{        
        System.out.println( LocalDateTime.now() + " - DMR > Realizando solicitud de CCFDI al SAT:" + initialDate + " - " + finalDate + " Tipo Solicitud:" + requestType );
        
        String req = SATlib.getRequest(initialDate, finalDate, requestType);
        String[] res = req.split("\\|"); //String[] res = {"0","1","2"}; // 
        int originId = 0;
        
        requestId = _spManager.request_save(requestId, _receiverId, LocalDateTime.now(), initialDate, finalDate, origin, requestType, res[0], res[1], res[2]);

        if ( !( res[0].isEmpty() || res[0].isBlank() || res[0].compareToIgnoreCase("NULL") == 0 ) ){
            verifyAndDownload(0, requestId, origin, originId, res[0], "C");
        }
        
        return res;
    }
    
    
    /*
    * Ejecuta el proceso de verificación y descarga de CFDIs
    */
    public String[] verifyAndDownload(
            int verifyId
        , int requestId
        , String origin
        , int originId
        , String requestSATId
        , String parent
    )throws Exception{
        LocalDateTime now  = LocalDateTime.now();
        
        System.out.println( LocalDateTime.now() + " - DMR > Realizando solicitud de verificación al SAT:" + requestSATId );
        
        String req = SATlib.verifyRequest(requestSATId);
        String[] res = req.split("\\|"); // {"3", "uweouekn-wrhoiwerjho","event", "message","res[4]", "messa"}; // req.split("\\|");
        
        int status = (int)utils.strToNumeric(res[0]);
        String packagesIds = res[1];
        String event = res[2];
        String cevent = res[5];
        String message = res[3];
        
        int verificationId = _spManager.verifications_save(verifyId, requestId, LocalDateTime.now(), status, packagesIds, event, cevent, message, parent);
        
        if (status == _Terminada){
            String _package = "";
            
            System.out.println( "DMR > Realizando descarga de paquete desde el SAT: " + packagesIds + " >>> " + LocalDateTime.now() );
            _package = SATlib.downloadRequest(packagesIds); //_package = "UEsDBBQAAAAIAJYraVVxbnLq8gAAAEgBAAArAAAAQ0E3Q0U4QzUtQTk2Ny00MTJFLTlBNTEtMzdGRkYyRjg0OTA5XzAxLnR4dF2LQW6DMBBF95FyBy6ANAYCmJ2hIIGAVqQ5gOMa1RVgZMyGxVyqR+jFSluURVfz3/vzb6t6w64X+agWbbDV493IA3bdSSFn+ygeuFcvXGAhxTv/+VZ6+oNMGqt6JbjY1ZVbbPRkNea9FFZnepyNvvPJSswXy+26HCs+CTn8bs4nxhgJKQPXK/zUDbyn1KVhGrtR7AcFhdBnEGOTdyUAUELK2MeGG8Wd0m5ycBr5seeOj8rIDevnuoooEAhJxbBa+eRk3Ax6ceqvz1lux8Ere6URREDa1kcPCHUJcb2LA5B4QeJH/1wQJCTASwyAJRI8n74BUEsBAhQAFAAAAAgAlitpVXFucuryAAAASAEAACsAAAAAAAAAAAAAAAAAAAAAAENBN0NFOEM1LUE5NjctNDEyRS05QTUxLTM3RkZGMkY4NDkwOV8wMS50eHRQSwUGAAAAAAEAAQBZAAAAOwEAAAAA"; //
            
            _spManager.downloads_save(verificationId, origin, originId, LocalDateTime.now(), _package);
        }
        
        return res;
    }
}
