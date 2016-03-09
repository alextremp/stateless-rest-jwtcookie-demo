package dev.axt.demo.security;

import dev.axt.cache.AbstractModelCache;
import dev.axt.cache.StringModelCacheKeyImpl;
import dev.axt.demo.domain.UserAuth;
import dev.axt.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security User Details Service using cache to avoid logged users being
 * repeteadly searched in database
 *
 * @author alextremp
 */
@Service
public class SecurityUserDetailsService extends AbstractModelCache<UserAuth, StringModelCacheKeyImpl> implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * Initializes a 5min alive entries cache
	 */
	public SecurityUserDetailsService() {
		super("SecurityUserDetails", 300L, 50);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return getEntry(new StringModelCacheKeyImpl(username));
	}

	@Override
	protected UserAuth load(StringModelCacheKeyImpl usernameKey) {
		UserAuth userAuth = userMapper.getUserAuthByUsername(usernameKey.getKey());
		if (userAuth == null) {
			throw new UsernameNotFoundException(usernameKey.getKey());
		}
		return userAuth;
	}

}
