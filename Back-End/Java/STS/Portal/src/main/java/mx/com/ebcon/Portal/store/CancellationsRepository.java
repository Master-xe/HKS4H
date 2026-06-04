
package mx.com.ebcon.Portal.store;

import java.util.List;
import java.util.ArrayList;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;

import mx.com.ebcon.Portal.utils.Utils;
import mx.com.ebcon.Portal.model.Cancellation;

import org.springframework.stereotype.Repository;

import io.spring.guides.gs_producing_web_service.Cfdi;
import io.spring.guides.gs_producing_web_service.SapDocument;

@Repository
public class CancellationsRepository
{
    private int code;
    private String error;

    public int getCode(){ return this.code; }

    public String getError(){ return this.error; }

    public boolean userreply(Connection connection, int document, int username, String response)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call respond_request(?,?,?)}");
            sp.setString("reply", response);
            sp.setInt("usrid", username);
            sp.setInt("docid", document);
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }
    }

    public boolean choiceSAPDocument(Connection connection, int record, String uuid)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call saparchived_select(?,?)}");
            sp.setInt("document", record);
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

    public boolean saveSAPDocuments(Connection connection, List<SapDocument> documents)
    {
        this.code = 0;
        this.error = null;

        String errorlog = null;
        CallableStatement sp = null;

        try
        {
            sp = connection.prepareCall("{call sapdocument_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return false;
        }   errorlog = "";

        for(int i=0; i<documents.size(); i++)
        {
            try
            {
                sp.setString("augbl", documents.get(i).getAugbl());
                sp.setString("belnr", documents.get(i).getBelnr());
                sp.setString("blart", documents.get(i).getBlart());
                sp.setString("bstat", documents.get(i).getStsim());
                sp.setString("budat", documents.get(i).getBudat());
                sp.setString("bukrs", documents.get(i).getBukrs());
                sp.setString("ktokk", documents.get(i).getKtokk());
                sp.setString("lifnr", documents.get(i).getLifnr());
                sp.setString("sgtxt", documents.get(i).getTxtps());
                sp.setString("sstat", documents.get(i).getBstat());
                sp.setString("waers", documents.get(i).getWaers());
                sp.setString("xblnr", documents.get(i).getXblnr());
                sp.setString("zlsch", documents.get(i).getZlsch());
                sp.setString("ibelnr", documents.get(i).getDocim());
                sp.setString("iblart", documents.get(i).getClsim());
                sp.setString("isgtxt", documents.get(i).getDscim());
                sp.setString("yuud", documents.get(i).getSgtxt().toUpperCase());
                sp.executeUpdate();
            }   catch(SQLException e)
            {
                errorlog += documents.get(i).getSgtxt() + ",";
                Utils.write("documents", documents.get(i).getSgtxt() + ":" + e.getMessage());
            }
        }

        if( errorlog.isEmpty() )
        {
            return true;
        }   else
        {
            this.error = errorlog.substring(0, errorlog.length() - 1);
            return false;
        }
    }

    public List<Cfdi> saplist(Connection connection)
    {
        this.code = 0;
        this.error = null;

        List<Cfdi> cfdis = new ArrayList<Cfdi>();

        try
        {
            CallableStatement sp = connection.prepareCall("{call cancellations_viewsap()}");
            ResultSet rs = sp.executeQuery();

            for(;rs.next();)
            {
                Cfdi cfdi = new Cfdi();
                cfdi.setTipo(rs.getString("cfditype"));
                cfdi.setUuid(rs.getString("cfdiuuid"));
                cfdi.setFecha(rs.getString("issueDate"));
                cfdi.setFolio(rs.getString("folio"));
                cfdi.setSerie(rs.getString("serie"));
                cfdi.setMonto(rs.getDouble("amount"));
                cfdi.setEmisor(rs.getString("rfcEmitter"));
                cfdi.setReceptor(rs.getString("rfcReceiver"));
                cfdis.add(cfdi);
            }

            rs.close();
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
        }

        return cfdis;
    }

    public List<Cancellation> sapview(Connection connection)
    {
        this.code = 0;
        this.error = null;

        List<Cancellation> cancellations = new ArrayList<Cancellation>();

        try
        {
            CallableStatement sp = connection.prepareCall("{call saparchived_view()}");
            ResultSet rs = sp.executeQuery();

            for(;rs.next();)
            {
                Cancellation cancellation = new Cancellation();
                cancellation.setIbelnr(rs.getString("ibelnr"));
                cancellation.setIblart(rs.getString("iblart"));
                cancellation.setIsgtxt(rs.getString("isgtxt"));
                cancellation.setAugbl(rs.getString("augbl"));
                cancellation.setBelnr(rs.getString("belnr"));
                cancellation.setBlart(rs.getString("blart"));
                cancellation.setBstat(rs.getString("bstat"));
                cancellation.setBudat(rs.getString("budat"));
                cancellation.setBukrs(rs.getString("bukrs"));
                cancellation.setKtokk(rs.getString("ktokk"));
                cancellation.setLifnr(rs.getString("lifnr"));
                cancellation.setSgtxt(rs.getString("sgtxt"));
                cancellation.setWaers(rs.getString("waers"));
                cancellation.setXblnr(rs.getString("xblnr"));
                cancellation.setZlsch(rs.getString("zlsch"));
                cancellation.setUuid(rs.getString("yuud"));
                cancellation.setDocid(rs.getInt("docid"));
                cancellations.add(cancellation);
            }

            rs.close();
            return cancellations;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return null;
        }
    }

    public List<Cancellation> view(Connection connection, String uuid, int company, int userxid, boolean replied, Date from, Date until)
    {
        this.code = 0;
        this.error = null;

        company = (company > 0) ? company : 0;
        userxid = (userxid > 0) ? userxid : 0;

        try
        {
            List<Cancellation> cancellations = new ArrayList<Cancellation>();
            CallableStatement sp = connection.prepareCall("{call cancellations_view(?,?,?,?,?,?)}");

            if( uuid == null || uuid.isBlank() ){ sp.setNull("docuuid", java.sql.Types.VARCHAR); } else { sp.setString("docuuid", uuid); }

            sp.setBoolean("replied", replied);
            sp.setInt("company", company);
            sp.setInt("userxid", userxid);
            sp.setDate("enddate", until);
            sp.setDate("startdt", from);

            ResultSet rs = sp.executeQuery();

            for(;rs.next();)
            {
                Cancellation cancellation = new Cancellation();
                cancellation.setDocid(rs.getInt("document"));
                cancellation.setAmount(rs.getFloat("amount"));
                cancellation.setElapsed(rs.getInt("elapsed"));
                // SAP
                cancellation.setAugbl(rs.getString("augbl"));
                cancellation.setBelnr(rs.getString("belnr"));
                cancellation.setBlart(rs.getString("blart"));
                cancellation.setBstat(rs.getString("bstat"));
                cancellation.setBudat(rs.getString("budat"));
                cancellation.setBukrs(rs.getString("bukrs"));
                cancellation.setKtokk(rs.getString("ktokk"));
                cancellation.setLifnr(rs.getString("lifnr"));
                cancellation.setSgtxt(rs.getString("sgtxt"));
                cancellation.setSstat(rs.getString("sstat"));
                cancellation.setTxtps(rs.getString("sgtxt"));
                cancellation.setXblnr(rs.getString("xblnr"));
                cancellation.setZlsch(rs.getString("zlsch"));
                cancellation.setIbelnr(rs.getString("ibelnr"));
                cancellation.setIblart(rs.getString("iblart"));
                cancellation.setIsgtxt(rs.getString("isgtxt"));
                // REQ
                cancellation.setDate(rs.getString("created"));
                cancellation.setUpdt(rs.getString("updated"));
                cancellation.setUuid(rs.getString("cfdiuuid"));
                cancellation.setUser(rs.getString("username"));
                cancellation.setStage(rs.getString("curstage"));
                cancellation.setReceipt(rs.getString("receipt"));
                cancellation.setResponse(rs.getString("response"));
                // SAT
                cancellation.setSeal(rs.getString("seal"));
                cancellation.setFolio(rs.getString("folio"));
                cancellation.setSerie(rs.getString("serie"));
                cancellation.setIdate(rs.getString("issueDate"));
                cancellation.setRfce(rs.getString("rfcEmitter"));
                cancellation.setRfcr(rs.getString("rfcReceiver"));
                cancellation.setDoctype(rs.getString("cfditype"));
                cancellation.setCurrency(rs.getString("currency"));
                // PAC
                cancellation.setPstype(rs.getString("procType"));
                cancellation.setPorder(rs.getString("orderNumber"));
                cancellation.setKresult(rs.getString("finalResult"));
                cancellation.setKstatus(rs.getString("kstatus"));
                cancellation.setKmessage(rs.getString("kmessage"));
                cancellations.add(cancellation);
            }

            rs.close();
            return cancellations;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();
            return null;
        }
    }
}
