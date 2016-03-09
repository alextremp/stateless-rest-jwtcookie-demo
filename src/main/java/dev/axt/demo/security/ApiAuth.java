package dev.axt.demo.security;

import dev.axt.demo.domain.UserAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author alextremp
 */
@Component
class ApiAuth {

	private static final Logger LOG = LoggerFactory.getLogger(ApiAuth.class);

	@Value("${axt.token.cookie}")
	private String cookieName;

	@Value("${axt.token.secret}")
	private String secret;

	@Autowired
	private UserDetailsService userDetailsService;

	public void restoreSecurityContext(HttpServletRequest request) {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String authToken = getRequestToken(request);
			if (authToken != null) {
				Claims tokenClaims = extractTokenClaims(authToken);
				String username = tokenClaims.getSubject();
				if (username != null) {
					UserAuth userAuth = null;

					try {
						userAuth = (UserAuth) userDetailsService.loadUserByUsername(username);
					} catch (UsernameNotFoundException e) {
						LOG.warn(String.format(">>> bad user token, username [%s] not found", username));
					}

					if (userAuth != null) {
						String userId = tokenClaims.get("userId", String.class);
						if (userId != null && userId.equals(Long.toString(userAuth.getId()))) {
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userAuth, null, userAuth.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

							SecurityContextHolder.getContext().setAuthentication(authentication);

						} else {
							LOG.warn(String.format(">>> bad user token, username [%s] not related to userId [%s]", username, userId));
						}
					}
				}
			}
		}
	}

	private Claims extractTokenClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody();
	}

	private String generateToken(UserAuth userAuth) {
		Claims claims = Jwts.claims().setSubject(userAuth.getUsername());
		claims.put("userId", Long.toString(userAuth.getId()));

		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	private String getRequestToken(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie c : request.getCookies()) {
				if (cookieName.equals(c.getName())) {
					return c.getValue();
				}
			}
		}
		return null;
	}

	public void put(UserAuth userAuth, HttpServletResponse response) {
		String token = generateToken(userAuth);
		Cookie apiCookie = new Cookie(cookieName, token);
		apiCookie.setHttpOnly(true);
		apiCookie.setMaxAge(86440);
		apiCookie.setPath("/");
		response.addCookie(apiCookie);
	}

	public void remove(HttpServletRequest request, HttpServletResponse response) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookieName.equals(cookie.getName())) {
					Cookie apiCookie = new Cookie(cookieName, "logout");
					apiCookie.setHttpOnly(true);
					apiCookie.setMaxAge(0);
					apiCookie.setPath("/");
					response.addCookie(apiCookie);
				}
			}
		}
	}

}
