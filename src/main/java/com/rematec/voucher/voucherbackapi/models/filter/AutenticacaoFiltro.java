package com.rematec.voucher.voucherbackapi.models.filter;


import com.rematec.voucher.voucherbackapi.services.AutenticacaoService;
import com.rematec.voucher.voucherbackapi.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class AutenticacaoFiltro extends BasicAuthenticationFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    private JWTUtil jwtUtil;


    public AutenticacaoFiltro(AuthenticationManager authenticationManager,
                              JWTUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //TODO melhorar isso aqui
        if (!"/voucher-back/v1/login/refresh".equals(request.getRequestURI())
                && !"refresh_token".equals(request.getParameter("grant_type"))) {
            if (!"/voucher-back/v1/voucher".equals(request.getRequestURI()) &&
                    !"/voucher-back/v1/voucher/cancel".equals(request.getRequestURI()) &&
                    !"/voucher-back/v1/voucher/confirm".equals(request.getRequestURI()) &&
                    !"/voucher-back/v1/voucher/resgate".equals(request.getRequestURI()) &&
                    !"/voucher-back/v1/voucher/consumer".equals(request.getRequestURI()) &&
                    !"/voucher-back/v1/voucher/rollback".equals(request.getRequestURI())) {

                Authentication authentication = AutenticacaoService.obterAutenticacao(request, response);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        chain.doFilter(request, response);

    }

}
