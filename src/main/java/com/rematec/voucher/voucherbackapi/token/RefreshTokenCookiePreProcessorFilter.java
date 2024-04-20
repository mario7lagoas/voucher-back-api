package com.rematec.voucher.voucherbackapi.token;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {



        HttpServletRequest req = (HttpServletRequest)request;

        if ("/voucher-back/v1/login/refresh".equalsIgnoreCase(req.getRequestURI())
                && "refresh_token".equals(req.getParameter("grant_type"))
                && req.getCookies() != null) {

            boolean checkCookieAdd = false;
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("RefreshToken") && checkCookieAdd == false) {
                    System.out.println("cooke nome -> " + cookie.getName() );
                    String refreshToken = cookie.getValue();
                    System.out.println("Componente -> " + refreshToken);
                    req = new MyServLetRequestWrapper(req, refreshToken);
                    checkCookieAdd = true;
                }

            }

        }

        chain.doFilter(req, response);

    }

    static class MyServLetRequestWrapper extends HttpServletRequestWrapper {

        private String refreshToken;

        public MyServLetRequestWrapper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put("RefreshToken", new String[]{refreshToken});
            map.setLocked(true);
            return map;
        }

    }

    @Override
    public void destroy() {

    }
}
