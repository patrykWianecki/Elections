package com.app.security;

import com.app.exceptions.MyException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(SecurityConfig.HEADER_STRING);

        if (StringUtils.isEmpty(header) || !header.startsWith(SecurityConfig.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader(SecurityConfig.HEADER_STRING);
            Optional.ofNullable(token).orElseThrow(() -> new MyException("Token is null"));

            Claims claims = Jwts.parser()
                .setSigningKey(SecurityConfig.SECRET)
                .parseClaimsJws(token.replace(SecurityConfig.TOKEN_PREFIX, ""))
                .getBody();

            String username = claims.getSubject();
            Optional.ofNullable(username).orElseThrow(() -> new MyException("Username is null"));

            List<GrantedAuthority> roles = Arrays
                .stream(claims.get("roles").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(username, null, roles);
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Inappropriate token data");
            } catch (Exception e1) {
                throw new MyException("Failed to send error");
            }
        }
        return null;
    }
}
