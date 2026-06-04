
import java.util.Map;
import java.sql.SQLException;
import mx.com.ebcon.pac.SWSmart;
import mx.com.ebcon.pac.Response;
import mx.com.ebcon.utils.LogFile;
import mx.com.ebcon.dba.DBManager;
import javax.sql.rowset.CachedRowSet;

public class CancellationRequests
{
    private static final String database = "portalcfdi";
    private static final String password = "portaluser";
    private static final String username = "portaluser";
    private static final String hostname = "localhost";
    private static final int port = 3306;

    public static void main(String[] args)
    {
        final String log = "requests";
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
        Response rs = null;
        String rfc = null;
        int requestid = 0;
        int cid = 0;

        try
        {
            for(;companies.next();)
            {
                cid = companies.getInt("cid");
                rfc = companies.getString("crfc");
                rs = pac.getCancellationsRequest(rfc);
                LogFile.write(log, rfc);

                requestid = db.saveCancellationRequest(cid, rs.code, rs.status, rs.message, rs.detail);

                if( rs.code == 1100 && requestid > 0 )
                {
                    for(int i=0; i<rs.uuids.size(); i++)
                    {
                        db.saveCancellationUUID(requestid, rs.uuids.get(i));
                    }
                }
            }
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
