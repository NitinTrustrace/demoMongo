package com.example.demo.service;

import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exceptions.SpringRedditException;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerficationTokenRepository;
import com.example.demo.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private  PasswordEncoder passwordEncoder;
    private  UserRepository userRepository;
    private  VerficationTokenRepository verficationTokenRepository;
    private  AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private RefreshTokenService refreshTokenService;
    private final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

       String token =  generateVerificatonToken(user);
       System.out.println(token+"==========signuptokr");

        System.out.println("http://localhost:8080/api/auth/accountVerification/" + token);

        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    private String generateVerificatonToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        System.out.println(user+"=======usergenerateVerificatonToken");
        verificationToken.setUser(user);

        verficationTokenRepository.save(verificationToken);
        return token;
    }


    public void verifyAccount(String token) {
       Optional<VerificationToken> verificationToken =  verficationTokenRepository.findByToken(token);
       System.out.println(verificationToken+"=============verificationTokenverificationToken11");
       verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
       System.out.println(verificationToken.get()+"==========gertrrrrrrrrrrr");
       fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        System.out.println(username+"======usernameusername11");
       User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("user not found with name - " +username));
       System.out.println(user+"============useruseruser112");
       user.setEnabled(true);
       userRepository.save(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        System.out.println("Loginnnnnnnnnnnnnnnnn");
        System.out.println(loginRequest.getUsername()+"========userrr");
        System.out.println(loginRequest.getPassword()+"========passsssss");
      Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
      System.out.println(authenticate+"=========authenticateeeeeeee2");
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        System.out.println(token+"=========tokentoken1111");
       /* System.out.println( AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build()+"==================retrunnnn");*/
        System.out.println(token+"=========tokentoken");
        System.out.println(refreshTokenService.generateRefreshToken().getToken()+"=========tokentoken1111111111");
        System.out.println(jwtProvider.getJwtExpirationInMillis()+"=========tokentoken2222222222222");
        System.out.println(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis())+"=========tokentoken3333333333");
        System.out.println(loginRequest.getUsername()+"====token444444444444");


        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        System.out.println("Rrrrrrrefreshhhhhhhhhhhhhhhhhhhhhhhhhhh");
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
