
package mx.com.ebcon.Portal.controller;

import java.util.List;
import java.util.Base64;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import mx.com.ebcon.Portal.model.User;
import mx.com.ebcon.Portal.utils.Utils;
import mx.com.ebcon.Portal.model.Company;
import mx.com.ebcon.Portal.utils.JResponse;
import mx.com.ebcon.Portal.utils.JListResponse;
import mx.com.ebcon.Portal.store.CompaniesRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/Companies")
public class CompaniesController
{
    @Autowired
    private DataSource dataSource;

    @Autowired
    private CompaniesRepository companiesDAO;

    @PostMapping("/delete/{company}")
    public @ResponseBody JResponse delete(HttpSession session, @PathVariable(value = "company") int company)
    {
        Connection connection = null;
        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null || !jusersession.getProfile().equals("Admin") )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(e.getErrorCode(), -1, "SQL", e.getMessage());
        }

        if(!companiesDAO.delete(connection, company))
        {   try { connection.close(); } catch(SQLException e){}
            return new JResponse(companiesDAO.getCode(), -1, "SQL", companiesDAO.getError());
        }   try { connection.close(); } catch(SQLException e){}

        return new JResponse(0, 0, "OK", "Sociedad eliminada");
    }

    @PostMapping("/listview")
    public @ResponseBody JListResponse<List<Company>> list(HttpSession session, @RequestBody Company company)
    {
        Connection connection = null;
        List<Company> companies = new ArrayList<Company>();

        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
        {
            return new JListResponse<List<Company>>(companies, 0, false, "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JListResponse<List<Company>>(companies, 0, false, e.getMessage());
        }

        companies = companiesDAO.view(connection, company.getCrfc(), company.getClock());
        try { connection.close(); } catch(SQLException e){}
        if( companies == null )
        {
            return new JListResponse<List<Company>>(companies, 0, false, companiesDAO.getError());
        }

        return new JListResponse<List<Company>>(companies, companies.size(), true, companiesDAO.getError());
    }

    @PostMapping("/update")
    public @ResponseBody JResponse update(HttpSession session, @RequestBody Company company)
    {
        Connection connection = null; InputStream fcsd = null, fpfx = null;
        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null || !jusersession.getProfile().equals("Admin") )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        if( company.getFlpfx() != null && !company.getFlpfx().isBlank() )
        {
            int pointer = company.getFlpfx().indexOf(44) + 1;
            company.setFlpfx(company.getFlpfx().substring(pointer));

            try
            {
                byte[] bytes = Base64.getDecoder().decode(company.getFlpfx());
                fpfx = new ByteArrayInputStream(bytes); bytes = null;
            }   catch(IllegalArgumentException e)
            {
                return new JResponse(500, -1, "SRV", "Error con la FIEL: " + e.getMessage());
            }
        }

        if( company.getFlcsd() != null && !company.getFlcsd().isBlank() )
        {
            int pointer = company.getFlcsd().indexOf(44) + 1;
            company.setFlcsd(company.getFlcsd().substring(pointer));

            try
            {
                byte[] bytes = Base64.getDecoder().decode(company.getFlcsd());
                fcsd = new ByteArrayInputStream(bytes); bytes = null;
            }   catch(IllegalArgumentException e)
            {
                return new JResponse(500, -1, "SRV", "Error con el Sello/CSD: " + e.getMessage());
            }
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(e.getErrorCode(), -1, "SQL", e.getMessage());
        }

        if(!companiesDAO.update(connection, company.getCid(), company.getCpswd(), company.getClock(), fcsd, fpfx))
        {   try { connection.close(); } catch(SQLException e){}
            return new JResponse(companiesDAO.getCode(), -1, "SQL", companiesDAO.getError());
        }   try { connection.close(); } catch(SQLException e){}

        return new JResponse(0, 0, "OK", "Sociedad actualizada");
    }

    @PostMapping("/enroll")
    public @ResponseBody JResponse enroll(HttpSession session, @RequestBody Company company)
    {
        Connection connection = null; InputStream fcsd = null, fpfx = null;
        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        final String validation = Utils.validateCompanyData(company);

        if(!validation.equals("OK"))
        {
            return new JResponse(500, -1, "SRV", validation);
        }

        int pointer = company.getFlpfx().indexOf(44) + 1;
        company.setFlpfx(company.getFlpfx().substring(pointer));

        pointer = company.getFlcsd().indexOf(44) + 1;
        company.setFlcsd(company.getFlcsd().substring(pointer));

        try
        {
            byte[] bytes = Base64.getDecoder().decode(company.getFlpfx());
            fpfx = new ByteArrayInputStream(bytes); bytes = null;

            bytes = Base64.getDecoder().decode(company.getFlcsd());
            fcsd = new ByteArrayInputStream(bytes); bytes = null;
        }   catch(IllegalArgumentException e)
        {
            return new JResponse(500, -1, "SRV", "Error con FIEL/CSD: " + e.getMessage());
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(e.getErrorCode(), -1, "SQL", e.getMessage());
        }

        if(!companiesDAO.enroll(connection, company, fcsd, fpfx))
        {   try { connection.close(); } catch(SQLException e){}
            return new JResponse(500, -1, "SQL", companiesDAO.getError());
        }   try { connection.close(); } catch(SQLException e){}

        return new JResponse(0, 0, "OK", "Sociedad registrada");
    }
}
