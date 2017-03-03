package org.bitsofinfo.config;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.bitsofinfo.app.MyAuthenticationManagerAndProvider;
import org.bitsofinfo.app.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) 
@EnableWebSecurity
public class SoapServicesConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	private MyAuthenticationManagerAndProvider myAuthenticationManagerAndProvider;
	
	@Autowired 
	private MyUserDetailsService myUserDetailsService;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/service/**").permitAll();
    }
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
	    return myAuthenticationManagerAndProvider;
	}
	
	@Bean
	public ServletRegistrationBean cxfServletRegistrationBean() {
	    CXFServlet cxfServlet = new CXFServlet();
	    return new ServletRegistrationBean(cxfServlet, "/service/*");
	}

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder authMgrBuilder) throws Exception {
		authMgrBuilder.authenticationProvider(myAuthenticationManagerAndProvider);
    }
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(myUserDetailsService);
		 auth.authenticationProvider(myAuthenticationManagerAndProvider);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return this.myAuthenticationManagerAndProvider;
	}

}
