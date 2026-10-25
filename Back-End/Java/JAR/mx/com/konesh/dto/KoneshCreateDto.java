package mx.com.konesh.dto;
/**
 *
 * @author janhell
 */
public class KoneshCreateDto
{
    private Integer dataId;
    private Integer originId;
    private Integer receiverId; // id de la empresa que solicitó el CFDI
    private String UUID;
    private String recRfc;

    private String emiRfc;
    private String procType;
    private String orderNumber;
    private String finalResult;
    private String status;

    private String message;
    private String currency;
    private String total;
    private String docType;
    private String serie;

    private String folio;
    private String docDate;

    public KoneshCreateDto
    (
        Integer dataId,
        Integer originId,
        Integer receiverId, // id de la empresa que solicitó el CFDI
        String UUID,
        String recRfc,

        String emiRfc,
        String procType,
        String orderNumber,
        String finalResult,
        String status,

        String message,
        String currency,
        String total,
        String docType,
        String serie,

        String folio,
        String docDate
    ){
        this.dataId = dataId;
        this.originId = originId;
        this.receiverId = receiverId;
        this.UUID = UUID;
        this.recRfc = recRfc;
        this.emiRfc = emiRfc;
        
        this.procType = procType;
        this.orderNumber = orderNumber;
        this.finalResult = finalResult;
        this.status = status;
        this.message = message;
        this.currency = currency;
        this.total = total;
        this.docType = docType;
        this.serie = serie;
        this.folio = folio;
        this.docDate = docDate;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getRecRfc() {
        return recRfc;
    }

    public void setRecRfc(String recRfc) {
        this.recRfc = recRfc;
    }

    public String getEmiRfc() {
        return emiRfc;
    }

    public void setEmiRfc(String emiRfc) {
        this.emiRfc = emiRfc;
    }

    public String getProcType() {
        return procType;
    }

    public void setProcType(String procType) {
        this.procType = procType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
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

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }
}
