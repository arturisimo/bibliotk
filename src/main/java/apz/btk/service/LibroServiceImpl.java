package apz.btk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import apz.btk.entity.Libro;
import apz.btk.repository.LibroRepository;
import apz.btk.util.JasperUtil;

@Service
public class LibroServiceImpl implements LibroService {

    @Value("${path.pdf}")
    private String pathPDF;
    
    @Autowired
	LibroRepository libroRepository;

	@Override
	public void generateReport() throws Exception {
		
		List<Libro> librosPublicos = libroRepository.findByAlta(true);
		List<Libro> librosPrivados = libroRepository.findByAlta(false);
		
		String jasper = LibroServiceImpl.class.getResource("/jasper/report.jasper").getFile();
		
		Map<String, Object> parameters = new HashMap<>();
	    parameters.put("TITULO", "PAISES");
	    parameters.put("FECHA", new java.util.Date());
		
		JasperUtil.listReport(jasper, librosPrivados, JasperUtil.TypeReport.PDF, parameters, pathPDF + "librosPrivados.pdf");
		JasperUtil.listReport(jasper, librosPublicos, JasperUtil.TypeReport.PDF, parameters, pathPDF + "librosPublicos.pdf");
		
	}
	
	
}