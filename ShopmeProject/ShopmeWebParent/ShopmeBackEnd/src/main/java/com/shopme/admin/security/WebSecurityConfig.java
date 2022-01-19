package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ShopmeUserDetailsService shopmeUserDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(shopmeUserDetailsService());
		return authenticationProvider;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.authorizeRequests().anyRequest().permitAll();
		http.authorizeRequests()
			.antMatchers("/users/**","/settings/**").hasAuthority("Admin")
			.antMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/products/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
			.antMatchers("/questions/**").hasAnyAuthority("Admin","Assistant")
			.antMatchers("/reviews/**").hasAnyAuthority("Admin","Assistant")
			.antMatchers("/customers/**").hasAnyAuthority("Admin","Salesperson")
			.antMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")
			.antMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson","Shipper")
			.antMatchers("/reports/**").hasAnyAuthority("Admin","Salesperson")
			.antMatchers("/articles/**").hasAnyAuthority("Admin","Editor")
			.antMatchers("/menus/**").hasAnyAuthority("Admin","Editor")
			.anyRequest().authenticated()
			.and().formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and().logout().permitAll()
			.and().rememberMe().key("ac7f2fa7-5b07-4e81-9468-5fae974f561c")
					.tokenValiditySeconds(7 * 24 * 60 * 60)
					.userDetailsService(shopmeUserDetailsService());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**","/js/**","/webjars/**");
	}
	
	
	
	

}
