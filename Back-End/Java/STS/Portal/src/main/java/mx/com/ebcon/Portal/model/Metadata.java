package mx.com.ebcon.Portal.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.Table;

@Entity
@Table(name="metadata")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "cancellations_viewsap",
        procedureName = "cancellations_viewsap"
        )
})

public class Metadata implements Serializable
{	
	@Id
	private String uuid;
	
	private String emiiter;
	private String receiver;
	private String rfcEmitter;
	private String rfcReceiver;
	private String satCertDate;
	private String issueDate;
	private float amount;
	private String serie;
	private String folio;
	private String currency;
	private String cfdtype;
	private int estatus;
	private String cancelDate;
	private String recorded;
	
	public String getUuid() 
	{
		return uuid;
	}
	
	public void setUuid(String uuid) 
	{
		this.uuid = uuid;
	}
	
	public String getEmiiter() 
	{
		return emiiter;
	}
	
	public void setEmiiter(String emiiter) 
	{
		this.emiiter = emiiter;
	}
	
	public String getReceiver() 
	{
		return receiver;
	}
	
	public void setReceiver(String receiver) 
	{
		this.receiver = receiver;
	}
	
	public String getRfcEmitter() 
	{
		return rfcEmitter;
	}
	
	public void setRfcEmitter(String rfcEmitter) 
	{
		this.rfcEmitter = rfcEmitter;
	}
	
	public String getRfcReceiver() 
	{
		return rfcReceiver;
	}
	
	public void setRfcReceiver(String rfcReceiver) 
	{
		this.rfcReceiver = rfcReceiver;
	}
	
	public String getSatCertDate() 
	{
		return satCertDate;
	}
	
	public void setSatCertDate(String satCertDate) 
	{
		this.satCertDate = satCertDate;
	}
	
	public String getIssueDate() 
	{
		return issueDate;
	}
	
	public void setIssueDate(String issueDate) 
	{
		this.issueDate = issueDate;
	}
	
	public float getAmount() 
	{
		return amount;
	}
	
	public void setAmount(float amount) 
	{
		this.amount = amount;
	}
	
	public String getSerie() 
	{		
		return serie;
	}
	
	public void setSerie(String serie) 
	{
		this.serie = serie;
	}
	
	public String getFolio() 
	{
		return folio;
	}
	
	public void setFolio(String folio) 
	{
		this.folio = folio;
	}
	
	public String getCurrency() 
	{
		return currency;
	}
	
	public void setCurrency(String currency) 
	{
		this.currency = currency;
	}
	
	public String getCfdtype() 
	{
		return cfdtype;
	}
	
	public void setCfdtype(String cfdtype) 
	{
		this.cfdtype = cfdtype;
	}
	
	public int getEstatus() 
	{
		return estatus;
	}
	
	public void setEstatus(int estatus) 
	{
		this.estatus = estatus;
	}
	
	public String getCancelDate() 
	{
		return cancelDate;
	}
	
	public void setCancelDate(String cancelDate) 
	{
		this.cancelDate = cancelDate;
	}
	
	public String getRecorded() 
	{
		return recorded;
	}
	
	public void setRecorded(String recorded) 
	{
		this.recorded = recorded;
	}
}
