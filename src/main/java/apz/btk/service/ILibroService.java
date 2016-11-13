package apz.btk.service;

import java.io.FileNotFoundException;

import net.sf.jasperreports.engine.JRException;

public interface ILibroService {
	
	/**
	 * generate PDF
	 * @throws JRException
	 * @throws FileNotFoundException
	 */
	void generateReport() throws JRException, FileNotFoundException;

}
