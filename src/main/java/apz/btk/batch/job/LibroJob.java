package apz.btk.batch.job;

import java.io.FileNotFoundException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import apz.btk.service.LibroService;
import net.sf.jasperreports.engine.JRException;

public class LibroJob implements Job {
    
	@Autowired
    private LibroService service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
			service.generateReport();
		} catch (JRException | FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
}