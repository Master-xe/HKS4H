package mx.com.konesh.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import mx.com.konesh.dto.CancellationListDto;
import mx.com.konesh.dto.KoneshCreateDto;
import mx.com.konesh.repository.Connector;
/**
 *
 * @author janhell
 */
public class KoneshDao
{
    private final Connector connector;
    
    public KoneshDao(Connector connector)
    {
        this.connector = connector;
    }
        
    public void save(KoneshCreateDto data)
    {
        String koneshEval;
        Integer newStatus;
        
        try(Connection con = this.connector.getConnection())
        {
            con.setAutoCommit(false);
            
            try{
                CallableStatement cmdKrc = con.prepareCall("{call konesh_request_complete(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
                CallableStatement cmdCsc = con.prepareCall("{call cancellation_save_complete(?,?,?)}");

                if( data.getDataId() == null )
                {
                    cmdKrc.setNull("p_data_id", Types.INTEGER);
                }   else
                {
                    cmdKrc.setInt("p_data_id", data.getDataId());
                }

                cmdKrc.setInt("p_origin_id", data.getOriginId());
                cmdKrc.setInt("p_receiver_id", data.getReceiverId());
                cmdKrc.setString("p_UUID", data.getUUID());
                cmdKrc.setString("p_rec_rfc", data.getRecRfc());
                
                cmdKrc.setString("p_emi_rfc", data.getEmiRfc());
                cmdKrc.setString("p_proc_type", data.getProcType());
                cmdKrc.setString("p_order_number", data.getOrderNumber());
                cmdKrc.setString("p_final_result", data.getFinalResult());
                cmdKrc.setString("p_status", data.getStatus());
                
                cmdKrc.setString("p_message", data.getMessage());
                cmdKrc.setString("p_currency",data.getCurrency());
                cmdKrc.setString("p_total", data.getTotal());
                cmdKrc.setString("p_doc_type", data.getDocType());
                cmdKrc.setString("p_serie", data.getSerie());
                
                cmdKrc.setString("p_folio", data.getFolio());
                cmdKrc.setString("p_doc_date", data.getDocDate());
                cmdKrc.registerOutParameter("o_konesh_eval", Types.VARCHAR );
                cmdKrc.registerOutParameter("o_new_status", Types.INTEGER);
                
                cmdKrc.execute();
                
                koneshEval = cmdKrc.getString("o_konesh_eval");
                newStatus = cmdKrc.getInt("o_new_status");
                
                cmdCsc.setInt("p_document", data.getOriginId());
                cmdCsc.setString("p_konesh_eval", koneshEval);
                cmdCsc.setInt("p_new_status", newStatus);
                
                cmdCsc.execute();
                
                con.commit();
                
            }   catch(Exception e)
            {// algo falló, se revierte
                con.rollback();
                throw e;
            }   finally
            {
                con.setAutoCommit(true);
                con.close();
            }
        }   catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public List<CancellationListDto> ClaimPendingUUID(Integer numberOfRecords) throws Exception
    {
        List<CancellationListDto> result = new ArrayList<>();
    
        try(Connection con = this.connector.getConnection())
        {
            CallableStatement cmd = con.prepareCall("{call konesh_claim_pending_uuid(?)}");
            cmd.setInt("p_number_of_records", numberOfRecords);
            
            if( cmd.execute() )
            {
                try(ResultSet rs = cmd.getResultSet())
                {
                    while(rs.next())
                    {
                        CancellationListDto dto = new CancellationListDto(rs.getInt("document"), rs.getString("cfdiuuid") , rs.getObject("dataId", Integer.class));
                        result.add(dto);
                    }
                }
            }
        }
        
        return result;
    }
    
    
    public Boolean ClaimUUID(String UUID)throws Exception
    {
        try(Connection con = this.connector.getConnection())
        {
            CallableStatement cmd = con.prepareCall("{call konesh_claim_uuid(?, ?)}");
            cmd.setString("p_uuid", UUID);
            cmd.registerOutParameter("o_claimed", Types.BIT);            
            cmd.execute();            
            return cmd.getBoolean("o_claimed");
        }
    }
}
