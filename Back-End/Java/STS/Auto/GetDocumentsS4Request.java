
package io.spring.guides.gs_producing_web_service;

import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "token" })
@XmlRootElement(name = "getDocumentsS4Request")
public class GetDocumentsS4Request
{
    @XmlElement(required = true)
    protected String token;
    public String getToken(){ return token; }
    public void setToken(String value){ this.token = value; }
}

