package mx.com.konesh.service;

import java.util.List;

import mx.com.konesh.dao.KoneshDao;
import mx.com.konesh.dto.CFDIRecepcion;
import mx.com.konesh.dto.KoneshCreateDto;
import mx.com.konesh.dto.CancellationListDto;
/**
 * @author janhell
 */
public class KoneshService
{
    private final KoneshDao koneshDao;
    private final KoneshLibService koneshLibService;
    
    public KoneshService(KoneshDao koneshDao, KoneshLibService koneshLibService)
    {
        this.koneshDao = koneshDao;
        this.koneshLibService = koneshLibService;
    }
    
    public void claimPendingUUID(Integer numberOfRecords)throws Exception
    {
        List<CancellationListDto> candidates = koneshDao.ClaimPendingUUID(numberOfRecords);
        
        for(CancellationListDto c : candidates)
        {
            if( !koneshDao.ClaimUUID(c.getCfdiUuid()) )
            {
                continue;
            }
            
            process(c.getDocument(),  c.getCfdiUuid(), c.getDataId());
        }
    }
    
    private void process(Integer document, String UUID, Integer dataId) throws Exception
    {
        Integer receiverId = 0;        
        CFDIRecepcion CFDI = koneshLibService.getUuidData(UUID).get(0);

        this.save(dataId, document, receiverId, CFDI);
    }
    
    private void save(Integer dataId, Integer originId, Integer receiverId/*Company ID Requested CFDI*/, CFDIRecepcion CFDI) throws Exception
    {
        koneshDao.save( new KoneshCreateDto(dataId, originId, receiverId,
        CFDI.getUuid() , CFDI.getRfcReceptor(), CFDI.getRfcEmisor(), CFDI.getTipoProceso(), CFDI.getNumPedido(), CFDI.getResultadoFinal(), CFDI.getEstatus(), CFDI.getMensaje(), CFDI.getMoneda(), CFDI.getTotal(),
        CFDI.getTipoDocumento(), CFDI.getSerie(), CFDI.getFolio(), CFDI.getFechaDoc() ) );
    }
}
