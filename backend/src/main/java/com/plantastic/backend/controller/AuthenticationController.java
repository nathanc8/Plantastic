package com.example.test_auth.controller;

import com.example.test_auth.model.AuthenticationRequest;
import com.example.test_auth.model.AuthenticationResponse;
import com.example.test_auth.security.JwtUtil;
import com.example.test_auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public String root() {
        return "Bienvenue sur l'API !";
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationResponse(@RequestBody AuthenticationRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    //@Todo voir comment ça se passe avec un email
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(jwt);
    }

    @GetMapping("/test")
    public String testMapping() {
        System.out.println();
        return "ça marche toujours";
    }
}
