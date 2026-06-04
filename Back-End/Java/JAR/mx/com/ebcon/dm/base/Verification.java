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
import java.util.Base64;
import mx.com.ebcon.crones.utils;
import org.w3c.dom.NodeList;


public class Verification extends RequestBase {
    
    // Valores para el atributo EstadoSolicitud del SAT
    private int _Aceptada = 1;  // Hay que realizar las verificaciones
    private int _EnProceso = 2; // Necesarias hasta obtener un estado 3
    private int _Terminada = 3; // Listo para descargar
    private int _Error = 4;
    private int _Rechazada = 5; // Se hicieron más de 3 solicitudes con el mismo rango de fechas
    private int _Vencida = 6; // La descarga caducó ¡¡?


    /**
     * Constructor of VerifyRequest class
     *
     * @param url
     * @param SOAPAction
     */
    public Verification(String url, String SOAPAction) {
        super(url, SOAPAction);
    }

    @Override
    protected String getResult(String xmlResponse) {
        String[] result = new String[6];
        result[0] = "0";
        result[2] = "0";
        
        System.out.println("Verification > Respuesta del SAT: " + xmlResponse );
        
        Document doc = convertStringToXMLDocument(xmlResponse);

        //Verify XML document is build correctly
        if (doc != null ) {
            
            NodeList nl; // Verificar NODO
            
            nl = doc.getElementsByTagName("VerificaSolicitudDescargaResult");
            
            if (nl.getLength() > 0 ) {
                result[0] = (doc.getElementsByTagName("VerificaSolicitudDescargaResult")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("EstadoSolicitud").getTextContent());


                result[2] = (doc.getElementsByTagName("VerificaSolicitudDescargaResult")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("CodEstatus").getTextContent());

                result[3] = (doc.getElementsByTagName("VerificaSolicitudDescargaResult")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("Mensaje").getTextContent());

                result[4] = (doc.getElementsByTagName("VerificaSolicitudDescargaResult")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("NumeroCFDIs").getTextContent());


                int codigoEstadoSolicitud = (int)utils.strToNumeric(result[2]);

                if ( codigoEstadoSolicitud == 5000 || codigoEstadoSolicitud == 5004 ){
                    result[5] = (doc.getElementsByTagName("VerificaSolicitudDescargaResult")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("CodigoEstadoSolicitud").getTextContent());
                }

                if (Integer.parseInt(result[0]) == _Terminada){
                    if (Integer.parseInt(result[4]) > 0 ){
                        result[1] = doc.getElementsByTagName("IdsPaquetes").item(0).getTextContent();
                    }
                }
            
            }
        }

        return String.join("|", result) ;
    }

    /**
     * Generate XML to send through SAT's web service
     *
     * @param certificate
     * @param privateKey
     * @param idRequest
     * @param rfcSolicitante
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */
    public void generate(X509Certificate certificate, PrivateKey privateKey, String idRequest, String rfcSolicitante)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, CertificateEncodingException {
        
        String canonicalTimestamp = "<des:VerificaSolicitudDescarga xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\">" +
                "<des:solicitud IdSolicitud=\"" + idRequest + "\" RfcSolicitante=\"" + rfcSolicitante + ">" +
                "</des:solicitud>" +
                "</des:VerificaSolicitudDescarga>";

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

        this.setXml("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:des=\"http://DescargaMasivaTerceros.sat.gob.mx\" xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<s:Header/>" +
                "<s:Body>" +
                "<des:VerificaSolicitudDescarga>" +
                "<des:solicitud IdSolicitud=\"" + idRequest + "\" RfcSolicitante=\"" + rfcSolicitante + "\">" +
                "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<SignedInfo>" +
                "<CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/>" +
                "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>" +
                "<Reference URI=\"#_0\">" +
                "<Transforms>" +
                "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms>" +
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
                "</des:solicitud>" +
                "</des:VerificaSolicitudDescarga>" +
                "</s:Body>" +
                "</s:Envelope>");
    }
}