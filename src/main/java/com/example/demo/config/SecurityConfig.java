package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true   // @PreAuthorize and @PostAuthorize
        //securedEnabled = true,   // @Secured
        //jsr250Enabled = true   // @RolesAllowed
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    //@Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    /*@Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }*/

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(jwtTokenProvider.bCryptPasswordEncoder().encode("user1Pass")).roles("USER")
                .and()
                .withUser("user2").password(jwtTokenProvider.bCryptPasswordEncoder().encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(jwtTokenProvider.bCryptPasswordEncoder().encode("adminPass")).roles("ADMIN");
    }*/

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring().antMatchers("/");
        //web.ignoring().antMatchers("/", "/index.html", "/app/**", "/authenticate", "/api/reset/*", "/api/reset/**",
        //        "/api/OTPVerification", "/api/OTPVerification/**", "/api/PassChange/**", "/app/images/favicon.ico");
        //web.ignoring().antMatchers("/scripts/**");
        //web.ignoring().antMatchers("/css/**");
        //web.ignoring().antMatchers("/services/**");
        //web.ignoring().antMatchers("/images/**");
        //web.ignoring().antMatchers("/app/images/**");
        //web.ignoring().antMatchers("app/images/**");
        //web.ignoring().antMatchers("/locales/**");
        //web.ignoring().antMatchers("/fonts/**");
        //web.ignoring().antMatchers("/directives/**");
        // web.ignoring().antMatchers("/app/flags/**");
        //web.ignoring().antMatchers("/app/flags/**.png");
        web.ignoring().antMatchers("/**");

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                    //.exceptionHandling()

                //.and()
                    .cors()

                .and()
                    .authorizeRequests()
                        .antMatchers("/api/v1/auth/**")
                        .permitAll()

                    // authenticate all remaining URLS
                    //.anyRequest()
                    //.fullyAuthenticated()

                //.and()
                    //.anonymous()
                    //.authorizeRequests()
                    .anyRequest()
                    .authenticated()

                    /*.antMatchers("/api/v1/admin/**").permitAll()          // увазываем какие url-ы будут доступны всем
                    .antMatchers("/api/v1/auth/**").hasRole("ADMIN")*/

                //.and()
                    // adding JWT filter
                    //.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

                    // access denied page
                    //.exceptionHandling().accessDeniedPage("/access-denied.html").and()
                    //.apply(new JwtConfig(jwtTokenProvider))

                .and()
                    .sessionManagement()
                    .maximumSessions(1)
                .and()
                    .sessionFixation()
                    .migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                    .httpBasic()
                    .disable()
                    .csrf()
                    .disable()
                    .headers()
                    .defaultsDisabled()
                    .frameOptions()
                    .deny()
                    .contentTypeOptions()
                .and()

                .and()
                    .formLogin()
                    .loginPage("/api/v1/auth/login")
                    .permitAll()

                .and()
                    .logout()
                    .permitAll()
                    //.logoutUrl("/api/v1/auth/logout")
                    //.logoutSuccessUrl("/api/v1/auth/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("remove")
                ;
    }


}
