
package mx.com.ebcon.dba;

import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import mx.com.ebcon.utils.CFDI;

public class DBManager
{
    private int code;
    private String error;
    private Connection connection;

    public DBManager(){}

    public int getCode()
    {
        return this.code;
    }

    public String getError()
    {
        return this.error;
    }

    public void closeConnection()
    {
        if( connection != null )
        {
            try { connection.close(); } catch(SQLException e){}
        }
    }

    public boolean openConnection(String hostname, String username, String password, String database, int portnumb)
    {
        this.error = null;
        this.code = 0;

        if( hostname == null || username == null || password == null || database == null )
        {
            this.error = "Input strings parameters musn't be null";
        }

        final String tnsnames = "jdbc:mysql://" + hostname + ":" + portnumb + "/" + database + "?autoReconnect=true&useSSL=true";

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(tnsnames, username, password);

            return (connection == null) ? false : true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }   catch(Exception e)
        {
            this.error = e.getMessage();
            return false;
        }
    }

    public boolean saveMetadata(CFDI cfdi, int record)
    {
        this.error = null;
        this.code = 0;
        int estatus = 10;
        if( cfdi == null )
        {
            cfdi = new CFDI();
            cfdi.uuid = "X";
            cfdi.date = "2020-01-01 00:00:00";
            cfdi.stampdate = "2020-01-01 00:00:00";
        }   else
        {
            estatus = (cfdi.iscancelled) ? 0 : 1;
            cfdi.date = cfdi.date.replace('T', ' ');
            cfdi.stampdate = cfdi.stampdate.replace('T', ' ');
            if( cfdi.cancelled != null ){ cfdi.cancelled = cfdi.cancelled.replace('T', ' '); }
        }

        try
        {
            CallableStatement sp = connection.prepareCall("{call metadata_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            sp.setInt("estatus", estatus);
            sp.setInt("download", record);
            sp.setString("uuid", cfdi.uuid);
            sp.setFloat("amount", cfdi.total);
            sp.setString("folio", cfdi.folio);
            sp.setString("serie", cfdi.serie);
            sp.setString("cfditype", cfdi.type);
            sp.setString("rfcEmitter", cfdi.rfce);
            sp.setString("rfcReceiver", cfdi.rfcr);
            sp.setString("emitter", cfdi.rfcename);
            sp.setString("currency", cfdi.currency);
            sp.setString("receiver", cfdi.rfcrname);
            sp.setTimestamp("issueDate", java.sql.Timestamp.valueOf(cfdi.date));
            sp.setTimestamp("satCertDate", java.sql.Timestamp.valueOf(cfdi.stampdate));
            if( cfdi.seal == null || cfdi.seal.isBlank() ){ sp.setNull("seal", Types.VARCHAR); } else { sp.setString("seal", cfdi.seal); }

            if( cfdi.cancelled == null || cfdi.cancelled.isBlank() )
            {
                sp.setNull("cancelled", Types.TIMESTAMP);
            }   else
            {
                sp.setTimestamp("cancelled", java.sql.Timestamp.valueOf(cfdi.cancelled));
            }   sp.executeUpdate();

            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }
    }

    public boolean saveRespondRequest(int document, int company, int code, String status, String message, String detail)
    {
        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call rrequest_insert(?,?,?,?,?,?)}");
            sp.setString("message", message);
            sp.setString("detail", detail);
            sp.setString("stype", status);
            sp.setInt("docid", document);
            sp.setInt("cmpny", company);
            sp.setInt("scode", code);
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }
    }

    public int saveCancellationRequest(int company, int code, String status, String message, String detail)
    {
        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call crequest_insert(?,?,?,?,?,?)}");
            sp.registerOutParameter("rowid", Types.INTEGER);
            sp.setString("message", message);
            sp.setString("detail", detail);
            sp.setString("stype", status);
            sp.setInt("cmpny", company);
            sp.setInt("scode", code);
            sp.executeQuery();
            return sp.getInt("rowid");
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return 0;
        }
    }

    public boolean saveCancellationUUID(int requestid, String uuid)
    {
        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call cancellation_insert(?,?)}");
            sp.setInt("reqid", requestid);
            sp.setString("uuid", uuid);
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }
    }

    public boolean respondRequests()
    {
        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call respond_requests()}");
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }
    }

    public boolean enrollCompany(String code, String rfc, String name, String request, String password, String status, InputStream fpfx)
    {
        this.error = null;
        this.code = 0;

        rfc = (rfc==null)?"":rfc.toUpperCase();
        code = (code==null)?"":code.toUpperCase();

        try
        {
            CallableStatement sp = connection.prepareCall("{call company_insert(?,?,?,?,?,?,?)}");
            sp.setString("rfc", rfc);
            sp.setString("name", name);
            sp.setString("code", code);
            sp.setString("stat", status);
            sp.setString("data", request);
            sp.setString("pswd", password);
            sp.setBinaryStream("fpfx", fpfx);
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }
    }

    public CachedRowSet getCompanies(String rfc, String status)
    {
        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call companies_view(?,?)}");

            if( rfc == null || rfc.isBlank() ){ sp.setNull("rfc", Types.VARCHAR); } else { sp.setString("rfc", rfc); }
            if( status == null || status.isBlank() ){ sp.setNull("lck", Types.VARCHAR); } else { sp.setString("lck", status); }

            ResultSet result = sp.executeQuery();
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rowset = factory.createCachedRowSet();
            rowset.populate(result);
            result.close();

            return rowset;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return null;
        }
    }

    public CachedRowSet getResponsesList(int company)
    {
        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call respond_list(?)}");
            sp.setInt("company", company);
            ResultSet result = sp.executeQuery();
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rowset = factory.createCachedRowSet();
            rowset.populate(result);
            result.close();
            return rowset;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return null;
        }
    }

    public ResultSet getDownloads()
    {
        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call unzip_downloads()}");
            return sp.executeQuery();
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return null;
        }
    }

    public Map<String,String> getSettings()
    {
        Map<String,String> settings = new HashMap<>();

        this.error = null;
        this.code = 0;

        try
        {
            CallableStatement sp = connection.prepareCall("{call settings_view(?)}");
            sp.setNull("parameter", Types.VARCHAR);

            ResultSet result = sp.executeQuery();

            for(;result.next();)
            {
                settings.put(result.getString("slabel"),result.getString("svalue"));
            }

            result.close();

            return settings;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return settings;
        }
    }
}
