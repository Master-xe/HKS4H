
import mx.com.ebcon.dba.DBManager;
import mx.com.ebcon.utils.LogFile;

public class Cancellations
{
    private static final String database = "portalcfdi";
    private static final String password = "portaluser";
    private static final String username = "portaluser";
    private static final String hostname = "localhost";
    private static final int port = 3306;

    public static void main(String[] args)
    {
        final String log = "automatic";
        DBManager db = new DBManager();

        if(!db.openConnection(hostname, username, password, database, port))
        {
            LogFile.write(log, db.getError()); return;
        }   LogFile.write(log, "Database Connection Established Successfully");

        if(!db.respondRequests())
        {
            LogFile.write(log, db.getError());
        }

        db.closeConnection();
    }
}
