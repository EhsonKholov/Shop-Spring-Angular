package com.example.demo.controller;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth/")
public class AuthRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailService;


    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public ResponseEntity<?> SignIn(@RequestBody @Valid AuthDto authDto) {

        if (authDto.getEmail() == null || authDto.getPassword() == null) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByEmail(authDto.getEmail());

        if (user == null) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.BAD_REQUEST);
        }

        String token = jwtUtil.createToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail(), user.getRoles());
        List<String> roles = jwtUtil.getRoleNames(user.getRoles());

        Map<Object, Object> resp = new HashMap<>();
        resp.put("userId", user.getUserId());
        resp.put("firstname", user.getFirstName());
        resp.put("lastname", user.getLastName());
        resp.put("email", user.getEmail());
        resp.put("roles", roles);
        resp.put("access_token", token);
        resp.put("refresh_token", refreshToken);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "refreshtoken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> RefreshToken(@RequestBody String refresh_token, HttpServletRequest req) {

        if (refresh_token != null && !jwtUtil.validRefreshToken(refresh_token)) {
            return new ResponseEntity<>("JWT refresh token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtUtil.getEmailFromRefreshToken(refresh_token);

        if (email == null) {
            return new ResponseEntity<>("JWT refresh token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByEmail(email);

        UserDetails userDetails = userDetailService.loadUserByUsername(email);

        if (!email.equals(userDetails.getUsername())) {
            return new ResponseEntity<>("JWT refresh token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


        String access_token = jwtUtil.createToken(user.getEmail(), user.getRoles());

        Map<Object, Object> resp = new HashMap<>();
        resp.put("access_token", access_token);
        resp.put("refresh_token", refresh_token);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}
