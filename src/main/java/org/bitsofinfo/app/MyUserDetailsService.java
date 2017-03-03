package org.bitsofinfo.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		
		if (username.equals("testuser")) {
			
			return new UserDetails() {

				private static final long serialVersionUID = 4286834310397192831L;

				@Override
				public Collection<? extends GrantedAuthority> getAuthorities() {
					List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					
					authorities.add(new GrantedAuthority() {

						private static final long serialVersionUID = 1850051549310629007L;

						@Override
						public String getAuthority() {
							return "test-role";
						}
						
					});
					
					return authorities;
				}

				@Override
				public String getPassword() {
					return null;
				}

				@Override
				public String getUsername() {
					return "testuser";
				}

				@Override
				public boolean isAccountNonExpired() {
					return true;
				}

				@Override
				public boolean isAccountNonLocked() {
					return true;
				}

				@Override
				public boolean isCredentialsNonExpired() {
					return isAccountNonExpired();
				}

				@Override
				public boolean isEnabled() {
					return true;
				}
				
			};
		} else {
			throw new UsernameNotFoundException("User not found: " + username);
		}
		
	}
}
