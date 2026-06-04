package mx.com.ebcon.Portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.ebcon.Portal.model.Metadata;
import mx.com.ebcon.Portal.store.CfdisService;
import mx.com.ebcon.Portal.store.MetadataService;

@RestController
@RequestMapping("/Metadata")
public class MetadataController 
{
	@Autowired
	//private CfdisService cfdisService;
	private MetadataService metadataService;
	
	/*@RequestMapping("/guardar")
	public void visitar()
	{
		cfdisService.saveMetada();
		
	}*/	

	@RequestMapping("/cancelaciones")
	public List<Metadata> cancellationsViewSap()
	{
		return metadataService.getViewsap();
	}
}
