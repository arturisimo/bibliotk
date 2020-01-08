package apz.btk.service;

import java.io.FileNotFoundException;

import net.sf.jasperreports.engine.JRException;

public interface LibroService {
	
	/**
	 * generate PDF
	 * @throws JRException
	 * @throws FileNotFoundException
	 */
	void generateReport() throws Exception;

}
