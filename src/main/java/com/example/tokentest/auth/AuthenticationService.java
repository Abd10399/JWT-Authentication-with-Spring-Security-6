package com.example.tokentest.auth;

import com.example.tokentest.user.User;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tokentest.config.JwtService;
import com.example.tokentest.user.Role;
import com.example.tokentest.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.var;

//Where the register and authenticate methods will be implemented
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    //Injecting repository to interact with the database
    private final UserRepository repository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("User not authenticated");
        System.out.println("User email is " + request.getEmail());
        System.out.println("User password is " + request.getPassword());

        //Good uptill here
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        //TODO: FIX THIS, program doesn't reach here
        System.out.println("User authenticated");
        //If here, user is authenticated, ie username and password are both correct

        
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }
}
