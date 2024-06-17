package com.ggit.orderstorage.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
		throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
		List<String> updateTokens = null;
		boolean isValidToken = false;
		if (token != null) {
			try {
				isValidToken = jwtTokenProvider.validateToken(token);
			} catch (ExpiredJwtException e) {
				updateTokens = jwtTokenProvider.resolveRefreshToken();
				if (updateTokens != null){
					isValidToken = true;
					token = updateTokens.get(0);
				}
			}
			if (isValidToken) {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				if (authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
			if (updateTokens != null && updateTokens.size() == 2) {
				HttpServletResponse response = jwtTokenProvider.setResponse(updateTokens,
					(HttpServletResponse) servletResponse);
				filterChain.doFilter(servletRequest, response);
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
