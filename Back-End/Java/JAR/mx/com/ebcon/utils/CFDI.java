
package mx.com.ebcon.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class CFDI
{
    public float total;
    public float version;
    public float subTotal;
    public float exchangeRate;
    public boolean iscancelled;
    public String noCertificate;
    public String rfcProvCertif;
    public String cancelled;
    public String stampdate;
    public String rfcename;
    public String rfcrname;
    public String currency;
    public String serie;
    public String folio;
    public String date;
    public String seal;
    public String type;
    public String rfce;
    public String rfcr;
    public String uuid;

    private final String cfdi_root = "cfdi:Comprobante";
    private final String cfdi_emitter = "cfdi:Emisor";
    private final String cfdi_receiver = "cfdi:Receptor";
    private final String cfdi_complement = "cfdi:Complemento";
    private final String cfdi_tfd = "tfd:TimbreFiscalDigital";

    public CFDI(){}

    public CFDI(String xmlfile)
    {
        final File file = new File(xmlfile);

        if( file.exists() && file.isFile() )
        {
            try
            {
                InputStream xml = new FileInputStream(file);

                if( xml != null )
                {
                    final Document cfdi = this.getXMLDocument(xml);
                    this.parseDocument(cfdi); file.renameTo(new File(file.getParent() + "/" + this.uuid + ".xml"));
                }
            }   catch(IOException e){}
        }
    }

    private Document getXMLDocument(InputStream xml)
    {
        try
        {
            DocumentBuilderFactory  dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder         dbr = dbf.newDocumentBuilder();
            Document                doc = dbr.parse(xml);

            doc.normalize();
            xml.close();

            return doc;
        }   catch(ParserConfigurationException | SAXException | IOException e)
        {
            return null;
        }
    }

    private void parseDocument(Document cfdi)
    {
        if( cfdi == null ){ return; }

        final NodeList nodes = cfdi.getElementsByTagName(cfdi_root);

        if( nodes.getLength() != 1 ){ return; }

        Node node = nodes.item(0);
        Element root = (Element)node;
        Element xelement = null;
        NodeList xnodes = null;

        date            = root.getAttribute("Fecha");
        seal            = root.getAttribute("Sello");
        folio           = root.getAttribute("Folio");
        serie           = root.getAttribute("Serie");
        currency        = root.getAttribute("Moneda");
        noCertificate   = root.getAttribute("NoCertificado");
        seal            = seal.substring( seal.length() - 8 );
        type            = root.getAttribute("TipoDeComprobante");
        total           = this.analyzer(root.getAttribute("Total"));
        version         = this.analyzer(root.getAttribute("Version"));
        subTotal        = this.analyzer(root.getAttribute("SubTotal"));
        exchangeRate    = this.analyzer(root.getAttribute("TipoCambio"));

        xnodes = root.getElementsByTagName(cfdi_emitter);
        xelement = (Element)xnodes.item(0);
        rfce = xelement.getAttribute("Rfc");
        rfcename = xelement.getAttribute("Nombre");

        xnodes = root.getElementsByTagName(cfdi_receiver);
        xelement = (Element)xnodes.item(0);
        rfcr = xelement.getAttribute("Rfc");
        rfcrname = xelement.getAttribute("Nombre");

        xnodes = cfdi.getElementsByTagName(cfdi_tfd);
        xelement = (Element)xnodes.item(0);
        uuid = xelement.getAttribute("UUID").toUpperCase();
        stampdate = xelement.getAttribute("FechaTimbrado");
        rfcProvCertif = xelement.getAttribute("RfcProvCertif");
    }

    private float analyzer(String value)
    {
        value = (value == null || value.isBlank()) ? "0" : value;
        try { return Float.parseFloat(value); } catch(NumberFormatException e){ return 0; }
    }
}
