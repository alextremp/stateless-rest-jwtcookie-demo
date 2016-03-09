package dev.axt.demo;

/**
 * Backend Constants
 *
 * @author alextremp
 */
public final class BackendConstants {

	public static final String API_BASE = "/api";

	public static final String API_HELLO = API_BASE + "/hello";
	public static final String METHOD_HELLO_PUBLIC = "/public";
	public static final String METHOD_HELLO_AUTHENTICATED = "/authenticated";
	public static final String METHOD_HELLO_ADMIN = "/admin";

	public static final String SECURITY_BASE = "/auth";
	public static final String METHOD_LOGIN = "/login";
	public static final String METHOD_LOGOUT = "/logout";

}
