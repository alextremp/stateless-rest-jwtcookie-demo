package dev.axt.demo.api.hello;

import static dev.axt.demo.BackendConstants.API_HELLO;
import dev.axt.demo.domain.MessageSimple;
import java.security.Principal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static dev.axt.demo.BackendConstants.METHOD_HELLO_PUBLIC;
import static dev.axt.demo.BackendConstants.METHOD_HELLO_AUTHENTICATED;
import static dev.axt.demo.BackendConstants.METHOD_HELLO_ADMIN;

/**
 * Sample Controller
 *
 * @author alextremp
 */
@RestController
@RequestMapping(API_HELLO)
public class HelloController {

	@RequestMapping(value = METHOD_HELLO_PUBLIC, method = RequestMethod.GET)
	public MessageSimple<String> helloPublic(Principal principal) {
		if (principal == null) {
			return new MessageSimple<>("Hello... ?! You're not logged in");
		} else {
			return new MessageSimple<>("Hello... " + principal.getName() + "!");
		}
	}

	@RequestMapping(value = METHOD_HELLO_AUTHENTICATED, method = RequestMethod.GET)
	public MessageSimple<String> helloAuthenticated(Principal principal) {
		return new MessageSimple<>("Hello... " + principal.getName() + "!");
	}

	@RequestMapping(value = METHOD_HELLO_ADMIN, method = RequestMethod.GET)
	public MessageSimple<String> helloAdminOnly(Principal principal) {
		return new MessageSimple<>("Hello... " + principal.getName() + "!");
	}
}
