package dev.axt.demo.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * User definition
 *
 * @author alextremp
 */
@Data
public class User implements Serializable {

	private Long id;
	private String username;
	private String password;

}
