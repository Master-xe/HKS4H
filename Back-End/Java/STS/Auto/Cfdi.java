
package io.spring.guides.gs_producing_web_service;

import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfdi", propOrder = { "uuid", "tipo", "fecha", "folio", "serie", "monto", "emisor", "receptor" })
public class Cfdi
{
    @XmlElement(required = true)
    protected String uuid;
    @XmlElement(required = true)
    protected String tipo;
    @XmlElement(required = true)
    protected String fecha;
    @XmlElement(required = true)
    protected String folio;
    @XmlElement(required = true)
    protected String serie;
    protected double monto;
    @XmlElement(required = true)
    protected String emisor;
    @XmlElement(required = true)
    protected String receptor;

    public String getUuid(){ return uuid; }
    public String getTipo(){ return tipo; }
    public String getFecha(){ return fecha; }
    public String getFolio(){ return folio; }
    public String getSerie(){ return serie; }
    public double getMonto(){ return monto; }
    public String getEmisor(){ return emisor; }
    public String getReceptor(){ return receptor; }
    public void setUuid(String value){ this.uuid = value; }
    public void setTipo(String value){ this.tipo = value; }
    public void setFecha(String value){ this.fecha = value; }
    public void setFolio(String value){ this.folio = value; }
    public void setSerie(String value){ this.serie = value; }
    public void setMonto(double value){ this.monto = value; }
    public void setEmisor(String value){ this.emisor = value; }
    public void setReceptor(String value){ this.receptor = value; }
}
