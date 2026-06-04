
package mx.com.ebcon.Portal.controller;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import jakarta.servlet.http.HttpSession;

import mx.com.ebcon.Portal.model.User;
import mx.com.ebcon.Portal.model.Catalog;

import mx.com.ebcon.Portal.utils.JListResponse;
import mx.com.ebcon.Portal.store.CatalogsRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/Catalogs")
public class CatalogsController
{
    @Autowired
    private DataSource dataSource;

    @Autowired
    private CatalogsRepository dao;

    @PostMapping("/list")
    public @ResponseBody JListResponse<List<Catalog>> list(HttpSession session, @RequestParam String table)
    {
        Connection connection = null;
        List<Catalog> catalog = new ArrayList<Catalog>();

        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
        {
            return new JListResponse<List<Catalog>>(catalog, 0, false, "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JListResponse<List<Catalog>>(catalog, 0, false, e.getMessage());
        }

        catalog = dao.catalog(connection, table);
        try { connection.close(); } catch(SQLException e){}
        if( catalog == null )
        {
            return new JListResponse<List<Catalog>>(catalog, 0, false, dao.getError());
        }

        return new JListResponse<List<Catalog>>(catalog, catalog.size(), true, dao.getError());
    }
}
