package apz.btk.batch.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import apz.btk.service.LibroServiceImpl;

public class LibroJob implements Job {
	
	private static final Logger LOG = LoggerFactory.getLogger(LibroJob.class);
	
	@Autowired
    private LibroServiceImpl libroService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
        	libroService.generateReport();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
    }
    
}