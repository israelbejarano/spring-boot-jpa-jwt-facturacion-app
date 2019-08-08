package com.ideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ideas.springboot.app.auth.filter.JWTAuthenticationFilter;
import com.ideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.ideas.springboot.app.models.service.JpaUserDetailsService;

/**
 * The Class SpringSecurityConfig. Clase donde se configura la seguridad (Spring Security)
 * @author Israel Bejarano
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/** The success handler. */
	@Autowired
	private LoginSuccessHandler successHandler;
	
	/** The password encoder. */
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/** The user details service. */
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	/**
	 * Configure. Permite el qué ver según tu rol en la app. Como los guards en Angular para las rutas
	 *
	 * @param http the http
	 * @throws Exception the exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar**", "/locale").permitAll()
		// .antMatchers("/ver/**").hasAnyRole("USER")
		// .antMatchers("/uploads/**").hasAnyRole("USER")
		// .antMatchers("/form/**").hasAnyRole("ADMIN")
		// .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		// .antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		/*.and()
	    	.formLogin()
	    	.successHandler(successHandler)
	    	.loginPage("/login")
	    	.permitAll()
	    .and()
	    .logout().permitAll()
	    .and()
	    .exceptionHandling().accessDeniedPage("/error_403")*/
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager()))
	    .csrf().disable()
	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	/**
	 * Configurer globarl.
	 *
	 * @param builder the builder
	 * @throws Exception the exception
	 */
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		builder.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
	}
}