package com.example.spring_security_with_jwt.config;

import com.example.spring_security_with_jwt.filter.JwtAuthenticationFilter;
import com.example.spring_security_with_jwt.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final MyUserDetailService myUserDetailService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoderConfig bCryptPasswordEncoder;

    public SecurityConfiguration(MyUserDetailService myUserDetailService, JwtAuthenticationFilter jwtAuthenticationFilter, PasswordEncoderConfig bCryptPasswordEncoder) {
        this.myUserDetailService = myUserDetailService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/error/**"
                    , "/logout", "/", "/home", "/register").permitAll();
            authorize.requestMatchers("/api/auth/login").permitAll();
            authorize.requestMatchers("/admin/**").hasRole("ADMIN");
            authorize.requestMatchers("/user/**").hasRole("USER");
            authorize.anyRequest().authenticated();
        }).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()).build();
    }

    @Bean
    public UserDetailsService userDetailService() {
        return myUserDetailService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder.passwordEncoder());
        return daoAuthenticationProvider;
    }

}
