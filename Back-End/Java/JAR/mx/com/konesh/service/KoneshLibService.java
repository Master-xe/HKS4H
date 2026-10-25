package mx.com.konesh.service;

import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import mx.com.konesh.config.*;
import mx.com.konesh.dto.CFDIRecepcion;

public class KoneshLibService
{
    private final String[] ns;
    private final String endPoint;

    public KoneshLibService()
    {
        this.endPoint =  Config.get("konesh.endpoint");
        this.ns = Config.get("konesh.nodes.ns").split(",") ;
    }
    
    private String request(String soapRequest) throws NullPointerException, IOException
    {// System.setProperty("javax.net.ssl.trustStore", "trust-store.jks"); System.setProperty("javax.net.ssl.trustStorePassword", "TrustStore");
        
        URL url = new URL(this.endPoint);
        URLConnection connection = url.openConnection();
        HttpsURLConnection https = (HttpsURLConnection)connection;
        https.setHostnameVerifier(new UntrustedSite());
        https.setRequestProperty("Content-Type","application/soap+xml; charset=UTF-8");
        https.setRequestProperty("SOAPAction","urn:obtieneInfoCFDIs");/*
        https.setRequestProperty("Content-Length","length");*/
        https.setRequestMethod("POST");
        https.setUseCaches(false);
        https.setDoOutput(true);
       
        OutputStream ostream = https.getOutputStream();
        DataOutputStream dostream = new DataOutputStream(ostream);

        dostream.writeBytes(soapRequest);
        dostream.flush();
        dostream.close();

        final int code = https.getResponseCode();

        InputStream istream = (code == 200) ? https.getInputStream() : https.getErrorStream();
        InputStreamReader isreader = new InputStreamReader(istream);
        BufferedReader buffer = new BufferedReader(isreader);
        String text = "", line = buffer.readLine();

        while( line != null )
        {
            text += line;
            line = buffer.readLine();
        }   
        
        return text;
    }

    private Document getXMLDocument(String xmlString) throws ParserConfigurationException , SAXException , IOException
    {
        if( xmlString == null || xmlString.isBlank() )
        {
            throw new IllegalArgumentException("XML string cannot be null or empty");
        }
        
        DocumentBuilderFactory  factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        
        try (StringReader reader = new StringReader(xmlString)) {
                InputSource source = new InputSource(reader);

                return builder.parse(source);
        } catch (IOException | SAXException e) {
            throw new IllegalArgumentException(e.getMessage() );
        }
    }
        
    public List<CFDIRecepcion> getUuidData(String uuid) throws Exception{
        List<CFDIRecepcion> resultadoCFDI = new ArrayList<>();
        String namespace = ""; // String xml = "<>";
        
        Document doc = getXMLDocument(request(createTemplateRequest(uuid))); // Document doc = getXMLDocument(xml);
        
        NodeList nlCfdis = null;
        Element element = null;
       
        if (doc != null){
            doc.getDocumentElement().normalize();
            
            for( String n: ns  ){
                nlCfdis = doc.getElementsByTagNameNS(n, "cfdis");
                
                if (nlCfdis.getLength()>0){
                    namespace = n;
                    break;
                }
            }
            
            if (nlCfdis != null && nlCfdis.getLength()>0){
                for (int i = 0; i< nlCfdis.getLength(); i++){
                    element = (Element)nlCfdis.item(i);
                    
                    resultadoCFDI.add( new CFDIRecepcion(
                            getValue(element, namespace, "uuid"),
                            getValue(element, namespace, "rfcReceptor"),
                            getValue(element, namespace, "rfcEmisor"),
                            getValue(element, namespace, "tipoProceso"),
                            getValue(element, namespace, "numPedido"),
                            
                            getValue(element, namespace, "resultadoFinal"),
                            getValue(element, namespace, "estatus"),
                            getValue(element, namespace, "mensaje"),
                            getValue(element, namespace, "moneda"),
                            getValue(element, namespace, "total"),
                            
                            getValue(element, namespace, "tipoDocumento"),
                            getValue(element, namespace, "serie"),
                            getValue(element, namespace, "folio"),
                            getValue(element, namespace, "fechaDoc")
                    ));
                }
            }
        }else{
            throw new Exception("No se pudo generar el XML.");
        }
        
        return resultadoCFDI;
    }
    
    private static String getValue(
        Element parent,
        String namespace,
        String tagName) {

        NodeList nodes =
            parent.getElementsByTagNameNS(namespace, tagName);

        if (nodes.getLength() == 0) {
            return null;
        }

        return nodes.item(0).getTextContent();
    }
    
    private String createTemplateRequest(String UUIDs) throws Exception {
        String pre = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:kws=\"http://kwservice.heineken.mx.konesh.com\" xmlns:xsd=\"http://beans.kwservice.heineken.mx.konesh.com/xsd\">" +
                            "   <soap:Header/>" +
                            "   <soap:Body>" +
                            "      <kws:obtieneInfoCFDIs>" +
                            "         <kws:consultaCfdis>";
        
        String post = "         </kws:consultaCfdis>" +
                            "      </kws:obtieneInfoCFDIs>" +
                            "   </soap:Body>" +
                            "</soap:Envelope>";
        
        String template = "";
        template = template + "<xsd:cfdis><xsd:uuid>" + UUIDs + "</xsd:uuid></xsd:cfdis>";
        
        return pre + template + post;
    }
}
