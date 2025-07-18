package com.plantastic.backend.controller;


import com.plantastic.backend.models.auth.AuthenticationRequest;
import com.plantastic.backend.models.auth.AuthenticationResponse;
import com.plantastic.backend.security.JwtUtil;
import com.plantastic.backend.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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
    public AuthenticationResponse createAuthenticationResponse(@RequestBody AuthenticationRequest authRequest) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(
                    //@Todo voir comment ça se passe avec un email
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(jwt);
    }
}
