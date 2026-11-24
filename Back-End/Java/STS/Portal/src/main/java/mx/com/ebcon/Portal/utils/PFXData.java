
package mx.com.ebcon.Portal.utils;

import java.time.ZoneId;
import java.time.Instant;
import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.security.KeyStore;
import java.time.ZonedDateTime;
import java.security.cert.Certificate;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class PFXData
{
    public record EntityData(String alias, String subject, Instant valid_from, Instant valid_upto)
    {
        public boolean isValid(Instant a)
        {
            return !a.isBefore(valid_from) && !a.isAfter(valid_upto);
        }

        public ZonedDateTime validUpToLocalZone(ZoneId zone_id)
        {
            return valid_upto.atZone(zone_id);
        }
    }

    public static EntityData extract(InputStream pfx, char[] pswd) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException
    {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(pfx, pswd);

        Enumeration<String> aliases = keystore.aliases();

        while( aliases.hasMoreElements() )
        {
            String alias = aliases.nextElement();
            Certificate cert = keystore.getCertificate(alias);

            if( cert instanceof X509Certificate x509 )
            {// En Java 17+, X509Certificate.getNotBefore().toInstant() y getNotAfter().toInstant()
             // Evitan los problemas de mutabilidad de java.util.Date
                Instant valid_from = x509.getNotBefore().toInstant();
                Instant valid_upto = x509.getNotAfter().toInstant();
                String subject = x509.getSubjectX500Principal().getName();

                return new EntityData(alias, subject, valid_from, valid_upto);
            }
        }

        throw new IllegalStateException("No se encontró ningún certificado X.509 dentro del archivo PFX.");
    }
}
