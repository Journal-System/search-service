//package kth.numi.config;
//
//import jakarta.ws.rs.HttpMethod;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfiguration {
//
//    private final JwtAuthConverter jwtAuthConverter;
//
//    @Autowired
//    public SecurityConfiguration(JwtAuthConverter jwtAuthConverter) {
//        this.jwtAuthConverter = jwtAuthConverter;
//    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize ->
//                        authorize
//                                .requestMatchers("/actuator/health")
//                                .permitAll()
//                                // fixes the problem with preflight (401)
//                                .requestMatchers(HttpMethod.OPTIONS, "/**")
//                                .permitAll()
//                                .anyRequest()
//                                .authenticated());
//        http
//                .oauth2ResourceServer(t ->
//                        t.jwt(jwtConfigurer ->
//                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter)));
//        http
//                .sessionManagement(t ->
//                        t.sessionCreationPolicy(STATELESS));
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}