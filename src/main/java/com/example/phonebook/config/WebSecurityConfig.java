package com.example.phonebook.config;

import com.example.phonebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
                        //WebSecurityConfig class annotated with @EnableWebSecurity,
                        // to enable Spring Security web security support and provide Spring MVC integration.

public class WebSecurityConfig extends WebSecurityConfigurerAdapter

        // It also extends WebSecurityConfigurerAdapter and overrides several of its methods,
        // to set some web security configuration details.
{
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserService userService;

    //The configure (HttpSecurity) method determines which URL paths should be secured and which should not.
    // All other paths must be authenticated.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // In particular, the /signin, /signup, /static/** and "/activate/*" paths are set to not require authentication.
                // All other paths must be authenticated
                .authorizeRequests()
                .antMatchers("/signin",
                        "/signup",
                        "/static/**",
                        "/activate/*"
                ).permitAll()
                .anyRequest().authenticated()
            .and()
                // When the user successfully logs in, he is redirected to the previously requested page,
                // requiring authentication. There is a user page /signin (which is specified in loginPage()),
                // and everyone is allowed to view it.
                .formLogin()
                .loginPage("/signin")
                .permitAll()
            .and()
                .logout()
                .logoutSuccessUrl("/signin")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}