package dev.axt.demo.security;

import static dev.axt.demo.BackendConstants.API_HELLO;
import static dev.axt.demo.BackendConstants.METHOD_HELLO_ADMIN;
import static dev.axt.demo.BackendConstants.METHOD_HELLO_AUTHENTICATED;
import static dev.axt.demo.BackendConstants.METHOD_LOGIN;
import static dev.axt.demo.BackendConstants.METHOD_LOGOUT;
import static dev.axt.demo.BackendConstants.SECURITY_BASE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration for a stateless paradigm
 *
 * @author alextremp
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityInternalConfig internalConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic().disable()
				.exceptionHandling().authenticationEntryPoint(internalConfig.authenticationEntryPoint())
				.and().authenticationProvider(internalConfig.authenticationProvider())
				.formLogin().loginProcessingUrl(SECURITY_BASE + METHOD_LOGIN).successHandler(internalConfig.successHandler()).failureHandler(internalConfig.failureHandler())
				.and().logout().logoutUrl(SECURITY_BASE + METHOD_LOGOUT).logoutSuccessHandler(internalConfig.logoutSuccessHandler()).invalidateHttpSession(true)
				.and().authorizeRequests()
				.antMatchers(API_HELLO + METHOD_HELLO_ADMIN).hasAuthority("ADMIN")
				.antMatchers(API_HELLO + METHOD_HELLO_AUTHENTICATED).authenticated()
				.antMatchers("*").permitAll()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().csrf().disable();

		http.addFilterBefore(internalConfig.authenticationTokenFilterBean(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

	}

}
