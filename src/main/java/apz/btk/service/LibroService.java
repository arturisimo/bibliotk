package apz.btk.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import apz.btk.entity.Libro;
import apz.btk.repository.LibroRepository;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class LibroService implements ILibroService {

    private static final Logger LOG = LoggerFactory.getLogger(LibroService.class);
    
    
    @Value("${path.pdf}")
    private String pathPDF;
    
    @Value("${path.report}")
    private String pathReport;
    
    @Autowired
	LibroRepository libroRepository;

	@Override
	public void generateReport() throws JRException, FileNotFoundException {
		
		List<Libro> librosPublicos = libroRepository.findByAlta("S");
		List<Libro> librosPrivados = libroRepository.findByAlta("N");
		
		Map<String, Object> parameters = new HashMap<String, Object>();	
		parameters.put("librosPublicos", librosPublicos);
		parameters.put("librosPrivados", librosPrivados);
		
		String rutaReport = pathReport + "report.jasper";
		
		LOG.info("generar rutaPDF");
		
		JRDataSource dsPrivados = new JRBeanCollectionDataSource(librosPrivados, false);
		exportarPDFListado(rutaReport, dsPrivados, parameters, pathPDF + "librosPrivados.pdf");
		JRDataSource dsPublicos = new JRBeanCollectionDataSource(librosPublicos, false);
		exportarPDFListado(rutaReport, dsPublicos, parameters, pathPDF + "librosPublicos.pdf");
		
	}
	
	
	private static void exportarPDF(String nombreJasperCompilado, Map<String, Object> parameters, String nombrePdfSalida) throws JRException {
		
		//!!!!!! Prohibido compilar el jasper por cuestiones de performance...!!!!
		InputStream reportStream = LibroService.class.getResourceAsStream(nombreJasperCompilado);

		JasperReport jr = (JasperReport) JRLoader.loadObject(reportStream);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parameters);

		JasperExportManager.exportReportToPdfFile(jasperPrint, nombrePdfSalida);

	}
	
	/**
	 * Metodo que genera un pdf con datasource para listado
	 * @param nombreJasperCompilado ruta completa del jasper compilado (xxxx.jasper)
	 * @param jRDataSource los datos necesario par la tabla
	 * @param parameters parametros que recibe el informe
	 * @param nombrePdfSalida nombre que va tener el pdf generado
	 * @throws JRException si habido un error
	 * @throws FileNotFoundException 
	 */
	public static void exportarPDFListado(String nombreJasperCompilado, JRDataSource jRDataSource, 
			                             Map<String, Object> parameters, String nombrePdfSalida) throws JRException, FileNotFoundException {
		
			InputStream reportStream = new FileInputStream(new File(nombreJasperCompilado));
			
			JasperReport jr = (JasperReport) JRLoader.loadObject(reportStream);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jr, parameters, jRDataSource);

			JasperExportManager.exportReportToPdfFile(jasperPrint, nombrePdfSalida);

	}
}