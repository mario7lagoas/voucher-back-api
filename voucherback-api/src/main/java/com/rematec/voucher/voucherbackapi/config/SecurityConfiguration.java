package com.rematec.voucher.voucherbackapi.config;


import com.rematec.voucher.voucherbackapi.filter.AutenticacaoFiltro;
import com.rematec.voucher.voucherbackapi.filter.LoginFiltro;
import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import com.rematec.voucher.voucherbackapi.services.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/swagger-ui.html","/swagger-ui/**",
                                    "/v3/api-docs/**" ).permitAll()
                            .requestMatchers(HttpMethod.GET, "/usuario", "/loja","/promocao")
                            .hasAnyAuthority(PermissaoEnum.USUARIO.getRole())
                            .requestMatchers(HttpMethod.POST, "/usuario", "/loja", "/promocao")
                            .hasAnyAuthority(PermissaoEnum.ADMINISTRADOR.getRole())
                            .requestMatchers(HttpMethod.POST, "/usuario", "/loja", "/promocao")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole())
                            .anyRequest().authenticated();
                });

        http.addFilterBefore(new LoginFiltro("/login", authenticationConfiguration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new AutenticacaoFiltro(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
