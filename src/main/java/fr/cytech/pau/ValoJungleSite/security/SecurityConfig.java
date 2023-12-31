package fr.cytech.pau.ValoJungleSite.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                /*
                // Pour retirer les authentifications sur tout le site pour le test,
                // décommenter ce bloc et commenter les autres routes
                .requestMatchers(
                        new AntPathRequestMatcher("/**")
                ).permitAll()
                */

                ///*
                .requestMatchers(
                        new AntPathRequestMatcher("/h2-console/**")
                ).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/")
                ).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/admin/**")
                ).hasRole("admin")
                .requestMatchers(
                        new AntPathRequestMatcher("/player/**")
                ).hasRole("player")
                .requestMatchers(
                        new AntPathRequestMatcher("/img/**")
                ).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/css/**")
                ).permitAll()
                .requestMatchers(
                        new AntPathRequestMatcher("/register")
                ).permitAll()
                //*/
                .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        http
                .logout((logout) -> logout.logoutSuccessUrl("/"));

        http.headers((headers) -> headers
                .frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                )
        );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

}
