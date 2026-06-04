
package mx.com.ebcon.konesh;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.ArrayList;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import javax.net.ssl.HttpsURLConnection;
import javax.sql.rowset.CachedRowSet;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class KoneshLib
{
    private String error;
    
    private String eprefix = "";
    private final String[] prefix = {"ax27:", "ax25:"};
    
    private String _endPoint; // = "https://198.61.151.213/axis2/services/KWSInformacionComplementariaSAPECC";
    private final String[] nodesNames = { "soapenv:Envelope", "soapenv:Body", "ns:obtieneInfoCFDIsResponse", "ns:return", "ax25:cfdis" };
    private final String[] elements = { "uuid", "folio", "serie", "total", "moneda", "estatus", "mensaje", "numPedido", "rfcEmisor", "rfcReceptor", "tipoProceso", "tipoDocumento", "resultadoFinal", "fechaDoc" };

    public KoneshLib( String endPoint ){
        _endPoint = endPoint;
    }

    
    private InputStream documentsRequest(String output) throws NullPointerException, IOException
    {
        //String output = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:kws=\"http://kwservice.heineken.mx.konesh.com\" xmlns:xsd=\"http://beans.kwservice.heineken.mx.konesh.com/xsd\"><soap:Header/><soap:Body><kws:obtieneInfoCFDIs><kws:consultaCfdis><xsd:cfdis><xsd:uuid>0024EF7F-DE86-4E20-9840-82868B4AB2D1</xsd:uuid></xsd:cfdis><xsd:cfdis><xsd:uuid>AEA58326-8BE3-4949-A271-968F0297DA33</xsd:uuid></xsd:cfdis></kws:consultaCfdis></kws:obtieneInfoCFDIs></soap:Body></soap:Envelope>"; // System.setProperty("javax.net.ssl.trustStore", "trust-store.jks"); System.setProperty("javax.net.ssl.trustStorePassword", "TrustStore");
        
        System.out.println( LocalDateTime.now() + "  - KoneshLib > documentsRequest ::: COMUNICANDOSE CON EL WS DE KONESH ::: " + _endPoint  );
        
        URL url = new URL(_endPoint);
        URLConnection connection = url.openConnection();
        HttpsURLConnection https = (HttpsURLConnection)connection;
        https.setHostnameVerifier(new UntrustedSite());
        https.setRequestProperty("Content-Type","application/soap+xml; charset=UTF-8");
        https.setRequestProperty("SOAPAction","urn:obtieneInfoCFDIs");/*
        https.setRequestProperty("Content-Length","length");*/
        https.setRequestMethod("POST");
        https.setUseCaches(false);
        https.setDoOutput(true);
       
        
        /*
        
        https.setDoInput(true);*/

        OutputStream ostream = https.getOutputStream();
        DataOutputStream dostream = new DataOutputStream(ostream);

        dostream.writeBytes(output);
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
        
        System.out.println(LocalDateTime.now() + "  - KoneshLib > documentsRequest - Respuesta de KONESH:" + text);
        
        return new ByteArrayInputStream(text.getBytes());
    }

    private Document getXMLDocument(InputStream xml) throws ParserConfigurationException , SAXException , IOException
    {
        DocumentBuilderFactory  dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder         dbr = dbf.newDocumentBuilder();
        Document                doc = dbr.parse(xml);

        doc.normalize();
        xml.close();

        System.out.println( LocalDateTime.now() + "  - KoneshLib > getXMLDocument - ::: CONVIRTIENDO RESPUESTA DE KONESH A XML DOCUMENT ::: "   );
        
        return doc;
    }
    
    public CFDIRecepcion getUUIDData( String UUID) throws Exception {
        Node node = null;
        NodeList nodes = null, nlist = null;
        Element element = null, item = null;
        
        Document xmlsoap = getXMLDocument (documentsRequest(createTemplateRequest(UUID)));
        
        System.out.println( LocalDateTime.now() + "  - KoneshLib > getUUIDData - ::: COMPROBANDO EXISTENCIA DE NODO " + prefix[0] + " ::: "   );
        nodes = xmlsoap.getElementsByTagName(prefix[0] + "cfdis");
        eprefix = prefix[0];
        
        if (nodes.getLength() <= 0){
            System.out.println( LocalDateTime.now() + "  - KoneshLib > getUUIDData - ::: COMPROBANDO EXISTENCIA DE NODO " + prefix[1] + " ::: "   );
            nodes = xmlsoap.getElementsByTagName(prefix[1] + "cfdis");
            eprefix = prefix[1];
        }

        System.out.println( LocalDateTime.now() + "  - KoneshLib > getUUIDData - ::: INTENTANDO RECUPERAR INFORMACIÓN DE LOS UUIDS PROCESADOS ::: Num. nodos: " + nodes.getLength()  );
        CFDIRecepcion cfdi = new CFDIRecepcion();
        if (nodes.getLength()> 0) {
            
            System.out.println( LocalDateTime.now() + "  - KoneshLib > getUUIDData - ::: NODO RECUPERADO " + nodes.getLength()  );
            
            node = nodes.item(0);
            item = (Element)node;

            for(int k=0; k<elements.length; k++)
            {
                nlist = item.getElementsByTagName(eprefix + elements[k]);
                node = nlist.item(0);// Must Validate Have Only One Node

                switch(k)
                {
                    case 0: cfdi.setUuid(node.getTextContent()); break;
                    case 1: cfdi.setFolio(node.getTextContent()); break;
                    case 2: cfdi.setSerie(node.getTextContent()); break;
                    case 3: cfdi.setTotal(node.getTextContent()); break;
                    case 4: cfdi.setMoneda(node.getTextContent()); break;
                    case 5: cfdi.setEstatus(node.getTextContent()); break;
                    case 6: cfdi.setMensaje(node.getTextContent()); break;
                    case 7: cfdi.setNumPedido(node.getTextContent()); break;
                    case 8: cfdi.setRfcEmisor(node.getTextContent()); break;
                    case 9: cfdi.setRfcReceptor(node.getTextContent()); break;
                    case 10: cfdi.setTipoProceso(node.getTextContent()); break;
                    case 11: cfdi.setTipoDocumento(node.getTextContent()); break;
                    case 12: cfdi.setResultadoFinal(node.getTextContent()); break;
                    case 13: cfdi.setFechaDoc(node.getTextContent());break;
                    default: System.out.println(node.getTextContent());
                }
            }
        }
        return cfdi;
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
        
        System.out.println( LocalDateTime.now() + "  - KoneshLib > createTemplateRequest - ::: CREANDO XML DE SOLICITUD ::: " +  pre + template + post );
        
        return pre + template + post;
    }
}

