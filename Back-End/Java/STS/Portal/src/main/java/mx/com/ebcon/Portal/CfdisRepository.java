
package mx.com.ebcon.Portal;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import io.spring.guides.gs_producing_web_service.Cfdi;
import mx.com.ebcon.Portal.store.CancellationsRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CfdisRepository
{
    @Autowired
    private DataSource dataSource;

    @Autowired
    private CancellationsRepository dao;

    public List<Cfdi> cfdis = new ArrayList<Cfdi>();

    @PostConstruct
    public void initData()
    {
        genCfdis("ECC");
    }

    public List<Cfdi> getCfdis()
    {
        genCfdis("ECC");
        return cfdis;
    }

    public List<Cfdi> getCfdisS4()
    {
        genCfdis("S4H");
        return cfdis;
    }

    private void genCfdis(String sapversion)
    {
        try
        {
            Connection connection = dataSource.getConnection();
            cfdis = dao.saplist(connection, sapversion);
            try { connection.close(); } catch(SQLException e){}
        }   catch(SQLException e){}
    }
}
