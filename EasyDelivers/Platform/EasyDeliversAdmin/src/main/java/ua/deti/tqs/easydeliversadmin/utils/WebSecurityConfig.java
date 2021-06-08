package ua.deti.tqs.easydeliversadmin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ua.deti.tqs.easydeliversadmin.service.AdminDetailsService;
import ua.deti.tqs.easydeliversadmin.service.EasyDeliversService;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = Logger.getLogger(WebSecurityConfig.class.getName());

    @Autowired
    AdminDetailsService adminDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(adminDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        logger.info("request has been made");
        http
                .authorizeRequests()
                .antMatchers("/dashboard").hasAnyRole("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncryption getPasswordEncoder() {
        return new PasswordEncryption();
    }
}