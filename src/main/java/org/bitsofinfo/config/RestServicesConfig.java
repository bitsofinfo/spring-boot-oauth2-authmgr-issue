package org.bitsofinfo.config;

import org.bitsofinfo.app.MyTestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class RestServicesConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private ResourceServerTokenServices tokenServices;

	@Bean
	public MyTestController myTestController() {
		return new MyTestController();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources
			.resourceId("test-resource-id")
			.tokenServices(tokenServices);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()

				.antMatchers("/test-rest/test-method").hasAuthority("test-role")
				
				;
	}
	
	
}
