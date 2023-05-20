package com.example.touragency.filters;

import com.example.touragency.utils.TokenCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/reservations/*", "/users/*", "/tours/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String accessToken = getAccessTokenFromCookie(req);
        if (accessToken == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token missing");
            return;
        }

        Jws<Claims> jws = TokenCreator.validateToken(accessToken, "SECRET_KEY");
        if (jws == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token invalid");
            return;
        }

        String userType = (String) jws.getBody().get("userType");
        int userId = Integer.parseInt(jws.getBody().getSubject());
        req.setAttribute("userType", userType);
        req.setAttribute("userId", userId);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getAccessTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
