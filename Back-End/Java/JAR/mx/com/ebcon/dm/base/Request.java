/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.dm.base;

/**
 * 2022 NOV 02
 * @author Angel
 */
import org.w3c.dom.Document;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Base64;
import mx.com.ebcon.crones.utils;
import org.w3c.dom.NodeList;

public class Request extends RequestBase {


    /**
     * Constructor of Request class
     *
     * @param url
     * @param SOAPAction
     */
    public Request(String url, String SOAPAction) {
        super(url, SOAPAction);
    }

    @Override
    protected String getResult(String xmlResponse) { // protected
        
        System.out.println("Request > Respuesta del SAT: " + xmlResponse );
        
        Document doc = convertStringToXMLDocument(xmlResponse);
        
        String[] result = new String[3];
        
        result[1] = "0";
        
        //Verify XML document is build correctly
        if (doc != null){
            NodeList nl; // Verificar NODO
            
            nl = doc.getElementsByTagName("SolicitaDescargaResult");
            
            if (nl.getLength() > 0 ) { //&& nl.item(0).hasChildNodes()

                result[0] = doc.getElementsByTagName("SolicitaDescargaResult")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("IdSolicitud").getTextContent();


                result[1] = doc.getElementsByTagName("SolicitaDescargaResult")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("CodEstatus").getTextContent();

                result[2] = doc.getElementsByTagName("SolicitaDescargaResult")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("Mensaje").getTextContent();
            }
        }
        return String.join("|", result);
    }

    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @param UUID
     * @param rfcSolicitante
     * @param requestType
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */
    public void generate(X509Certificate certificate,
                         PrivateKey privateKey,
                         String UUID,
                         String rfcSolicitante,
                         String requestType
    ) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {

        String canonicalTimestamp = "<SolicitaDescarga xmlns=\"http://DescargaMasivaTerceros.sat.gob.mx\">" +
                "<solicitud Folio=\"" + UUID + "\" RfcSolicitante=\"" + rfcSolicitante + "\" TipoSolicitud=\"" + requestType + "\">" +
                "</solicitud>" +
                "</SolicitaDescarga>";

        String digest = createDigest(canonicalTimestamp);

        String canonicalSignedInfo = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>";

        String signature = sign(canonicalSignedInfo, privateKey);

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<s:Header/>" +
                "<s:Body xmlns:xsd=\"https://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"https://www.w3.org/2001/XMLSchema-instance\">" +
                "<SolicitaDescarga xmlns=\"http://DescargaMasivaTerceros.sat.gob.mx\">" +
                "<solicitud Folio=\"" + UUID + "\" RfcSolicitante=\"" + rfcSolicitante + "\" TipoSolicitud=\"" + requestType + "\">" +
                "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>" +
                "<SignatureValue>" + signature + "</SignatureValue>" +
                "<KeyInfo>" +
                "<X509Data>" +
                "<X509IssuerSerial>" +
                "<X509IssuerName>" + certificate.getIssuerX500Principal() + "</X509IssuerName>" +
                "<X509SerialNumber>" + certificate.getSerialNumber() + "</X509SerialNumber>" +
                "</X509IssuerSerial>" +
                "<X509Certificate>" + Base64.getEncoder().encodeToString(certificate.getEncoded()) + "</X509Certificate>" +
                "</X509Data>" +
                "</KeyInfo>" +
                "</Signature>" +
                "</solicitud>" +
                "</SolicitaDescarga>" +
                "</s:Body>" +
                "</s:Envelope>");
    }
    
    
    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @param rfcEmisor
     * @param rfcReceptor
     * @param rfcSolicitante
     * @param fechaInicial
     * @param fechaFinal
     * @param requestType
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */
    public void generate(X509Certificate certificate,
                         PrivateKey privateKey,
                         String rfcEmisor,
                         String rfcReceptor,
                         String rfcSolicitante,
                         LocalDateTime fechaInicial,
                         LocalDateTime fechaFinal,
                         String requestType
    ) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        //fechaInicial = fechaInicial; //+ "T00:00:00";
        //fechaFinal = fechaFinal ; //+ "T23:59:59";
        
        String id = utils.ldToStr(fechaInicial);
        String fd = utils.ldToStr(fechaFinal);
        
        id = id.replace(" ", "T");
        fd = fd.replace(" ", "T");
        
        String canonicalTimestamp = "<SolicitaDescarga xmlns=\"http://DescargaMasivaTerceros.sat.gob.mx\">" +
                "<solicitud EstadoComprobante=\"" + 0 + "\" RfcEmisor=\"" + rfcEmisor + "\" RfcSolicitante=\"" + rfcSolicitante + "\" FechaInicial=\"" + id + "\" FechaFinal=\"" + fd + "\" TipoSolicitud=\"" + requestType + "\">" +
                "<RfcReceptores>" +
                "<RfcReceptor>" + rfcReceptor + "</RfcReceptor>" +
                "</RfcReceptores>" +
                "</solicitud>" +
                "</SolicitaDescarga>";

        String digest = createDigest(canonicalTimestamp);

        String canonicalSignedInfo = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></CanonicalizationMethod>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"></Transform>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>";

        String signature = sign(canonicalSignedInfo, privateKey);

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<s:Header/>" +
                "<s:Body xmlns:xsd=\"https://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"https://www.w3.org/2001/XMLSchema-instance\">" +
                "<SolicitaDescarga xmlns=\"http://DescargaMasivaTerceros.sat.gob.mx\">" +
                "<solicitud EstadoComprobante=\"" + 0 + "\" FechaFinal =\"" + fd + "\" FechaInicial=\"" + id + "\" RfcEmisor=\"" + rfcEmisor + "\" RfcSolicitante=\"" + rfcSolicitante  + "\" TipoSolicitud=\"" + requestType + "\">" +
                        "<RfcReceptores>" +
                        "<RfcReceptor>" + rfcReceptor + "</RfcReceptor>" +
                        "</RfcReceptores>" +
                "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
                "</Transforms>" +
                "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>" +
                "<DigestValue>" + digest + "</DigestValue>" +
                "</Reference>" +
                "</SignedInfo>" +
                "<SignatureValue>" + signature + "</SignatureValue>" +
                "<KeyInfo>" +
                "<X509Data>" +
                "<X509IssuerSerial>" +
                "<X509IssuerName>" + certificate.getIssuerX500Principal() + "</X509IssuerName>" +
                "<X509SerialNumber>" + certificate.getSerialNumber() + "</X509SerialNumber>" +
                "</X509IssuerSerial>" +
                "<X509Certificate>" + Base64.getEncoder().encodeToString(certificate.getEncoded()) + "</X509Certificate>" +
                "</X509Data>" +
                "</KeyInfo>" +
                "</Signature>" +
                "</solicitud>" +
                "</SolicitaDescarga>" +
                "</s:Body>" +
                "</s:Envelope>");
    }
}
