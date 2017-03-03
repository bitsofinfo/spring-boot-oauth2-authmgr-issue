package org.bitsofinfo.config;

import org.bitsofinfo.app.MyAuthenticationManagerAndProvider;
import org.bitsofinfo.app.MyClientDetailsService;
import org.bitsofinfo.app.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	// We use JWT tokens (never persisted, encrypted, contains all info within)
	@Autowired 
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	// JWT token store, does not actually persist anything anywhere
	@Autowired 
	private JwtTokenStore jwtTokenStore;
	
	@Autowired
	private MyUserDetailsService primaryUserStoreDetailsService;
	
	@Autowired
	private MyClientDetailsService defaultClientDetailsService;
	
	@Autowired
	private MyAuthenticationManagerAndProvider myAuthenticationManagerAndProvider;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer securityConfig) throws Exception {
		securityConfig.tokenKeyAccess("hasAuthority('test-role')");
		/*securityConfig.passwordEncoder(new PasswordEncoder() {

			@Override
			public String encode(CharSequence rawPassword) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});*/
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(this.jwtTokenStore)
			.authenticationManager(myAuthenticationManagerAndProvider)
			.accessTokenConverter(this.jwtAccessTokenConverter)
			.userDetailsService(primaryUserStoreDetailsService)
			.setClientDetailsService(defaultClientDetailsService);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(this.defaultClientDetailsService);
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(this.jwtTokenStore);
		tokenServices.setTokenEnhancer(this.jwtAccessTokenConverter);
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setAccessTokenValiditySeconds(1500);
		tokenServices.setRefreshTokenValiditySeconds(300);
		return tokenServices;
	}

}
