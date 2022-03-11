package com.example.demo.security.jwt;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUserDetailsService;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;


    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, JwtAuthenticationException {

        String token = ((HttpServletRequest) servletRequest).getHeader("Authentication");

        if (userRepository == null) {

            ServletContext servletContext = servletRequest.getServletContext();

            WebApplicationContext webApplicationContext = WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
            userRepository = webApplicationContext.getBean(UserRepository.class);

        }

        //if (token == null || !token.startsWith("Bearer "))
            //((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
        if (token != null && token.startsWith("Bearer ")) {

            token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

            if (token != null && jwtTokenProvider.validateToken(token, (HttpServletResponse) servletResponse)) {

                Authentication authentication = jwtTokenProvider.authentication(token);

                if (authentication != null) {
                    // Аутентифицируем запрос
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                SecurityContextHolder.clearContext();
                throw new JwtAuthenticationException("JWT token is expired or invalid");
            }

            /*if (SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                    && ((HttpServletRequest)request).getRequestURI().equals("/login")) {
                System.out.println("user is authenticated but trying to access login page, redirecting to /");
                ((HttpServletResponse)response).sendRedirect("/");
            }*/
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }


}
