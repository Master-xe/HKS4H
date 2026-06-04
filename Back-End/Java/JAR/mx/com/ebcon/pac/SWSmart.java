
package mx.com.ebcon.pac;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.BufferedReader;
import java.net.URLConnection;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class SWSmart
{
    private int code;
    private String uid;
    private String rfc;
    private String pfxB64;
    private String password;
    private final String ws;
    private final String token;

    public SWSmart(String webservice, String token)
    {
        this.token = token;
        this.ws = (webservice.endsWith("/")) ? webservice : webservice + "/";
    }

    public String setCompanyData(final String rfc, final String password, InputStream pfx)
    {
        if( rfc == null || password == null || pfx == null )
        {
            this.rfc = null;
            return "Invalid Null Parameters Values";
        }

        try
        {
            pfxB64 = java.util.Base64.getEncoder().encodeToString(pfx.readAllBytes());
            this.password = password;
            this.rfc = rfc;
            pfx = null;
            return "OK";
        }   catch(IOException e)
        {
            this.rfc = null;
            return e.getMessage();
        }
    }

    public Response getCancellationsRequest(final String rfc)
    {
        final String wsrequests = ws + "pendings/" + rfc;
        return this.execRequest(wsrequests, "GET", false, null);
    }

    public Response replyCancellationRequest(String uuid, String answer)
    {
        if( this.rfc == null )
        {
            return new Response(500, "error", "Null Company Data", "Please set company data to reply request", null, null);
        }   this.uid = uuid;

        uuid = "[{\"uuid\":\"" + uuid + "\",\"action\":\"" + answer + "\"}]";
        answer = "{\"uuids\":" + uuid + ",\"password\":\"" + password + "\",\"rfc\":\"" + rfc + "\",\"b64Pfx\":\"" + pfxB64 + "\"}";
        final String wsrespond = ws + "acceptreject/pfx";
        return this.execRequest(wsrespond, "POST", true, answer);
    }

    private Response execRequest(final String endpoint, final String method, final boolean post, final String output)
    {
        final List<String> ids = new ArrayList<String>();
        String acuse, detail, message, status = "error";
        acuse = detail = message = null;
        int code = 500;

        try
        {
            URL url = new URL(endpoint);
            URLConnection connection = url.openConnection();
            HttpsURLConnection https = (HttpsURLConnection)connection;
            https.setRequestProperty("Authorization", "Bearer " + token);
            https.setRequestMethod(method);
            https.setUseCaches(false);
            https.setDoOutput(post);

            if( post )
            {
                https.setRequestProperty("Content-Type", "application/json");
                DataOutputStream ostream = new DataOutputStream(https.getOutputStream());
                ostream.writeBytes(output); // this.LogFile(this.uid + ".rsp", output);
                ostream.flush();
                ostream.close();
            }

            code = https.getResponseCode();

            InputStream istream = (code == 200) ? https.getInputStream() : https.getErrorStream();
            InputStreamReader isreader = new InputStreamReader(istream);

            BufferedReader buffer = new BufferedReader(isreader);
            String text = "" , line = buffer.readLine();

            while( line != null )
            {
                text += line;
                line = buffer.readLine();
            }// if( post ){ this.LogFile(this.uid + ".awr", text); }

            if( code > 402 )
            {
                message = "HTTP Error";
                return new Response(code, status, message, text, acuse, ids);
            }

            JSONObject json = new JSONObject(text);
            status = json.getString("status");

            if( json.has("codStatus") ){ code = json.getInt("codStatus"); }/*
            try{ code = json.getInt("codStatus"); }catch(JSONException e){}*/
            if( json.has("message") ){ message = json.getString("message"); }
            if( json.has("messageDetail") ){ detail = json.getString("messageDetail"); }

            if( json.has("data") )
            {
                try
                {
                    JSONObject data = json.getJSONObject("data");

                    if( code == 1100 )
                    {
                        JSONArray uuids = data.getJSONArray("uuid");
                        for(int i=0; i<uuids.length(); i++){ ids.add(uuids.get(i).toString()); }
                    }   else if( code == 1000 && data.has("acuse") )
                    {
                        acuse = data.getString("acuse");
                        this.LogFile(this.uid + "-acuse.xml", acuse);
                    }
                }   catch(JSONException je){ System.out.println(je.getMessage); }
            }
        }   catch(MalformedURLException e)
        {
            message = "MalformedURLException";
            detail = e.getMessage();
        }   catch(NullPointerException e)
        {
            message = "NullPointerException";
            detail = e.getMessage();
        }   catch(IOException e)
        {
            message = "IOException";
            detail = e.getMessage();
        }   catch(Exception e)
        {
            message = "Exception";
            detail = e.getMessage();
        }   return new Response(code, status, message, detail, acuse, ids);
    }
    
    private void LogFile(String filename, String content)
    {
        try
        {
            java.io.FileWriter fw = new java.io.FileWriter("/opt/portalcfdi/downloads/" + filename, true); fw.write(content); fw.close();
        }   catch(IOException e){ System.out.println(e.getMessage()); }
    }
}
