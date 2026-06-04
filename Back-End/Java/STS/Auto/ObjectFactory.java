
package io.spring.guides.gs_producing_web_service;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public ObjectFactory(){}
    public Cfdi createCfdi(){ return new Cfdi(); }
    public SapDocument createSapDocument(){ return new SapDocument(); }
    public SapDocumentsRequest createSapDocumentsRequest(){ return new SapDocumentsRequest(); }
    public GetDocumentsRequest createGetDocumentsRequest(){ return new GetDocumentsRequest(); }
    public GetDocumentsResponse createGetDocumentsResponse(){ return new GetDocumentsResponse(); }
    public SapDocumentsResponse createSapDocumentsResponse(){ return new SapDocumentsResponse(); }
}
