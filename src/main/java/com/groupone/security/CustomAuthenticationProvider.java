package com.groupone.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Boolean doPasswordsMatch(String rawPassword,String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("username = " + username);
        System.out.println("password = " + password);

        UserDetails userDetails = null;
        try {
            userDetails = customUserDetailsService.loadUserByUsername(username);
            return checkPassword(userDetails,password);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }

    private Authentication checkPassword(UserDetails user, String rawPassword) {
        System.out.println("rawPassword = " + rawPassword);
        System.out.println("user.getPassword() = " + user.getPassword());

        if (!user.isEnabled()){
            throw new BadCredentialsException("Email is not validated");
        }

        if (doPasswordsMatch(rawPassword, user.getPassword())) {
            User innerUser = new User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );

            return new UsernamePasswordAuthenticationToken(innerUser, user.getPassword(), user.getAuthorities());
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}