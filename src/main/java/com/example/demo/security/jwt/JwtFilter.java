package com.example.demo.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    JwtTokenProvider jwtTokenProvider;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = ((HttpServletRequest) servletRequest).getHeader("Authentication");
        if (token == null || !token.startsWith("Bearer ")) {
            ((HttpServletResponse) servletRequest).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
        } else {

            token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.authentication(token);

                if (authentication != null) {
                    // Аутентифицируем запрос
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
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
