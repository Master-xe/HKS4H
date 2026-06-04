
import java.sql.Blob;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import mx.com.ebcon.dba.DBManager;

public class UnitTest
{
    private static final String database = "portalcfdi";
    private static final String password = "portaluser";
    private static final String username = "portaluser";
    private static final String hostname = "localhost";
    private static final int port = 3306;

    public static void main(String[] args)
    {
        if(args.length == 0)
        {
            System.out.println("usage: $JAVA_HOME/bin/java UnitTest function [args]");
            return;
        }   else if(args.length == 1)
        {
            switch(args[0])
            {
                case "companies:select": getCompanies(); break;
                default: System.out.println("function.notfound");
            }
        }   else if(args.length == 3)
        {
            int companyid = 0;

            try
            {
                companyid = Integer.parseInt(args[2]);
            }   catch(Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }

            switch(args[0])
            {
                case "companies:update": updateCompany(args[1],companyid); break;
                default: System.out.println("function.notfound");
            }
        }
    }

    public static void enrollCompany()
    {
        DBManager db = new DBManager();

        if( db.openConnection(hostname, username, password, database, port) )
        {
            try
            {
                InputStream stream = new FileInputStream("file.pfx");

                if(!db.enrollCompany("code", "XAXX010101000", "Company Name", "CFDI", "password", "N", stream))
                {
                    System.out.println(db.getError());
                }
            }   catch(FileNotFoundException e)
            {
                System.out.println(e.getMessage());
            }   finally
            {
                db.closeConnection();
                db = null;
            }
        }   else
        {
            System.out.println(db.getError());
        }
    }

    public static void updateCompany(String pfx, int cid)
    {
        Connection connection = null; InputStream csd = null; System.out.println("Updating...");
        final String tnsnames = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true&useSSL=true";

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(tnsnames, username, password);

            if( connection == null )
            {
                System.out.println("Connection = null");
                return;
            }

            csd = new FileInputStream(pfx);

            if( csd == null )
            {
                System.out.println("InputStream = null");
                connection.close(); return;
            }

            CallableStatement sp = connection.prepareCall("{call company_update(?,?,?,?,?)}");

            sp.setInt("xcid", cid);
            sp.setString("stat", "N");
            sp.setBinaryStream("fcsd", csd);
            sp.setNull("fpfx", Types.BINARY);
            sp.setNull("pswd", Types.VARCHAR);

            sp.executeUpdate();
            connection.close();
        }   catch(SQLException e)
        {
            System.out.println(e.getErrorCode() + ":" + e.getMessage());
        }   catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }   catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void getCompanies()
    {   System.out.println("Companies");
        DBManager db = new DBManager();

        if(!db.openConnection(hostname, username, password, database, port))
        {
            System.out.println(db.getError()); return;
        }

        final CachedRowSet companies = db.getCompanies(null,"N");
        db.closeConnection();

        if( companies == null )
        {
            System.out.println(db.getError()); return;
        }

        try
        {
            for(;companies.next();)
            {
                InputStream fpfx = companies.getBinaryStream("csdfl");
                String fname = companies.getString("crfc") + "." + companies.getString("cpswd") + ".pfx";
                System.out.println(fname);
                try
                {
                    FileOutputStream file = new FileOutputStream(fname);
                    file.write(fpfx.readAllBytes()); file.close();
                }   catch(FileNotFoundException e)
                {
                    System.out.println(e.getMessage());
                }   catch(IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }   catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
