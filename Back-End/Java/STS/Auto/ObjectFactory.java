
package io.spring.guides.gs_producing_web_service;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public ObjectFactory(){}
    public Cfdi createCfdi(){ return new Cfdi(); }
    public SapDocument createSapDocument(){ return new SapDocument(); }
    public SapDocumentS4 createSapDocumentS4(){ return new SapDocumentS4(); }
    public GetDocumentsRequest createGetDocumentsRequest(){ return new GetDocumentsRequest(); }
    public SapDocumentsRequest createSapDocumentsRequest(){ return new SapDocumentsRequest(); }
    public GetDocumentsResponse createGetDocumentsResponse(){ return new GetDocumentsResponse(); }
    public SapDocumentsResponse createSapDocumentsResponse(){ return new SapDocumentsResponse(); }
    public GetDocumentsS4Request createGetDocumentsS4Request(){ return new GetDocumentsS4Request(); }
    public SapDocumentsS4Request createSapDocumentsS4Request(){ return new SapDocumentsS4Request(); }
    public GetDocumentsS4Response createGetDocumentsS4Response(){ return new GetDocumentsS4Response(); }
    public SapDocumentsS4Response createSapDocumentsS4Response(){ return new SapDocumentsS4Response(); }
}

