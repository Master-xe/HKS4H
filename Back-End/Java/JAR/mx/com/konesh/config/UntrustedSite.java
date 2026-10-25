package mx.com.konesh.config;

import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;

public class UntrustedSite implements HostnameVerifier
{
    @Override
    public boolean verify(String hostname, SSLSession session){ return true; }
}
