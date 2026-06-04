
package mx.com.ebcon.Portal.store;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;

import mx.com.ebcon.Portal.model.Catalog;
import org.springframework.stereotype.Repository;

@Repository
public class CatalogsRepository
{
    private int code;
    private String error;

    public int getCode(){ return this.code; }

    public String getError(){ return this.error; }

    public List<Catalog> catalog(Connection connection, String table)
    {
        this.code = 0;
        this.error = null;

        List<Catalog> catalog = new ArrayList<Catalog>();
        Catalog all = new Catalog();
        all.setLabel("Todos");
        all.setEntry("0");
        catalog.add(all);

        if( table == null || !table.matches("[a-z]{4,16}") ){ return catalog; }

        try
        {
            CallableStatement sp = connection.prepareCall("{call catalogs(?)}");
            sp.setString("itable", table);

            ResultSet rs = sp.executeQuery();

            for(;rs.next();)
            {
                Catalog catalogue = new Catalog();
                catalogue.setEntry(rs.getString("entry"));
                catalogue.setLabel(rs.getString("label"));
                catalog.add(catalogue);
            }

            rs.close();
            return catalog;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return null;
        }
    }
}
