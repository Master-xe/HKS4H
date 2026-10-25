package mx.com.konesh.dto;

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
    private String fechaDoc;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRfcReceptor() {
        return rfcReceptor;
    }

    public void setRfcReceptor(String rfcReceptor) {
        this.rfcReceptor = rfcReceptor;
    }

    public String getRfcEmisor() {
        return rfcEmisor;
    }

    public void setRfcEmisor(String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }

    public String getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getResultadoFinal() {
        return resultadoFinal;
    }

    public void setResultadoFinal(String resultadoFinal) {
        this.resultadoFinal = resultadoFinal;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(String fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public CFDIRecepcion(
        String uuid,
        String rfcReceptor,
        String rfcEmisor,
        String tipoProceso,
        String numPedido,

        String resultadoFinal,
        String estatus,
        String mensaje,
        String moneda,
        String total,

        String tipoDocumento,
        String serie,
        String folio,
        String fechaDoc
    ){
        this.uuid = uuid;
        this.rfcReceptor = rfcReceptor;
        this.rfcEmisor = rfcEmisor;
        this.tipoProceso = tipoProceso;
        this.numPedido = numPedido;

        this.resultadoFinal = resultadoFinal;
        this.estatus = estatus;
        this.mensaje = mensaje;
        this.moneda = moneda;
        this.total = total;

        this.tipoDocumento = tipoDocumento;
        this.serie = serie;
        this.folio = folio;
        this.fechaDoc = fechaDoc;   
    }
}
