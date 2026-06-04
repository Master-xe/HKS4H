
package io.spring.guides.gs_producing_web_service;

import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sapDocument", propOrder = {})
public class SapDocument
{
    @XmlElement(required = true)
    protected String augbl;
    @XmlElement(required = true)
    protected String belnr;
    @XmlElement(required = true)
    protected String blart;
    @XmlElement(required = true)
    protected String bstat;
    @XmlElement(required = true)
    protected String budat;
    @XmlElement(required = true)
    protected String bukrs;
    @XmlElement(required = true)
    protected String clsim;
    @XmlElement(required = true)
    protected String docim;
    @XmlElement(required = true)
    protected String dscim;
    @XmlElement(required = true)
    protected String ktokk;
    @XmlElement(required = true)
    protected String lifnr;
    @XmlElement(required = true)
    protected String sgtxt;
    @XmlElement(required = true)
    protected String stsim;
    @XmlElement(required = true)
    protected String txtps;
    @XmlElement(required = true)
    protected String waers;
    @XmlElement(required = true)
    protected String xblnr;
    @XmlElement(required = true)
    protected String zlsch;

    public String getAugbl(){ return augbl; }
    public String getBelnr(){ return belnr; }
    public String getBlart(){ return blart; }
    public String getBstat(){ return bstat; }
    public String getBudat(){ return budat; }
    public String getBukrs(){ return bukrs; }
    public String getClsim(){ return clsim; }
    public String getDocim(){ return docim; }
    public String getDscim(){ return dscim; }
    public String getKtokk(){ return ktokk; }
    public String getLifnr(){ return lifnr; }
    public String getSgtxt(){ return sgtxt; }
    public String getStsim(){ return stsim; }
    public String getTxtps(){ return txtps; }
    public String getWaers(){ return waers; }
    public String getXblnr(){ return xblnr; }
    public String getZlsch(){ return zlsch; }

    public void setAugbl(String value){ this.augbl = value; }
    public void setBelnr(String value){ this.belnr = value; }
    public void setBlart(String value){ this.blart = value; }
    public void setBstat(String value){ this.bstat = value; }
    public void setBudat(String value){ this.budat = value; }
    public void setBukrs(String value){ this.bukrs = value; }
    public void setClsim(String value){ this.clsim = value; }
    public void setDocim(String value){ this.docim = value; }
    public void setDscim(String value){ this.dscim = value; }
    public void setKtokk(String value){ this.ktokk = value; }
    public void setLifnr(String value){ this.lifnr = value; }
    public void setSgtxt(String value){ this.sgtxt = value; }
    public void setStsim(String value){ this.stsim = value; }
    public void setTxtps(String value){ this.txtps = value; }
    public void setWaers(String value){ this.waers = value; }
    public void setXblnr(String value){ this.xblnr = value; }
    public void setZlsch(String value){ this.zlsch = value; }
}

