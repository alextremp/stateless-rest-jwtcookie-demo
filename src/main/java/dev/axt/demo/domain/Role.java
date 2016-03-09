package dev.axt.demo.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * Role definition
 *
 * @author alextremp
 */
@Data
public class Role implements GrantedAuthority {

	private String id;
	private String description;

	@Override
	public String getAuthority() {
		return getId();
	}
}
