package mx.com.ebcon.Portal.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import mx.com.ebcon.Portal.model.Metadata;
import mx.com.ebcon.Portal.model.MetadataRepository;

@Service
public class MetadataServiceImpl implements MetadataService 
{
	@Autowired
	private MetadataRepository metadataRepository;
	
    @Autowired
    @PersistenceContext
    private EntityManager em;
    
    @Override
	@Transactional
	public List<Metadata> getViewsap() 
	{
		// TODO Auto-generated method stub
		return em.createNamedStoredProcedureQuery("cancellations_viewsap").getResultList();
	}

}
