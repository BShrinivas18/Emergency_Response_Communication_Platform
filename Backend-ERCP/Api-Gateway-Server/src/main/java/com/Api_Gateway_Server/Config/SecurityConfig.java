package com.Api_Gateway_Server.Config;

import com.Api_Gateway_Server.Service.MyUserDetailsService;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private ResponderAccess responderAccess;
    @Value("${security.cors.origins:http://localhost:4200}")
    private String origins;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request->request
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
//                        .requestMatchers("/responders/create/**","/responders/update","/responders/delete","/responders/getAll", "/incidents/{id}").hasRole("ADMIN")
//                        .requestMatchers(("/responders/request-additional/{id}/{type}")).hasRole("RESPONDER")
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/incidents/create").permitAll()
                        .requestMatchers("/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/responders/{id}/history").access((auth, context) ->
                            new org.springframework.security.authorization.AuthorizationDecision(responderAccess.owns(auth.get(), context.getVariables().get("id"))))
                        .requestMatchers(HttpMethod.GET, "/incidents/**", "/responders/**", "/logs/**", "/locations/**").hasAnyRole("ADMIN", "RESPONDER")
                        .requestMatchers(HttpMethod.PATCH, "/responders/{id}/{status}").access((auth, context) ->
                            new org.springframework.security.authorization.AuthorizationDecision(responderAccess.owns(auth.get(), context.getVariables().get("id"))))
                        .requestMatchers(HttpMethod.PUT, "/responders/request-additional/{id}/{type}").access((auth, context) ->
                            new org.springframework.security.authorization.AuthorizationDecision(responderAccess.assignedTo(auth.get(), context.getVariables().get("id"))))
                        .requestMatchers("/responders/assign-responder").denyAll()
                        .requestMatchers(HttpMethod.POST, "/logs/**").denyAll()
                        .requestMatchers(HttpMethod.PUT, "/logs/**").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/logs/**").denyAll()
                        .anyRequest().hasRole("ADMIN"))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new TrustedActorFilter(), org.springframework.security.web.authentication.AnonymousAuthenticationFilter.class)
//                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        List<String> allowed = Arrays.stream(origins.split(",")).map(String::trim).toList();
        if (allowed.stream().anyMatch(o -> o.isBlank() || o.contains("*"))) {
            throw new IllegalArgumentException("Explicit CORS origins required");
        }
        config.setAllowCredentials(false);
        config.setAllowedOrigins(allowed);
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // You can also use other encoders, but BCrypt is the most commonly used.
    }


}
