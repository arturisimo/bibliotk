package apz.btk.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable()
	    	.authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/fragments/**").permitAll()
	    						.antMatchers("/api/**").permitAll().antMatchers("/js/**").permitAll().antMatchers("/css/**").permitAll()
	    						.anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
	    														  .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll().logoutSuccessUrl("/login?logout");
	
	}
}

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication().withUser("demo").password("demo").roles("demo");
	}
}
