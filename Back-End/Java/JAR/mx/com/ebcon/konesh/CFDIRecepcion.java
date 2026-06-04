
package mx.com.ebcon.konesh;

import java.util.Date;

public class CFDIRecepcion
{
    private String uuid;
    private String rfcReceptor;
    private String rfcEmisor;
    private String tipoProceso;
    private String numPedido;
    
    private String resultadoFinal;
    private String estatus;
    private String mensaje;
    private String moneda;
    private String total;
    
    private String tipoDocumento;
    private String serie;
    private String folio;
    private String originId; // el registro padre en cancellatios

    private String dataId;
    private String fechaDoc;

    public String getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(String fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

  

    public CFDIRecepcion(){}

    public String getUuid(){ return this.uuid; }
    public String getFolio(){ return this.folio; }
    public String getSerie(){ return this.serie; }
    public String getTotal(){ return this.total; }
    public String getMoneda(){ return this.moneda; }
    public String getEstatus(){ return this.estatus; }
    public String getMensaje(){ return this.mensaje; }
    public String getNumPedido(){ return this.numPedido; }
    public String getRfcEmisor(){ return this.rfcEmisor; }
    public String getRfcReceptor(){ return this.rfcReceptor; }
    public String getTipoProceso(){ return this.tipoProceso; }
    public String getTipoDocumento(){ return this.tipoDocumento; }
    public String getResultadoFinal(){ return this.resultadoFinal; }
    //public String getOrigin() { return this.origin; }
    public String getOriginId() { return this.originId; }

    public void setUuid(String value){ this.uuid = value; }
    public void setFolio(String value){ this.folio = value; }
    public void setSerie(String value){ this.serie = value; }
    public void setTotal(String value){ this.total = value; }
    public void setMoneda(String value){ this.moneda = value; }
    public void setEstatus(String value){ this.estatus = value; }
    public void setMensaje(String value){ this.mensaje = value; }
    public void setNumPedido(String value){ this.numPedido = value; }
    public void setRfcEmisor(String value){ this.rfcEmisor = value; }
    public void setRfcReceptor(String value){ this.rfcReceptor = value; }
    public void setTipoProceso(String value){ this.tipoProceso = value; }
    public void setTipoDocumento(String value){ this.tipoDocumento = value; }
    public void setResultadoFinal(String value){ this.resultadoFinal = value; }
    //public void setOrigin(String _origin) { this.origin = _origin; }
    public void setOriginId(String _originId) { this.originId = _originId; }
    
}

