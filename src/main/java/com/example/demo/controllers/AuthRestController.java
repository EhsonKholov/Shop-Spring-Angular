package com.example.demo.controllers;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;




    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody AuthDto authDto) {
        System.out.println(authDto.toString());
        try {
            String username = authDto.getUserName();

            //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authDto.getPassword()));

            User user = userService.findByUserName(username);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            System.out.println(response);

            return new ResponseEntity(response, HttpStatus.OK);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<?> registration(@Validated @RequestBody UserDto userDto, HttpServletRequest request, Errors errors) {

        if (userService.emailExists(userDto.getEmail())) {
            return new ResponseEntity<String>("There is an account exist with that email address: "
                    + userDto.getEmail(), HttpStatus.CONFLICT);
        }
        if (userService.userNameExists(userDto.getUserName())) {
            return new ResponseEntity<String>("There is an account with that username: "
                    + userDto.getUserName(), HttpStatus.CONFLICT);
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return new ResponseEntity<String>("Password and confirm password do not match", HttpStatus.BAD_REQUEST);
        }

        User user = userService.createUser(userDto);

        return new ResponseEntity<String>("User successfully registered", HttpStatus.OK);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<Boolean> logout(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
        System.out.println(session.toString());
        session.invalidate();

        return new ResponseEntity(true, HttpStatus.OK);
    }

    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public ResponseEntity<String> accessDenied() {
        return new ResponseEntity<>("access-denied", HttpStatus.FORBIDDEN);
    }
}
