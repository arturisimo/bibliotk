package apz.btk.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//	  auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
//	}
	
	
	/** definicion de roles y usuario. autenticacion por acceso a BBDD por jdbc */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		/* auth con usuario en BBDD */ 
		auth.jdbcAuthentication()
				.passwordEncoder(encoder())
				.dataSource(dataSource)
	    		.usersByUsernameQuery("select usuario, password, enabled from usuarios where usuario=?")
	    		.authoritiesByUsernameQuery("select usuario, authority from roles r, usuarios u where r.id_usuario=u.id and usuario=?");
		
	}
	
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable()
	    	.authorizeRequests()
	    	.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
	    	.and().formLogin().loginPage("/login").defaultSuccessUrl("/admin/libros")
	    	.failureUrl("/error").permitAll()
	    	.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll().logoutSuccessUrl("/");
	
	}
	
	@Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
	}
	
}
