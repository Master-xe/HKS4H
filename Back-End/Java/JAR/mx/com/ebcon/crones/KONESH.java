/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.crones;


import mx.com.ebcon.konesh.KoneshLib;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.sql.rowset.CachedRowSet;

import mx.com.ebcon.db.spManager;
import mx.com.ebcon.konesh.CFDIRecepcion;


/**
 *
 * @author Angel
 * Librería para consumir los servicios de Konesh de búsqueda de UUID en SAP
 */

public class KONESH {
    private int _receiverId;
    private spManager _spManager;
    private String _endPoint;
    
    public void setReceiver(int receiverId){
        _receiverId = receiverId;
    }
    
    public void setSpManager(spManager spManager){
        _spManager = spManager;
    }
    
    public void setEndPointWS(String endPoint){
        _endPoint = endPoint;
    }   
    
    public void getUUIDData(CachedRowSet records) throws Exception {
        try {
            KoneshLib koneshLib = new KoneshLib(_endPoint);
            CFDIRecepcion cfdi;

            while(records.next()){
                try{
                    cfdi = koneshLib.getUUIDData(records.getString("UUID"));

                    cfdi.setOriginId(records.getString( "originId" ) );
                    cfdi.setDataId( records.getString("dataId"));

                    _spManager.koneshRequest_save(_receiverId, LocalDateTime.now(), cfdi.getUuid(), cfdi.getRfcReceptor(), cfdi.getRfcEmisor()
                            , cfdi.getTipoProceso(), cfdi.getNumPedido(), cfdi.getResultadoFinal(), cfdi.getEstatus(), cfdi.getMensaje()
                            , cfdi.getMoneda(),cfdi.getTotal(), cfdi.getTipoDocumento(), cfdi.getSerie(), cfdi.getFolio()
                            , cfdi.getOriginId(), cfdi.getDataId(), cfdi.getFechaDoc()  );

                }catch(Exception ex){
                    utils _utils = new utils();
                    String msg = _utils.getStackTrace(ex);

                    _spManager.cronesExec_save(0, LocalDateTime.now(), "KONESH > getUUIDData:ERROR", msg );
                    System.out.println(LocalDateTime.now() + " - " + msg);
                }
            }
        }catch(Exception ex){
            throw(new Exception(ex.getMessage()));
        }
    }
   
}
