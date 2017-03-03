package org.bitsofinfo.config;

import java.security.KeyPair;

import org.bitsofinfo.app.MyAuthenticationManagerAndProvider;
import org.bitsofinfo.app.MyClientDetailsService;
import org.bitsofinfo.app.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
public class CoreConfigs {
	
	
	@Autowired
	public void doWhatever(AuthenticationManagerBuilder builder) {
		builder.authenticationProvider(new MyAuthenticationManagerAndProvider());
	}

	@Bean	
	public MyAuthenticationManagerAndProvider myAuthenticationManagerAndProvider() {
		return new MyAuthenticationManagerAndProvider();
	}
	
	@Bean	
	@Primary
	public AuthenticationManager authenticationManagerBean() {
		return new MyAuthenticationManagerAndProvider();
	}
	
	@Bean	
	@Primary
	public AuthenticationProvider authenticationProvider() {
		return new MyAuthenticationManagerAndProvider();
	}
	
	
	@Bean
	public MyUserDetailsService myUserDetailsService() {
		return new MyUserDetailsService();
	}
	
	@Bean
	public MyClientDetailsService myClientDetailsService() {
		return new MyClientDetailsService();
	}
	
	@Bean
	@Primary
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtConverter = new JwtAccessTokenConverter();
		jwtConverter.setSigningKey("123");
		return jwtConverter;
	}
	
	@Bean 
	public JwtTokenStore jwtTokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	
}
