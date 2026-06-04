/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 * 2022 oct 26
 * @author Angel Hernández
 * Funciones DAO para enviar y recibir datos de la DB
 */
public class spManager {
    private cnnManager _cnnMan;
    
    public spManager(String strCnn, String user, String pwd){
        _cnnMan= new cnnManager(strCnn,user,pwd);
    }
    
    public CachedRowSet crones_list( ) throws Exception{
        Connection cnn = _cnnMan.getConnection();
        CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
        
        System.out.println(LocalDateTime.now() + " - CRONES_LIST > Preparando ejecución de SP crones_list" );
        
        CallableStatement cmd = cnn.prepareCall("{call crones_list()}");
        
        boolean exec = cmd.execute();
        if (exec)
        {
            ResultSet rs = cmd.getResultSet();

            result.populate(rs);
            
            rs.close();
        }
        cnn.close();
        return result;
    }
    
    
    public CachedRowSet companies_list( String lck, String RFC ) throws Exception{
        Connection cnn = _cnnMan.getConnection();
        CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
        
        System.out.println( LocalDateTime.now() + " - COMPANIES_LIST > Preparando ejecución de SP companies_view >>> Empresa:" + RFC );
        
        CallableStatement cmd = cnn.prepareCall("{call companies_view(?,?)}");
        cmd.setString("lck", lck);
        cmd.setString("rfc", RFC);
        
        boolean exec = cmd.execute();
        if (exec)
        {
            ResultSet rs = cmd.getResultSet();

            result.populate(rs);
            
            rs.close();
        }
        cnn.close();
        
        return result;
    }
    
    
    public CachedRowSet koneshRequest_list( int _receiverId ) throws Exception{
        Connection cnn = _cnnMan.getConnection();
        CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
        
        System.out.println( LocalDateTime.now() + " - KONESHREQUESTS_LIST > Preparando ejecución de SP koneshRequests_list >>> Id empresa:" + _receiverId );
        
        CallableStatement cmd = cnn.prepareCall("{call koneshRequests_list(?)}");
        cmd.setInt("_receiverId", _receiverId);
        
        boolean exec = cmd.execute();
        if (exec)
        {
            ResultSet rs = cmd.getResultSet();

            result.populate(rs);
            
            rs.close();
        }
        cnn.close();
        
        return result;
    }
    
    
    public CachedRowSet uuidRequest_list( int _receiverId ) throws Exception{
        Connection cnn = _cnnMan.getConnection();
        CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
        
        System.out.println( LocalDateTime.now() + " - UUIDREQUEST_LIST > Preparando ejecución de SP uuidRequests_list >>> Id empresa:" + _receiverId );
        
        CallableStatement cmd = cnn.prepareCall("{call uuidRequests_list(?)}");
        cmd.setInt("_receiverId", _receiverId);
        
        boolean exec = cmd.execute();
        if (exec)
        {
            ResultSet rs = cmd.getResultSet();

            result.populate(rs);
            
            rs.close();
        }
        cnn.close();
        
        return result;
    }
    
    
    public CachedRowSet ccfdiRequests_list( int receiverId
            , LocalDateTime initialDate
            , LocalDateTime finalDate
            , String requestType
    ) throws Exception{
        Connection cnn = _cnnMan.getConnection();
        CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
        
        System.out.println( LocalDateTime.now() + " - CCFDIREQUESTS_LIST > Preparando ejecución de SP ccfdiRequests_list >>> Id empresa:" + receiverId + " Período de solicitud:" + initialDate + "-" + finalDate );
        
        // Se crea artificialmente la solicitud actual para al final iterar un solo resultset
        CallableStatement cmd = cnn.prepareCall("{call ccfdiRequests_list(?,?,?,?)}");
        cmd.setInt("_receiverId", receiverId);
        cmd.setObject("_initialDate", initialDate);
        cmd.setObject("_finalDate", finalDate);
        cmd.setString("_requestType", requestType);
        
        boolean exec = cmd.execute();
        if (exec)
        {
            ResultSet rs = cmd.getResultSet();
            
            result.populate(rs);
            
            rs.close();
        }
        cnn.close();
        
        return result;
    }
    
    
    public CachedRowSet verifications_list( int _receiverId, int _status ) throws Exception{
        Connection cnn = _cnnMan.getConnection();
        CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
        
        System.out.println( LocalDateTime.now() + " - VERIFICAFIONS_LIST > Preparando ejecución de SP verificafions_list >>> Id empresa:" + _receiverId );
        
        CallableStatement cmd = cnn.prepareCall("{call verificafions_list(?, ?)}");
        cmd.setInt("_receiverId", _receiverId);
        cmd.setInt("_status", _status);
        
        boolean exec = cmd.execute();
        if (exec)
        {
            ResultSet rs = cmd.getResultSet();

            result.populate(rs);
            
            rs.close();
        }
        cnn.close();
        
        return result;
    }
    

