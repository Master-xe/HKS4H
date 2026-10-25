import mx.com.konesh.config.Config;
import mx.com.konesh.dao.KoneshDao;
import mx.com.konesh.repository.Connector;
import mx.com.konesh.service.KoneshService;
import mx.com.konesh.service.KoneshLibService;
/**
* @author janhell
*/
public class KoneshIntegratorService
{
    public static void main(String[] args)
    {
        Connector cnn = new Connector();
        KoneshDao dao = new KoneshDao(cnn);
        KoneshLibService kls = new KoneshLibService();
        KoneshService ks = new KoneshService(dao, kls);
        try
        {
            ks.claimPendingUUID(Integer.valueOf( Config.get("konesh.claim-pending.records") ));
        }   catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
