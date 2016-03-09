package dev.axt.demo.mapper;

import dev.axt.demo.domain.Role;
import dev.axt.demo.domain.User;
import dev.axt.demo.domain.UserAuth;
import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * MyBatis User Mapper
 *
 * @author alextremp
 */
public interface UserMapper {

	public static final String SQL_SELECT_USER_BY_USERNAME = "select u.id, u.username, u.password from users u where username=#{username}";

	/**
	 * Get an user by username
	 *
	 * @param username
	 * @return
	 */
	@Select(value = SQL_SELECT_USER_BY_USERNAME)
	public User getUserByUsername(@Param("username") String username);

	/**
	 * Get user with authentication details by username
	 *
	 * @param username
	 * @return
	 */
	@Select(value = "select u.id, u.username, u.password from users u where username=#{username}")
	@Results(value = {
		@Result(column = "id", property = "id", javaType = Long.class),
		@Result(column = "id", property = "authorities", javaType = List.class, many = @Many(select = "dev.axt.demo.mapper.UserMapper.getUserRoles"))
	})
	public UserAuth getUserAuthByUsername(@Param("username") String username);

	/**
	 * Get user roles
	 *
	 * @param userId
	 * @return
	 */
	@Select(value = "select r.id, r.description from roles r, users_roles ur where r.id=ur.role_id and ur.user_id=#{userId}")
	public List<Role> getUserRoles(@Param("userId") Long userId);

}
