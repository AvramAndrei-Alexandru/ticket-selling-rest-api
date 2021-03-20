package com.andrei.ticket_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public SecurityConfig(@Autowired DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select login, password, 'true' as enabled from user where login=?")
                .authoritiesByUsernameQuery("select login, role from role where login=?");
    }


    @Override//for access to site config
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/api/users/*").hasAnyRole("ADMIN")
                .antMatchers("/api/concert/getAll").hasAnyRole("ADMIN", "CASHIER")
                .antMatchers("/api/concert/createConcert").hasAnyRole("ADMIN")
                .antMatchers("/api/concert/deleteConcert/*").hasAnyRole("ADMIN")
                .antMatchers("/api/concert/editConcert/*").hasAnyRole("ADMIN")
                .antMatchers("/api/tickets/getAll").hasAnyRole("ADMIN","CASHIER")
                .antMatchers("/api/tickets/getAllTicketsForConcert/*").hasAnyRole("ADMIN","CASHIER")
                .antMatchers("/api/tickets/sellTicket").hasAnyRole("ADMIN","CASHIER")
                .antMatchers("/api/tickets/editTicket/*").hasAnyRole("ADMIN","CASHIER")
                .antMatchers("/api/tickets/deleteTicket/*").hasAnyRole("ADMIN","CASHIER")
                .antMatchers("/api/tickets/export/*").hasAnyRole("ADMIN")
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/api/greetings")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
        http.csrf().disable();

    }


}
