
package io.spring.guides.gs_producing_web_service;

import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "message" })
@XmlRootElement(name = "sapDocumentsResponse")
public class SapDocumentsResponse
{
    @XmlElement(required = true)
    protected String message;
    public String getMessage(){ return message; }
    public void setMessage(String value){ this.message = value; }
}