    public long cronesExec_save(   
        int receiverId
        , LocalDateTime execDate
        , String event
        , String info
    )throws Exception{
        Connection cnn = _cnnMan.getConnection();
        
                    
        System.out.println( LocalDateTime.now() + " - CRONESEXEC_SAVE > Preparando ejecución de SP cronesExec_save >>> Id empresa:" + receiverId );
        
        CallableStatement cmd = cnn.prepareCall("{call cronesExec_save(?,?,?,?)}");
        cmd.setInt("_receiverId", receiverId);
        cmd.setObject("_execDate", execDate);
        cmd.setString("_event", event);
        cmd.setString("_info", info);
        
        long id=-1;
        boolean result = cmd.execute();
        
        if (result)
        {
            ResultSet rs = cmd.getResultSet();
            
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(rs);
            rs.close();
     
            while ( cached.next()){
                id = cached.getLong("id");
            }
        }
        
        cnn.close();
        
        System.out.println( LocalDateTime.now() + " - CRONESEXEC_SAVE > cronesExec_save >>> RECORD_ID: " + id );
        
        return id;
    }
    
    
    public int request_save(   
          int receiverId
        , LocalDateTime execDate
        , String origin
        , int originId  
        , int requestId  
        
        , String UUID
        , String requestType
        , String requestSATid
        , String event 
        , String message
    )throws Exception{
        Connection cnn = _cnnMan.getConnection();
        
        
        System.out.println( execDate + " - UUIDREQUESTS > Preparando ejecución de SP uuidRequests_save >>> Id empresa:" + receiverId + " requestSATid:" + requestSATid );
        
        CallableStatement cmd = cnn.prepareCall("{call uuidRequests_save(?,?,?,?,?,?,?,?,?,?)}");
        
        cmd.setInt("_receiverId", receiverId);
        cmd.setObject("_execDate", execDate);
        cmd.setString("_origin", origin);
        cmd.setInt("_originId", originId);       
        cmd.setInt("_requestId", requestId);      
        cmd.setString("_UUID", UUID);
        
        cmd.setString("_requestType", requestType);
        cmd.setString("_requestSATid", requestSATid);
        cmd.setString("_event", event);
        cmd.setString("_message", message);
        
        int id=0;
        boolean result = cmd.execute();
        
        if (result)
        {
            ResultSet rs = cmd.getResultSet();
            
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(rs);
            rs.close();
            
            while ( cached.next()){
                id = cached.getInt(1);
            }
        }
        
        cnn.close();
        
        System.out.println( LocalDateTime.now() + " - UUIDREQUESTS > uuidRequests_save >>> RECORD_ID: " + id );
        
        return id;
    }
    
    
    public int request_save(   
            int requestId
        , int receiverId
        , LocalDateTime execDate
        , LocalDateTime initialDate
        , LocalDateTime finalDate
        , String origin
        , String requestType
        , String requestSATid

        , String event 
        , String message
    )throws Exception{
        Connection cnn = _cnnMan.getConnection();
        
        System.out.println( LocalDateTime.now() + " - CCFDIREQUESTS > Preparando ejecución de SP ccfdiRequests_save >>> Id empresa:" + receiverId + " requestSATid:" + requestSATid );
        
        CallableStatement cmd = cnn.prepareCall("{call ccfdiRequests_save(?,?,?,?,?,?,?,?,?,?)}");
        cmd.setInt("_requestId", requestId);
        cmd.setInt("_receiverId", receiverId);
        cmd.setObject("_execDate", execDate);
        cmd.setObject("_initialDate", initialDate);
        cmd.setObject("_finalDate", finalDate);
        cmd.setString("_origin", origin);
        cmd.setString("_requestType", requestType);
        cmd.setString("_requestSATid", requestSATid);
        cmd.setString("_event", event);
        cmd.setString("_message", message);
        
        int id=0;
        boolean result = cmd.execute();
        
        if (result)
        {
            ResultSet rs = cmd.getResultSet();
            
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(rs);
            rs.close();
            
            while ( cached.next()){
                id = cached.getInt(1);
            }
        }
        
        cnn.close();
        
        System.out.println( LocalDateTime.now() +  " - CCFDIREQUESTS > ccfdiRequests_save >>> RECORD_ID: " + id );
        
        return id;
    }
    
    
    public int verifications_save(   
            int verificationId
        , int requestId
        //, String origin
        , LocalDateTime execDate
        , int status
        , String packageId
        , String event
        , String cevent
        , String message
        , String parent

    )throws Exception{
        Connection cnn = _cnnMan.getConnection();
        
        System.out.println( LocalDateTime.now() + " - VERIFICATIONS > Preparando ejecución de SP verifications_save >>> Id verificacion:" + verificationId + " packageId:" + packageId );
        
        CallableStatement cmd = cnn.prepareCall("{call verifications_save(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        cmd.setInt("_verificationId",verificationId);
        cmd.setInt("_requestId",requestId);
        cmd.setObject("_execDate", execDate);
        cmd.setInt("_status", status);
        cmd.setString("_packageId", packageId);
        cmd.setString("_event", event);
        cmd.setString("_cevent", cevent);
        cmd.setString("_message", message);
        cmd.setString("_parent", parent);
        
        int id=0;
        boolean result = cmd.execute();
        
        if (result)
        {
            ResultSet rs = cmd.getResultSet();
            
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(rs);
            rs.close();
            
            while ( cached.next()){
                id = cached.getInt(1);
            }
        }
        
        cnn.close();
        
        System.out.println( LocalDateTime.now() + " - VERIFICACION > verifications_save >>> RECORD_ID: " + id );
        
        return id;
    }
    
    
    public int downloads_save(   
        int verificationId
        , String origin
        , int originId
        , LocalDateTime execDate
        , String _package

    )throws Exception{
        Connection cnn = _cnnMan.getConnection();
        
        System.out.println( LocalDateTime.now() + " - DOWNLOADS > Preparando ejecución de SP downloads_save >>> Id verificacion:" + verificationId + " package" + _package);
        
        CallableStatement cmd = cnn.prepareCall("{call downloads_save(?, ?, ?, ?, ?)}");
        cmd.setInt("_verificationId",verificationId);
        cmd.setString("_origin", origin);
        cmd.setInt("_originId", originId);
        cmd.setObject("_execDate", execDate);
        cmd.setString("_package", _package);
        
        int id=0;
        boolean result = cmd.execute();
        
        if (result)
        {
            ResultSet rs = cmd.getResultSet();
            
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(rs);
            rs.close();
            
            while ( cached.next()){
                id = cached.getInt("id");
            }
        }
        
        cnn.close();
        
        System.out.println( LocalDateTime.now() + " - DOWNLOADS > downloads_save >>> RECORD_ID: " + id );
        
        return id;
    }
    
    
    public long koneshRequest_save(
         int receiverId
        , LocalDateTime execDate
            
        , String UUID
        , String recrfc
        , String emirfc
        , String procType
        , String orderNumber
            
        , String finalResult
        , String status
        , String message
        , String currency
        , String total
           
        , String docType
        , String serie 
        , String folio
        , String originId
        , String dataId
            
        , String docDate
    )throws Exception{
        Connection cnn = _cnnMan.getConnection();
        long id = 0;
        
        System.out.println( LocalDateTime.now() + " - KONESHREQUEST > Preparando ejecución de SP koneshRequests_save >>> Id empresa:" + receiverId + " UUIDData:" + UUID);
        
        CallableStatement cmd = cnn.prepareCall("{call koneshrequests_save(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        
        cmd.setInt("_receiverId", receiverId);
        cmd.setObject("_execDate", execDate);
        
        cmd.setString("_UUID", UUID);
        cmd.setString("_recrfc", recrfc);
        cmd.setString("_emirfc", emirfc);
        cmd.setString("_procType", procType);
        cmd.setString("_orderNumber", orderNumber);
        
        cmd.setString("_finalResult", finalResult);
        cmd.setString("_status", status);
        cmd.setString("_message", message);
        cmd.setString("_currency", currency);
        cmd.setString("_total", total);
        
        cmd.setString("_docType", docType);
        cmd.setString("_serie", serie);
        cmd.setString("_folio", folio);
        cmd.setString("_originId", originId);
        cmd.setString("_dataId", dataId);
        
        cmd.setString("_docDate", docDate);
        
        boolean result = cmd.execute();
        
        if (result)
        {
            ResultSet rs = cmd.getResultSet();
            
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(rs);
            rs.close();
            
            while ( cached.next()){
                id = cached.getLong("id");
            }
        }
        
        cnn.close();
        
        System.out.println( LocalDateTime.now() + " - KONESHREQUEST > koneshRequests_save >>> RECORD_ID: " + id );
        
        return id;
    }
    
    
    public int verifyUUIDRequest(int receiverId, String UUID, String requestType)throws Exception{
        Connection cnn = _cnnMan.getConnection();
        
        System.out.println( LocalDateTime.now() + " - VERIFYUUIDREQUEST > Verificando UUID para evitar doble solicitud al SAT >>> receiverId: (" +  receiverId + ")   UUID:" + UUID + " requestType:" + requestType );
        
        CallableStatement cmd = cnn.prepareCall("{call verifyUUIDRequest(?, ?, ?)}");
        
        cmd.setInt("_receiverId", receiverId);
        cmd.setString("_UUID", UUID);
        cmd.setString("_requestType", requestType);
        
        int exist = 0;
        boolean result = cmd.execute();
        
        if (result)
        {
            ResultSet rs = cmd.getResultSet();
            
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(rs);
            rs.close();
            
            while ( cached.next()){
                exist = (cached.getInt(1)); // ´por alguna razón no permite referenciar a la columna por su nombre ( exist)
            }
        }
        
        cnn.close();
        
        System.out.println( LocalDateTime.now() + " - VERIFYUUIDREQUEST > verifyUUIDRequest >>> Resultado de verificación " + UUID + " >>> EXIST = " + exist );
        
        return exist;
    }
    
    
    public String getKoneshEndPoint(  ) throws Exception{
        Connection cnn = _cnnMan.getConnection();
        CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
        String svalue = null;
        
        CallableStatement cmd = cnn.prepareCall("{call settings_view(?)}");
        cmd.setString("parameter", "koneshws");
        
        boolean exec = cmd.execute();
        if (exec)
        {
            ResultSet rs = cmd.getResultSet();

            result.populate(rs);
            
            rs.close();
            
            while(result.next()){
                svalue = result.getString("svalue");         
            }
        }
        
        cnn.close();

        return svalue;
    }
    
}
