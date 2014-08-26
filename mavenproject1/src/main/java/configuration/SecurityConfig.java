/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author Mihai
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages={"service"},includeFilters=@ComponentScan.Filter(value = {org.springframework.stereotype.Service.class},type=FilterType.ANNOTATION))
public class SecurityConfig  extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService userDetailsService;
        
	@Override
        protected void configure(AuthenticationManagerBuilder registry) throws Exception {
		/*
        registry
        .inMemoryAuthentication()
        .withUser("siva")
          .password("siva")
          .roles("USER")
          .and()
        .withUser("admin")
          .password("admin")
          .roles("ADMIN","USER");
        */
        //registry.jdbcAuthentication().dataSource(dataSource);
		registry.userDetailsService(userDetailsService);
        }
	  @Override
	  public void configure(WebSecurity web) throws Exception {
	    web
	      .ignoring()
	         .antMatchers("/resources/**");
	  }
	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	    .csrf().disable()
	    .authorizeRequests()
	        .antMatchers("/login","/register").permitAll()
                .antMatchers("/play/list/refresh","/play/list/undo","/play/list/filter").hasAnyAuthority("manager","user")
	        .antMatchers("/play/list","/play/list/**").hasAnyAuthority("manager")
                .antMatchers("/seat/list","/seat/list/**").hasAnyAuthority("user")
	        .anyRequest().authenticated()
	        .and()
	    .formLogin()
                .successHandler(new AuthSuccessHandler())
                .usernameParameter("username").passwordParameter("password")
	        .loginPage("/login")
	        .loginProcessingUrl("/login")
	        //.failureUrl("/login")
	        .permitAll()
                .and().logout().logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true);
	  }
}
