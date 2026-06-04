
package io.spring.guides.gs_producing_web_service;

import java.util.List;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "token", "document" })
@XmlRootElement(name = "sapDocumentsRequest")
public class SapDocumentsRequest
{
    @XmlElement(required = true)
    protected String token;
    @XmlElement(required = true)
    protected List<SapDocument> document;
    public String getToken(){ return token; }
    public void setToken(String value){ this.token = value; }
    public List<SapDocument> getDocument(){ if( document == null ){ document = new ArrayList<SapDocument>(); } return this.document; }
}
