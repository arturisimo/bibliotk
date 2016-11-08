package apz.btk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import apz.btk.entity.Libro;

@Configuration
public class RepositoryConfig extends RepositoryRestMvcConfiguration  {
    
	@Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Libro.class);
        config.setBasePath("/api");
    }
}


