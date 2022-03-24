package com.example.demo.security.jwt;

import com.example.demo.service.UserDetailService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;


@Slf4j
//@ComponentScan("com.example.demo.repository")
@Component
public class JwtFilter extends GenericFilterBean {

    @Value("${jwt.token.secret}")
    private String secret;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String token = req.getHeader("Authorization");

        /*if (token == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authorization header needed");
            return;
        }*/

        if (token != null) {

            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
                if (claims.getBody().getExpiration().before(new Date())) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired or invalid");
                    return;
                }

                servletRequest.setAttribute("claims", claims);
                String username = claims.getBody().getSubject();

                String token_type = claims.getBody().get("token_type").toString();

                if (!token_type.equals("access_token")) {
                    ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired or invalid");
                    return;
                }

                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                if (!username.equals(userDetails.getUsername())) {
                    ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired or invalid");
                    return;
                }

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


            } catch (ExpiredJwtException e) {

                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired or invalid");
                return;

            }/* catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }*/

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
