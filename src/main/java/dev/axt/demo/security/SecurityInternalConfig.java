package dev.axt.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.axt.demo.domain.UserAuth;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Security Configuration dependencies, handlers, providers, ...
 *
 * @author alextremp
 */
@Configuration
class SecurityInternalConfig {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityInternalConfig.class);

	@Autowired
	private SecurityUserDetailsService userDetailsService;

	@Autowired
	private ApiAuth apiAuth;

	private final ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();

	/**
	 * For a REST backend there's no login page, so security must return a 401
	 * code
	 *
	 * @return
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
			LOG.info("ENTRY >>> rejecting entry: " + authException.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		};
	}

	/**
	 * Database authentication provider using BCrypt password encoder
	 *
	 * @return
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return daoAuthenticationProvider;
	}

	/**
	 * Success login hander, adding cookie auth
	 *
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new SavedRequestAwareAuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				LOG.info("LOGIN >>> " + authentication.getPrincipal());
				UserAuth userAuth = (UserAuth) authentication.getPrincipal();
				apiAuth.put(userAuth, response);
				PrintWriter writer = response.getWriter();
				mapper.writeValue(writer, userAuth);
				writer.flush();
			}
		};
	}

	/**
	 * Failed login handler, returning a 401 code instead of a login page
	 *
	 * @return
	 */
	@Bean
	public AuthenticationFailureHandler failureHandler() {
		return new SimpleUrlAuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
				LOG.warn("LOGIN >>> authentication failure");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
			}
		};
	}

	/**
	 * Logout success handler, removing cookie auth
	 *
	 * @return
	 */
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
			if (authentication != null && authentication.getPrincipal() != null) {
				LOG.info("LOGOUT >>> " + authentication.getPrincipal());
			} else {
				LOG.info("LOGOUT >>> called without authentication");
			}
			apiAuth.remove(request, response);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().flush();
		};
	}

	/**
	 * Filter to restore authentication from the auth cookie
	 *
	 * @param authenticationManager
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationTokenFilter authenticationTokenFilterBean(AuthenticationManager authenticationManager) throws Exception {
		AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManager);
		return authenticationTokenFilter;
	}
}
