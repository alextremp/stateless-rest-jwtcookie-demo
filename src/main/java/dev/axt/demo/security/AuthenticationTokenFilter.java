package dev.axt.demo.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author alextremp
 */
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private ApiAuth apiAuth;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		apiAuth.restoreSecurityContext((HttpServletRequest) request);
		chain.doFilter(request, response);
	}

}
