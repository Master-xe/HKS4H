
package mx.com.ebcon.Portal;

import java.util.List;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import mx.com.ebcon.Portal.store.CancellationsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import io.spring.guides.gs_producing_web_service.Cfdi;
import io.spring.guides.gs_producing_web_service.SapDocument;
import io.spring.guides.gs_producing_web_service.SapDocumentS4;
import io.spring.guides.gs_producing_web_service.ObjectFactory;
import io.spring.guides.gs_producing_web_service.GetDocumentsRequest;
import io.spring.guides.gs_producing_web_service.SapDocumentsRequest;
import io.spring.guides.gs_producing_web_service.GetDocumentsResponse;
import io.spring.guides.gs_producing_web_service.SapDocumentsResponse;
import io.spring.guides.gs_producing_web_service.GetDocumentsS4Request;
import io.spring.guides.gs_producing_web_service.SapDocumentsS4Request;
import io.spring.guides.gs_producing_web_service.GetDocumentsS4Response;
import io.spring.guides.gs_producing_web_service.SapDocumentsS4Response;

@Endpoint
public class PortalEndpoint
{
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private static final String token = "T2lYQ0t4L0RHVkR4dHZ5Nkk1VHNEakZ3Y0J4Nk9GODZuRyt4cE1wVm5t";

    @Autowired
    private CancellationsRepository dao;

    private CfdisRepository documents;

    @Autowired
    private DataSource dataSource;

    public PortalEndpoint(CfdisRepository documents)
    {
        this.documents = documents;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDocumentsRequest")
    @ResponsePayload
    public GetDocumentsResponse getCfdis(@RequestPayload GetDocumentsRequest request)
    {
        ObjectFactory factory = new ObjectFactory();
        GetDocumentsResponse response = factory.createGetDocumentsResponse();

        if( request.getToken().equals(token) )
        {
            List<Cfdi> cfdis = documents.getCfdis();

            response.getCfdi().addAll(cfdis);
            response.setMessage("OK");
        }   else
        {
            response.setMessage("Access Denied");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDocumentsS4Request")
    @ResponsePayload
    public GetDocumentsS4Response getCfdisS4(@RequestPayload GetDocumentsS4Request request)
    {
        ObjectFactory factory = new ObjectFactory();
        GetDocumentsS4Response response = factory.createGetDocumentsS4Response();

        if( request.getToken().equals(token) )
        {
            List<Cfdi> cfdis = documents.getCfdisS4();

            response.getCfdi().addAll(cfdis);
            response.setMessage("OK");
        }   else
        {
            response.setMessage("Access Denied");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sapDocumentsRequest")
    @ResponsePayload
    public SapDocumentsResponse setCfdis(@RequestPayload SapDocumentsRequest request)
    {
        ObjectFactory factory = new ObjectFactory();
        SapDocumentsResponse response = factory.createSapDocumentsResponse();

        if( request.getToken().equals(token) )
        {
            List<SapDocument> cfdis = request.getDocument();
            Connection connection = null;

            try
            {
                connection = dataSource.getConnection();
            }   catch(SQLException e)
            {
                response.setMessage(e.getMessage());
                return response;
            }

            final boolean result = dao.saveSAPDocuments(connection, cfdis);
            try { connection.close(); } catch(SQLException e){}
            if( result )
            {
                response.setMessage("OK");
            }   else
            {
                response.setMessage(dao.getError());
            }
        }   else
        {
            response.setMessage("Access Denied");
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sapDocumentsS4Request")
    @ResponsePayload
    public SapDocumentsS4Response setCfdisS4(@RequestPayload SapDocumentsS4Request request)
    {
        ObjectFactory factory = new ObjectFactory();
        SapDocumentsS4Response response = factory.createSapDocumentsS4Response();

        if( request.getToken().equals(token) )
        {
            List<SapDocumentS4> cfdis = request.getDocument();
            Connection connection = null;

            try
            {
                connection = dataSource.getConnection();
            }   catch(SQLException e)
            {
                response.setMessage(e.getMessage());
                return response;
            }

            final boolean result = dao.saveSAPDocumentsS4(connection, cfdis);
            try { connection.close(); } catch(SQLException e){}
            if( result )
            {
                response.setMessage("OK");
            }   else
            {
                response.setMessage(dao.getError());
            }
        }   else
        {
            response.setMessage("Access Denied");
        }

        return response;
    }
}

