package com.example.tokentest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.tokentest.user.UserRepository;

import lombok.RequiredArgsConstructor;


//This class will be holding all the beans that we need to define
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    //This repo will be used for injecting the UserRepository bean in the userdetailservice
    private final UserRepository userRepository;

    //Now we need to create a bean of type userdetailservice
    //This bean will be used by spring security to fetch the user from the database (in the JwtAuthenticationFilter)
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        //This is the the dao which will be fetching the user details, encoding passwords etc and so on
        //We need to create a bean of type AuthenticationProvider
        //WE have many implementations, use the dao auth provider one

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //First one we tell it which user details service to use to fetch user stuff
        //We may have differen userDetailService for different types of users
        authProvider.setUserDetailsService(userDetailsService());
        //Now we provide the password encoder
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //Using this bean for the authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //We need to create a bean of type PasswordEncoder
        //We can use the bcrypt encoder
        return new BCryptPasswordEncoder();
    }
}
