package com.TiendaEnLinea.TiendaEnLinea.config;


import com.TiendaEnLinea.TiendaEnLinea.services.CustomDetailService;
import com.TiendaEnLinea.TiendaEnLinea.utils.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomDetailService customDetailService;
    private final JwtFilter jwtFilter;


    public SecurityConfig(CustomDetailService customDetailService, JwtFilter jwtFilter) {
        this.customDetailService = customDetailService;
        this.jwtFilter = jwtFilter;
    }
    //METODO PARA ENCRIPTAR CONTRASEÑAS

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    proveedor de seguridad
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());


        return authenticationProvider;
    }

        /*

        FILTROS DE SEGURIDAD
         */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/usuarios/register").permitAll()
                        .requestMatchers("/usuarios/all", "/usuarios/*").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/updateDate/*").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/updateRoles").hasAnyAuthority("ROLE_ADMIN")

                        //ENPOINT PARA PRODUCTOS
                        .requestMatchers("/productos/addProducts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/productos/allproducts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/productos/product/*").permitAll()


                        //PROTECCION DE ENPOINT PARA ORDENES
                        .requestMatchers(HttpMethod.POST, "/order/*/paid").authenticated()


                        .anyRequest().authenticated()
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
