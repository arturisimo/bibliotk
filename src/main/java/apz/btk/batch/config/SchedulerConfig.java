package apz.btk.batch.config;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import apz.btk.batch.job.LibroJob;

@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {
	
	@Value("${expresion.cron}")
	private String cronExpresion;
	
	
	@Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
    	
    	//Get QuartzInitializerListener 
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory,
                                                     @Qualifier("libroJobTrigger") Trigger libroJobTrigger) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());
        factory.setTriggers(libroJobTrigger);

        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public JobDetailFactoryBean libroJobDetail() {
    	JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(LibroJob.class);
        factoryBean.setDurability(true); // job has to be durable to be stored in DB:
        return factoryBean;
    }
    
    @Bean(name = "libroJobTrigger")
    public Trigger libroJobTrigger(@Qualifier("libroJobDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setStartTime(new Date());
        //cron expresion <second> <minute> <hour> <day-of-month> <month> <day-of-week>
        factoryBean.setCronExpression(cronExpresion);
        factoryBean.setName("LibroJob");
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        try {
        	factoryBean.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return factoryBean.getObject();
    }
    
//    @Bean(name = "libroRepeatJobTrigger")
//    public SimpleTriggerFactoryBean libroRepeatJobTrigger(@Qualifier("libroRepeatJobTrigger") JobDetail jobDetail,
//                                                     @Value("${libroJob.frequency}") long frequency) {
//    	 SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
//         factoryBean.setJobDetail(jobDetail);
//         factoryBean.setStartDelay(0L);
//         factoryBean.setRepeatInterval(frequency);
//         factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
//         // in case of misfire, ignore all missed triggers and continue :
//         factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
//         return factoryBean;
//    }
    
    
    public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
    	
    	private transient AutowireCapableBeanFactory beanFactory;

		@Override
		public void setApplicationContext(final ApplicationContext context) {
		    beanFactory = context.getAutowireCapableBeanFactory();
		}
	
		@Override
		protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
		    final Object job = super.createJobInstance(bundle);
		    beanFactory.autowireBean(job);
		    return job;
		}
    }

}