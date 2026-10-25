package mx.com.konesh.dto;
/**
 * @author janhell
 */
public class CancellationListDto
{
    private Integer dataId;
    private String cfdiUuid;
    private Integer document;

    public CancellationListDto(Integer document, String cfdiUuid, Integer dataId)
    {
        this.dataId = dataId;
        this.cfdiUuid = cfdiUuid;
        this.document = document;
    }

    public Integer getDataId(){ return dataId; }
    public String getCfdiUuid(){ return cfdiUuid; }
    public Integer getDocument(){ return document; }

    public void setDataId(Integer dataId){ this.dataId = dataId; }
    public void setCfdiuuid(String cfdiUuid){ this.cfdiUuid = cfdiUuid; }
    public void setDocument(Integer document){ this.document = document; }
}
