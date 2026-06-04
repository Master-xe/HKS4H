
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;

import mx.com.ebcon.utils.CFDI;
import mx.com.ebcon.utils.Tools;
import mx.com.ebcon.dba.DBManager;
import mx.com.ebcon.utils.LogFile;

public class UnzipDownloads
{
    private static final String database = "portalcfdi";
    private static final String password = "portaluser";
    private static final String username = "portaluser";
    private static final String hostname = "localhost";
    private static final int port = 3306;

    public static void main(String[] args)
    {
        final String workspace = "/opt/portalcfdi/downloads/";
        final String log = "downloads", filetype = ".zip";
        String base64 = null, fname = null, result = null;
        boolean saved = false; int fileid = 0;
        DBManager db = new DBManager();

        if(!db.openConnection(hostname, username, password, database, port))
        {
            LogFile.write(log, db.getError()); return;
        }   LogFile.write(log, "Database Connection Established Successfully");

        ResultSet downloads = db.getDownloads();

        if( downloads == null )
        {
            LogFile.write(log, db.getError());
            db.closeConnection();
            db = null;
            return;
        }

        try
        {
            for(;downloads.next();)
            {
                fileid = downloads.getInt("verificationId");
                base64 = downloads.getString("package");

                saved = Tools.saveFileBase64(workspace, String.valueOf(fileid), filetype, base64);

                if( saved )
                {
                    fname = fileid + filetype;
                    result = Tools.unzipFile(workspace, fname);

                    String[] files = result.split("\n");

                    for(int i=1; i<files.length; i++)
                    {
                        if( files[i].endsWith(".xml") )
                        {
                            CFDI cfdi = new CFDI(workspace + files[i]);
                            db.saveMetadata(cfdi, fileid);
                        }   else if( files[i].endsWith(".txt") )
                        {
                            List<CFDI> cfdis = Tools.getMetadata(workspace + files[i]);

                            for(int j=0; j<cfdis.size(); j++)
                            {
                                db.saveMetadata(cfdis.get(j), fileid);
                            }
                        }
                    }
                }   else
                {
                    db.saveMetadata(null, fileid);
                }
            }

            downloads.close();
            downloads = null;
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
