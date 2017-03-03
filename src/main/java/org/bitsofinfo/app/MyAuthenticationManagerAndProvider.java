package org.bitsofinfo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


public class MyAuthenticationManagerAndProvider implements AuthenticationManager, AuthenticationProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(MyAuthenticationManagerAndProvider.class); 
	
	public MyAuthenticationManagerAndProvider() {}
	
	private MyUserDetailsService myUserDetailsService = null;
	
	public MyAuthenticationManagerAndProvider(MyUserDetailsService myUserDetailsService) {
		this.myUserDetailsService = myUserDetailsService;
	}


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		try {
			String user = authentication.getPrincipal().toString(); 
			String pw = authentication.getCredentials().toString();
			
			if (!user.equals("testuser") && !pw.equals("123")) {
				
				throw new BadCredentialsException("Invalid credentials: " + authentication.getPrincipal());
			}
			
			UserDetails userDetails = (UserDetails)this.myUserDetailsService.loadUserByUsername(user);
			
			return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
														   authentication.getCredentials(),
														   userDetails.getAuthorities());
		} catch(BadCredentialsException e) {
			throw e;
			
		} catch(Exception e) {
			throw new InternalAuthenticationServiceException("Unexpected error: " + e.getMessage(),e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		if (UsernamePasswordAuthenticationToken.class == authentication) {
			return true;
		}
		return false;
	}

}
