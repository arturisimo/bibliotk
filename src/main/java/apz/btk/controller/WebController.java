package apz.btk.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import apz.btk.batch.job.QuartzJob;


@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	@Autowired
    ServletContext servletContext; 
	
	@Autowired
	SchedulerFactoryBean schedulerFactory; 
	
	private static final String DATA_LOADER = "JobSchedulingDataLoaderPlugin";
	
	@GetMapping("/")
	public ModelAndView list(Map<String, Object> data) {
		data.put("title" , "Lista de libros");
		return new ModelAndView("libros", data);
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/admin/libros")
	@Secured({ "ROLE_ADMIN" })
	public ModelAndView admin(Map<String, Object> data) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();

		data.put("date", new Date());
		data.put("title" , "Admin de libros");
		data.put("username" , name);
		return new ModelAndView("admin", data);
	}
	
	@GetMapping("/admin/jobs")
	@Secured({ "ROLE_ADMIN" })
	public ModelAndView jobs(Map<String, Object> data) throws SchedulerException {
		
		Scheduler scheduler = schedulerFactory.getScheduler();
		
		List<String> groups = scheduler.getJobGroupNames();
		
		/** lista de jobs */
		List<QuartzJob> quartzJobList = new ArrayList<>();
		
		Collections.sort(groups);
		
		// loop jobs by group
		for (String groupName : groups) {
			if (!DATA_LOADER.equalsIgnoreCase(groupName)) {
				
				// get jobkey
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					
					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();
					String jobDescription = scheduler.getJobDetail(jobKey).getDescription();
					
					// get job's trigger
					@SuppressWarnings("unchecked")
					List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
					Date nextFireTime = triggers.get(0).getNextFireTime();
					
					QuartzJob qj = new QuartzJob(jobName, jobGroup, nextFireTime);
					qj.setJobDescription(jobDescription);
					
					quartzJobList.add(qj);
	
				}
			}
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName();
		
		data.put("jobs", quartzJobList);
		data.put("date", new Date());
		data.put("title" , "Admin de jobs");
		data.put("username" , name);
		
		return new ModelAndView("jobs", data);
	}
	
	@GetMapping("/admin/job/list")
	@Secured({ "ROLE_ADMIN" })
	public @ResponseBody List<QuartzJob> listJob() throws SchedulerException {
		
		Scheduler scheduler = schedulerFactory.getScheduler();
		
		List<String> groups = scheduler.getJobGroupNames();
		
		/** lista de jobs */
		List<QuartzJob> quartzJobList = new ArrayList<>();
		
		Collections.sort(groups);
		
		// loop jobs by group
		for (String groupName : groups) {
			// get jobkey
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				
				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();
				String jobDescription = scheduler.getJobDetail(jobKey).getDescription();
				
				// get job's trigger
				@SuppressWarnings("unchecked")
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				Date nextFireTime = triggers.get(0).getNextFireTime();
				
				QuartzJob qj = new QuartzJob(jobName, jobGroup, nextFireTime);
				qj.setJobDescription(jobDescription);
				
				quartzJobList.add(qj);

			}
			
		}
		return quartzJobList;
	}
	
	
	@GetMapping("/admin/fire/{jobName}")
	@Secured({ "ROLE_ADMIN" })
	public @ResponseBody String fireJob(@PathVariable("jobName") String jobName) {
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobKey jobKey = new JobKey(jobName);
			scheduler.triggerJob(jobKey);
			return "OK";
			
		} catch (SchedulerException e) {
			return e.getMessage();
		}
	}
	
	
	@RequestMapping("/fragments/{fragment}")
    public String fragment(@PathVariable("fragment") String fragment) {
    	return "fragments/" + fragment;
    }
	
}
