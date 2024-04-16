package com.rematec.voucher.voucherbackapi.config;


import com.rematec.voucher.voucherbackapi.models.filter.AutenticacaoFiltro;
import com.rematec.voucher.voucherbackapi.models.filter.LoginFiltro;
import com.rematec.voucher.voucherbackapi.interfaces.repositories.IUsuarioRepository;
import com.rematec.voucher.voucherbackapi.models.enums.PermissaoEnum;
import com.rematec.voucher.voucherbackapi.services.UsuarioAutenticadoService;
import com.rematec.voucher.voucherbackapi.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/login",
            "/login/refresh",
            "/login/revoke",
            "/voucher",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PUBLIC_MATCHERS).permitAll()
                            .requestMatchers(HttpMethod.GET, "/loja")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.BUSCAR_LOJA.getRole())
                            .requestMatchers(HttpMethod.GET, "/usuario")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.BUSCAR_USUARIO.getRole())
                            .requestMatchers(HttpMethod.GET, "/promocao")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.BUSCAR_PROMOCAO.getRole())
                            .requestMatchers(HttpMethod.GET, "/perfil")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.BUSCAR_PERFIL.getRole())


                            .requestMatchers(HttpMethod.PUT, "/loja")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.ALTERAR_LOJA.getRole())
                            .requestMatchers(HttpMethod.PUT, "/usuario")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.ALTERAR_USUARIO.getRole())
                            .requestMatchers(HttpMethod.PUT, "/promocao")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.ALTERAR_PROMOCAO.getRole())
                            .requestMatchers(HttpMethod.PUT, "/perfil")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.ALTERAR_PERFIL.getRole())


                            .requestMatchers(HttpMethod.POST, "/loja")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.CADASTRAR_LOJA.getRole())
                            .requestMatchers(HttpMethod.POST, "/usuario")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.CADASTRAR_USUARIO.getRole())
                            .requestMatchers(HttpMethod.POST, "/promocao")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.CADASTRAR_PROMOCAO.getRole())
                            .requestMatchers(HttpMethod.POST, "/perfil")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.CADASTRAR_PERFIL.getRole())


                            .requestMatchers(HttpMethod.DELETE, "/loja")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.APAGAR_LOJA.getRole())
                            .requestMatchers(HttpMethod.DELETE, "/usuario")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.APAGAR_USUARIO.getRole())
                            .requestMatchers(HttpMethod.DELETE, "/promocao")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.APAGAR_PROMOCAO.getRole())
                            .requestMatchers(HttpMethod.DELETE, "/perfil")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.APAGAR_PERFIL.getRole())

                            .requestMatchers(HttpMethod.PATCH, "/loja")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.ALTERAR_LOJA.getRole())
                            .requestMatchers(HttpMethod.PATCH, "/usuario")
                            .hasAnyAuthority(PermissaoEnum.MODERADOR.getRole(), PermissaoEnum.ALTERAR_USUARIO.getRole())

                            .requestMatchers(HttpMethod.GET, "/usuario", "/loja", "/promocao")
                            .hasAnyAuthority(PermissaoEnum.USUARIO.getRole(), PermissaoEnum.ADMINISTRADOR.getRole())
                            .requestMatchers(HttpMethod.POST, "/usuario", "/loja", "/promocao","/perfil")
                            .hasAnyAuthority(PermissaoEnum.ADMINISTRADOR.getRole())
                            .requestMatchers(HttpMethod.DELETE, "/usuario", "/loja", "/promocao","/perfil")
                            .hasAnyAuthority(PermissaoEnum.ADMINISTRADOR.getRole())
                            .requestMatchers(HttpMethod.PUT, "/usuario", "/loja", "/promocao","/perfil")
                            .hasAnyAuthority(PermissaoEnum.ADMINISTRADOR.getRole())

                            .anyRequest().authenticated();

                });

        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
       //http.httpBasic(Customizer.withDefaults());

        http.addFilterBefore(new LoginFiltro("/login", authenticationConfiguration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new AutenticacaoFiltro(authenticationManager(authenticationConfiguration), jwtUtil ), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
