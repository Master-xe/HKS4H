
package io.spring.guides.gs_producing_web_service;

import java.util.List;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "message", "cfdi" })
@XmlRootElement(name = "getDocumentsResponse")
public class GetDocumentsResponse
{
    @XmlElement(required = true)
    protected String message;
    protected List<Cfdi> cfdi;
    public String getMessage(){ return message; }
    public void setMessage(String value){ this.message = value; }
    public List<Cfdi> getCfdi(){ if( cfdi == null ){ cfdi = new ArrayList<Cfdi>(); } return this.cfdi; }
}

