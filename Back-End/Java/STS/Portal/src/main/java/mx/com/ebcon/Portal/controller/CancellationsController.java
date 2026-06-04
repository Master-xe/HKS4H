
package mx.com.ebcon.Portal.controller;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.io.FileOutputStream;
import java.io.IOException;

import java.time.LocalDate;

import java.sql.Date;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import jakarta.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import mx.com.ebcon.Portal.model.User;
import mx.com.ebcon.Portal.model.Cancellation;
import mx.com.ebcon.Portal.model.CancellationsFilter;

import mx.com.ebcon.Portal.utils.JResponse;
import mx.com.ebcon.Portal.utils.JListResponse;
import mx.com.ebcon.Portal.store.CancellationsRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/Cancellations")
public class CancellationsController
{
    @Autowired
    private DataSource dataSource;

    @Autowired
    private CancellationsRepository dao;

    @PostMapping("/saplist")
    public @ResponseBody JListResponse<List<Cancellation>> saplist(HttpSession session)
    {
        Connection connection = null;
        List<Cancellation> cancellations = new ArrayList<Cancellation>();

        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
        {
            return new JListResponse<List<Cancellation>>(cancellations, 0, false, "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JListResponse<List<Cancellation>>(cancellations, 0, false, e.getMessage());
        }

        cancellations = dao.sapview(connection);
        try { connection.close(); } catch(SQLException e){}

        if( cancellations == null )
        {   cancellations = new ArrayList<Cancellation>();
            return new JListResponse<List<Cancellation>>(cancellations, 0, false, dao.getError());
        }   else
        {
            return new JListResponse<List<Cancellation>>(cancellations, cancellations.size(), true, "OK");
        }
    }

    @PostMapping("/listview")
    public @ResponseBody JListResponse<List<Cancellation>> list(HttpSession session, @RequestBody CancellationsFilter filters)
    {
        Connection connection = null;
        List<Cancellation> cancellations = new ArrayList<Cancellation>();

        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
        {
            return new JListResponse<List<Cancellation>>(cancellations, 0, false, "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JListResponse<List<Cancellation>>(cancellations, 0, false, e.getMessage());
        }

        Date from = Date.valueOf(LocalDate.now());
        Date until = Date.valueOf(LocalDate.now().plusDays(1));

        try
        {
            until = Date.valueOf(filters.getEnddate());
            from = Date.valueOf(filters.getStartdate());
        }   catch(Exception e){}

        cancellations = dao.view(connection, filters.getCfdiuuid(), filters.getCompany(), filters.getUserid(), filters.getReplied(), from, until);
        try { connection.close(); } catch(SQLException e){}

        if( cancellations == null )
        {   cancellations = new ArrayList<Cancellation>();
            return new JListResponse<List<Cancellation>>(cancellations, 0, false, dao.getError());
        }   else
        {
            return new JListResponse<List<Cancellation>>(cancellations, cancellations.size(), true, "OK");
        }
    }

    @PostMapping("/update/{documents}")
    public @ResponseBody JResponse update(HttpSession session, @PathVariable(value = "documents") String documents)
    {
        Connection connection = null;
        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
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

        int index = 0; String document, response, token;
        StringTokenizer tokens = new StringTokenizer(documents, "|");

        tokens.nextToken();

        while( tokens.hasMoreTokens() )
        {
            token = tokens.nextToken();
            index = token.indexOf(44);
            document = token.substring(0,index);
            response = token.substring(index+1);
            try{ index = Integer.parseInt(document); } catch(NumberFormatException e){ index = 0; }
            dao.userreply(connection, index, jusersession.getUsrid(), response);
        }   try { connection.close(); } catch(SQLException e){}

        return new JResponse(0, 0, "OK", "Registros Actualizados");
    }

    @PostMapping("/sapselect/{documents}")
    public @ResponseBody JResponse sapselect(HttpSession session, @PathVariable(value = "documents") String documents)
    {
        Connection connection = null;
        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
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

        int index = 0; String document, response, token;
        StringTokenizer tokens = new StringTokenizer(documents, "|");

        tokens.nextToken();

        while( tokens.hasMoreTokens() )
        {
            token = tokens.nextToken();
            index = token.indexOf(44);
            document = token.substring(0,index);
            response = token.substring(index+1);
            try{ index = Integer.parseInt(document); } catch(NumberFormatException e){ index = 0; }
            dao.choiceSAPDocument(connection, index, response);
        }   try { connection.close(); } catch(SQLException e){}

        return new JResponse(0, 0, "OK", "Registros Actualizados");
    }

    @PostMapping("/export")
    public @ResponseBody JResponse export(HttpSession session, @RequestBody CancellationsFilter filters)
    {
        Connection connection = null;
        List<Cancellation> cancellations = new ArrayList<Cancellation>();

        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(500, -1, "SQL", "Acceso Denegado");
        }

        try
        {
            XSSFWorkbook xlsx = new XSSFWorkbook();
            Sheet sheet = xlsx.createSheet("Reporte");
            String fname = UUID.randomUUID().toString();
            Date until = Date.valueOf(filters.getEnddate());
            Date from = Date.valueOf(filters.getStartdate());

            cancellations = dao.view(connection, filters.getCfdiuuid(), filters.getCompany(), filters.getUserid(), filters.getReplied(), from, until);
            try { connection.close(); } catch(SQLException e){}
            Row row = sheet.createRow(0);
            String[] columns;

            if( filters.getReplied() )
            {
                columns = new String[] {"UUID","Fecha","Emisor","Receptor","Monto","Moneda","Proceso","Resultado","Estatus","Mensaje","Descripción","Compensación","Registrado","Actualizado","Respuesta","Usuario"};
            }   else
            {
                columns = new String[] {"UUID","Registrado","Tipo","Fecha","Emisor","Receptor","Serie","Folio","Monto","Moneda","Etapa","Proceso","Pedido","Resultado","Estatus","Mensaje","Búsqueda","Sociedad", "Acreedor","Grupo", "Documento IM","Clase IM","Estatus","Descripción","No. Documento","Clase","Fecha Contable","Referencia","Compensación","Vía Pago","Texto"};
            }

            for(int i=0; i<columns.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(columns[i]);
            }

            if( filters.getReplied() )
            {
                for(int i=0; i<cancellations.size(); i++)
                {
                    row = sheet.createRow(i+1);
                    row.createCell(0).setCellValue(cancellations.get(i).getUuid());
                    row.createCell(1).setCellValue(cancellations.get(i).getIdate());
                    row.createCell(2).setCellValue(cancellations.get(i).getRfce());
                    row.createCell(3).setCellValue(cancellations.get(i).getRfcr());
                    row.createCell(4).setCellValue(cancellations.get(i).getAmount());
                    row.createCell(5).setCellValue(cancellations.get(i).getCurrency());
                    row.createCell(6).setCellValue(cancellations.get(i).getPstype());
                    row.createCell(7).setCellValue(cancellations.get(i).getKresult());
                    row.createCell(8).setCellValue(cancellations.get(i).getKstatus());
                    row.createCell(9).setCellValue(cancellations.get(i).getKmessage());
                    row.createCell(10).setCellValue(cancellations.get(i).getIsgtxt());
                    row.createCell(11).setCellValue(cancellations.get(i).getAugbl());
                    row.createCell(12).setCellValue(cancellations.get(i).getDate());
                    row.createCell(13).setCellValue(cancellations.get(i).getUpdt());
                    row.createCell(14).setCellValue(cancellations.get(i).getResponse());
                    row.createCell(15).setCellValue(cancellations.get(i).getUser());
                }
            }   else
            {
                for(int i=0; i<cancellations.size(); i++)
                {
                    row = sheet.createRow(i+1);
                    row.createCell(0).setCellValue(cancellations.get(i).getUuid());
                    row.createCell(1).setCellValue(cancellations.get(i).getDate());
                    row.createCell(2).setCellValue(cancellations.get(i).getDoctype());
                    row.createCell(3).setCellValue(cancellations.get(i).getIdate());
                    row.createCell(4).setCellValue(cancellations.get(i).getRfce());
                    row.createCell(5).setCellValue(cancellations.get(i).getRfcr());
                    row.createCell(6).setCellValue(cancellations.get(i).getSerie());
                    row.createCell(7).setCellValue(cancellations.get(i).getFolio());
                    row.createCell(8).setCellValue(cancellations.get(i).getAmount());
                    row.createCell(9).setCellValue(cancellations.get(i).getCurrency());
                    row.createCell(10).setCellValue(cancellations.get(i).getStage());
                    row.createCell(11).setCellValue(cancellations.get(i).getPstype());
                    row.createCell(12).setCellValue(cancellations.get(i).getPorder());
                    row.createCell(13).setCellValue(cancellations.get(i).getKresult());
                    row.createCell(14).setCellValue(cancellations.get(i).getKstatus());
                    row.createCell(15).setCellValue(cancellations.get(i).getKmessage());
                    row.createCell(16).setCellValue(cancellations.get(i).getSstat());
                    row.createCell(17).setCellValue(cancellations.get(i).getBukrs());
                    row.createCell(18).setCellValue(cancellations.get(i).getLifnr());
                    row.createCell(19).setCellValue(cancellations.get(i).getKtokk());
                    row.createCell(20).setCellValue(cancellations.get(i).getIbelnr());
                    row.createCell(21).setCellValue(cancellations.get(i).getIblart());
                    row.createCell(22).setCellValue(cancellations.get(i).getBstat());
                    row.createCell(23).setCellValue(cancellations.get(i).getIsgtxt());
                    row.createCell(24).setCellValue(cancellations.get(i).getBelnr());
                    row.createCell(25).setCellValue(cancellations.get(i).getBlart());
                    row.createCell(26).setCellValue(cancellations.get(i).getBudat());
                    row.createCell(27).setCellValue(cancellations.get(i).getXblnr());
                    row.createCell(28).setCellValue(cancellations.get(i).getAugbl());
                    row.createCell(29).setCellValue(cancellations.get(i).getZlsch());
                    row.createCell(30).setCellValue(cancellations.get(i).getSgtxt());
                }
            }

            try
            {
                final String filename = System.getProperty("user.dir") + "/webapps/bovedani/reports/" + fname + ".xlsx";
                FileOutputStream file = new FileOutputStream(filename);
                xlsx.write(file);
                xlsx.close();

                return new JResponse(0, 0, "OK", fname);
            }   catch(IOException e)
            {
                return new JResponse(500, -1, "SRV", e.getMessage());
            }

        }   catch(Exception e)
        {   try { connection.close(); } catch(SQLException ex){}
            return new JResponse(500, -1, "SRV", e.getMessage());
        }
    }
}
