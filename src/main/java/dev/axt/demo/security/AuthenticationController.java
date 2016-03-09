package dev.axt.demo.security;

import static dev.axt.demo.BackendConstants.METHOD_LOGIN;
import static dev.axt.demo.BackendConstants.METHOD_LOGOUT;
import static dev.axt.demo.BackendConstants.SECURITY_BASE;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login/Logout fake controller as it must be wrapped in SecurityConfig, but
 * it's a way to expose Spring Security login/logout functionality at the Spring
 * Fox Swagger UI
 *
 * @author alextremp
 */
@Order
@RestController
@RequestMapping(SECURITY_BASE)
public class AuthenticationController {

	@RequestMapping(value = METHOD_LOGIN, method = RequestMethod.POST)
	public String login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
		throw new UnsupportedOperationException("login delegates on spring security");
	}

	@RequestMapping(value = METHOD_LOGOUT, method = RequestMethod.POST)
	public String logout() {
		throw new UnsupportedOperationException("logout delegates on spring security");
	}
}
