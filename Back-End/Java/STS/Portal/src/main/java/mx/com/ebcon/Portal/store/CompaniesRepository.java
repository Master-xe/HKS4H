
package mx.com.ebcon.Portal.store;

import java.util.List;
import java.util.ArrayList;

import java.io.InputStream;

import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;

import mx.com.ebcon.Portal.model.Company;
import org.springframework.stereotype.Repository;

@Repository
public class CompaniesRepository
{
    private int code;
    private String error;

    public int getCode(){ return this.code; }

    public String getError(){ return this.error; }

    public boolean delete(Connection connection, int company)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call company_delete(?)}");
            sp.setInt("company", company);
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return false;
        }
    }

    public boolean enroll(Connection connection, Company company, InputStream fcsd, InputStream fpfx)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call company_insert(?,?,?,?,?,?,?,?)}");
            sp.setString("rfc", company.getCrfc());
            sp.setString("name", company.getCname());
            sp.setString("code", company.getCcode());
            sp.setString("stat", company.getClock());
            sp.setString("data", company.getRtype());
            sp.setString("pswd", company.getCpswd());
            sp.setBinaryStream("fcsd", fcsd);
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

    public boolean update(Connection connection, int company, String password, String locked, InputStream fpfx, InputStream fcsd)
    {
        this.code = 0;
        this.error = null;

        company = (company > 0) ? company : 0;
        locked = (locked == null || !locked.equals("Y")) ? "N" : "Y";

        if( password != null && password.length() > 20 )
        {
            this.error = "Contraseña Inválida";
            this.code = 500;

            return false;
        }

        try
        {
            CallableStatement sp = connection.prepareCall("{call company_update(?,?,?,?,?)}");

            sp.setInt("xcid", company);
            sp.setString("stat", locked);

            if( fcsd == null ){ sp.setNull("fcsd", Types.BINARY); } else { sp.setBinaryStream("fcsd", fcsd); }
            if( fpfx == null ){ sp.setNull("fpfx", Types.BINARY); } else { sp.setBinaryStream("fpfx", fpfx); }
            if( password == null ){ sp.setNull("pswd", Types.VARCHAR); } else { sp.setString("pswd", password); }

            sp.executeUpdate();

            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return false;
        }
    }

    public List<Company> view(Connection connection, String rfc, String status)
    {
        this.code = 0;
        this.error = null;

        if( status != null )
        {
            status = (status.equals("N") || status.equals("Y")) ? status : null;
        }

        if( rfc != null && !rfc.matches("[A-Z0-9]{12,13}") )
        {
            this.error = "RFC inválido";
            this.code = 500;

            return null;
        }

        try
        {
            List<Company> companies = new ArrayList<Company>();
            CallableStatement sp = connection.prepareCall("{call companies_view(?,?)}");
            if( rfc == null ){ sp.setNull("rfc", Types.VARCHAR); } else { sp.setString("rfc", rfc); }
            if( status == null ){ sp.setNull("lck", Types.VARCHAR); } else { sp.setString("lck", status); }
            ResultSet rs = sp.executeQuery();

            for(;rs.next();)
            {
                Company company = new Company();
                company.setCid(rs.getInt("cid"));
                company.setCrfc(rs.getString("crfc"));
                company.setCcode(rs.getString("ccode"));
                company.setCdate(rs.getString("cdate"));
                company.setClock(rs.getString("clock"));
                company.setCname(rs.getString("cname"));
                company.setCupdt(rs.getString("cupdt"));
                company.setRtype(rs.getString("reqtype"));
                companies.add(company);
            }

            rs.close();
            return companies;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return null;
        }
    }
}
