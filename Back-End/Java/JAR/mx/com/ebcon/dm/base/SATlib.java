package mx.com.ebcon.dm.base;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import mx.com.ebcon.dm.base.Authentication;
import mx.com.ebcon.dm.base.Download;
import mx.com.ebcon.dm.base.Request;
import mx.com.ebcon.dm.base.Verification;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * 2022 NOV 02
 * @author Angel Hernández
 * Implementación de métodos SOAP para comunicación con el SAT mediante sus WS
 */
public class SATlib {
    static String _RFC;
    static X509Certificate _certificate;
    static PrivateKey _privateKey;
    
    public SATlib(String RFC, InputStream PFX, String PFXPass) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        PFX.transferTo(baos);
        
        InputStream iCer = new ByteArrayInputStream(baos.toByteArray()) ;
        InputStream iPri = new ByteArrayInputStream(baos.toByteArray()) ;
        
        _certificate = getCertificate(iCer, PFXPass);
        _privateKey = getPrivateKey(iPri, PFXPass);

        _RFC = RFC;
    }
    
    private static String getAuthorization(String token) {
    // Get Token
            return "WRAP access_token=\"" + decodeValue(token) + "\"";
    }
    
    /**
     * Get a certificate through a pfx file
     *
     * @param inputStream
     * @param pwd
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    private static X509Certificate getCertificate(InputStream inputStream, String pwd)
    throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(inputStream, pwd.toCharArray());
        
        String alias = ks.aliases().nextElement();

        return (X509Certificate) ks.getCertificate(alias);
    }
    
    /**
     * Get a private key through a pfx file
     *
     * @param inputStream
     * @param pwd
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     */
    public static PrivateKey getPrivateKey(InputStream inputStream, String pwd)
            throws Exception {
        
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(inputStream, pwd.toCharArray());
       
        String alias = ks.aliases().nextElement();

        return (PrivateKey) ks.getKey(alias, pwd.toCharArray());
    }

    /**
     * Get XML response through SAT's web service and extract token from it
     *
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws CertificateEncodingException
     */
    public static String getToken()
            throws IOException,
            NoSuchAlgorithmException,
            SignatureException,
            InvalidKeyException,
            CertificateEncodingException {
        
        String urlAutentica = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/Autenticacion/Autenticacion.svc";
        String urlAutenticaAction = "http://DescargaMasivaTerceros.gob.mx/IAutenticacion/Autentica";

    
        Authentication authentication = new Authentication(urlAutentica, urlAutenticaAction);
        authentication.generate(_certificate, _privateKey);

        return authentication.send(null);
    }
    
    /**
     * Get XML response through SAT's web service and extract idRequest from it
     *
     * @param UUID
     * @param requestType
     * @return
     * @throws CertificateEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IOException
     */
    public static String getRequest(String UUID, String requestType)
            throws CertificateEncodingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException,
            IOException
    {
        System.out.println( LocalDateTime.now() + " - SATLib > getRequest:" + UUID  );
        String urlSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/SolicitaDescargaService.svc";
        String urlSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/ISolicitaDescargaService/SolicitaDescarga";
        
        Request request = new Request(urlSolicitud, urlSolicitudAction);

        // Send empty in rfcEmisor if you want to get receiver packages
        // or send empty in rfcReceptor if you want to get sender packages
        request.generate(_certificate, _privateKey, UUID, _RFC, requestType);

        return request.send( getAuthorization(getToken()) );
    }
    
    
    /**
     * Get XML response through SAT's web service and extract idRequest from it
     *
     * @param initialDate
     * @param finalDate
     * @param requestType
     * @return
     * @throws CertificateEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IOException
     */
    public static String getRequest(LocalDateTime initialDate, LocalDateTime finalDate, String requestType)
            throws CertificateEncodingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException,
            IOException {
        System.out.println( LocalDateTime.now() + " - SATLib > getRequest:" + initialDate + '-' + finalDate  );
        String urlSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/SolicitaDescargaService.svc";
        String urlSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/ISolicitaDescargaService/SolicitaDescarga";
        
        Request request = new Request(urlSolicitud, urlSolicitudAction);
       

        // Send empty in rfcEmisor if you want to get receiver packages
        // or send empty in rfcReceptor if you want to get sender packages
        request.generate(_certificate, _privateKey, "", _RFC, _RFC, initialDate, finalDate, requestType);

        return request.send( getAuthorization(getToken()) );
    }
    

    /**
     * Get XML response through SAT's web service and extract idPackages from it
     *
     * @param idRequest
     * @return
     * @throws CertificateEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IOException
     */
    public static String verifyRequest(String idRequest)
            throws CertificateEncodingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException,
            IOException {
        System.out.println( LocalDateTime.now() + " - SATLib > verifyRequest:" + idRequest  );
        String urlVerificarSolicitud = "https://cfdidescargamasivasolicitud.clouda.sat.gob.mx/VerificaSolicitudDescargaService.svc";
        String urlVerificarSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/IVerificaSolicitudDescargaService/VerificaSolicitudDescarga";

    
        Verification verifyRequest = new Verification(urlVerificarSolicitud, urlVerificarSolicitudAction);
        verifyRequest.generate(_certificate, _privateKey, idRequest, _RFC);

        return verifyRequest.send(getAuthorization(getToken()));
    }

    /**
     * Get XML response through SAT's web service and extract Base64's package from it
     *
     * @param idPackage
     * @return
     * @throws IOException
     * @throws CertificateEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String downloadRequest(String idPackage)
            throws IOException,
            CertificateEncodingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        System.out.println( LocalDateTime.now() + " - SATLib > downloadRequest" + idPackage  );
        String urlDescargarSolicitud = "https://cfdidescargamasiva.clouda.sat.gob.mx/DescargaMasivaService.svc";
        String urlDescargarSolicitudAction = "http://DescargaMasivaTerceros.sat.gob.mx/IDescargaMasivaTercerosService/Descargar";

        Download download = new Download(urlDescargarSolicitud, urlDescargarSolicitudAction);
        download.generate(_certificate, _privateKey, _RFC, idPackage);

        return download.send(getAuthorization(getToken()));
    }
    
        /**
     * Decodes a URL encoded string using `UTF-8`
     *
     * @param value
     * @return
     */
    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
