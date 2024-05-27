package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurity {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final Environment env;
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager= authenticationManagerBuilder.build();
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(form->form
                .loginPage("/login")
                .permitAll());
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/**").permitAll()
        );
        http.authenticationManager(authenticationManager);
        http.addFilter(getAuthenticationFilter(authenticationManager));

        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }
    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws  Exception{
        return new AuthenticationFilter(userService,authenticationManager,env);
    }

}
