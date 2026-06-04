
import java.util.Map;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.sql.SQLException;
import mx.com.ebcon.pac.SWSmart;
import mx.com.ebcon.pac.Response;
import mx.com.ebcon.utils.LogFile;
import mx.com.ebcon.dba.DBManager;
import javax.sql.rowset.CachedRowSet;

public class CancellationResponses
{
    private static final String database = "portalcfdi";
    private static final String password = "portaluser";
    private static final String username = "portaluser";
    private static final String hostname = "localhost";
    private static final int port = 3306;

    public static void main(String[] args)
    {
        final String log = "responses";
        DBManager db = new DBManager();

        if(!db.openConnection(hostname, username, password, database, port))
        {
            LogFile.write(log, db.getError()); return;
        }   LogFile.write(log, "Database Connection Established Successfully");

        final CachedRowSet companies = db.getCompanies(null, "N");

        if( companies == null )
        {
            LogFile.write(log, db.getError());
            db.closeConnection();
            db = null;
            return;
        }

        final Map<String,String> parameters = db.getSettings();
        final String token = parameters.get("pactoken");
        final String wsurl = parameters.get("pacurlws");
        final SWSmart pac = new SWSmart(wsurl, token);
        Response result = null;
        InputStream pfx = null;
        String rfc = null;
        String pw = null;
        int cid = 0;
        int dc = 0;

        try
        {
            for(;companies.next();)
            {
                cid = companies.getInt("cid");
                rfc = companies.getString("crfc");
                pw  = companies.getString("cpswd");
                pfx = companies.getBinaryStream("csdfl");
                pw  = pac.setCompanyData(rfc, pw, pfx);
                LogFile.write(log, rfc + " " + pw);
                CachedRowSet documents = db.getResponsesList(cid);

                if( documents == null )
                {
                    LogFile.write(log, db.getError()); continue;
                }

                for(;documents.next();)
                {
                    dc = documents.getInt("document");
                    pw = documents.getString("response");
                    rfc = documents.getString("cfdiuuid");
                    result = pac.replyCancellationRequest(rfc, pw);

                    if( result.message == null )
                    {
                        result.message = rfc;
                    }   else
                    {
                        result.message += "-" + rfc;
                        if( result.message.length() > 256 ){ result.message = result.message.substring(0, 256); }
                    }   if( result.detail != null && result.detail.length() > 999 ){ result.detail = result.detail.substring(0, 1000); }

                    if(!db.saveRespondRequest(dc, cid, result.code, result.status, result.message, result.detail))
                    {
                        LogFile.write(log, result.message + "|" + dc);
                    }
                }

                documents.release();
            }// db.closeConnection();
        }   catch(SQLException e)
        {
            LogFile.write(log, e.getMessage());
        }   finally
        {
            db.closeConnection();
            db = null;
        }
    }
}
