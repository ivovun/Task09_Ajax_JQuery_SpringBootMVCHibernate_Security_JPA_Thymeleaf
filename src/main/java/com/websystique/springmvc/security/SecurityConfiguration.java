package com.websystique.springmvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private UserDetailsService userDetailsService;

	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	@Lazy
	public SecurityConfiguration(@Qualifier("userServiceImpl") UserDetailsService userDetailsService,  AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.userDetailsService = userDetailsService;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}


	/*
			//testing without security
		http.authorizeRequests()
				.antMatchers("/**").permitAll();
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/user").hasAnyRole("ADMIN", "USER", "DBA")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.and().formLogin().loginPage("/login").loginProcessingUrl("/login").usernameParameter("ssoId").passwordParameter("password")
				.successHandler(authenticationSuccessHandler)
				.and().csrf().and().exceptionHandling().accessDeniedPage("/Access_Denied");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
		return new AuthenticationSuccessHandlerImpl();
	}
}
