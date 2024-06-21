package com.ggit.orderstorage.security.jwt;

import com.ggit.orderstorage.data.user.User;
import com.ggit.orderstorage.data.user.UserType;
import com.ggit.orderstorage.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

	private HttpServletRequest request;

	@Value("${jwt.token.secret}")
	private String secret;

	@Value("${jwt.token.expired}")
	private Long validateMilliseconds;

	@Value("${jwt.refreshToken.expired}")
	private Long refreshTokenTime;

	private final UserDetailsService userDetailsService;
	private final UserService userService;

	public JwtTokenProvider(UserDetailsService userDetailsService, UserService userService) {
		this.userDetailsService = userDetailsService;
		this.userService = userService;
	}

	@PostConstruct
	protected void init() {
		secret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
	}

	public List<String> createTokens(String email, UserType userType) {
		List<String> tokens = new ArrayList<>();
		tokens.add(accessToken(email, userType));
		tokens.add(refreshToken(email));
		return tokens;
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getEmail(String token) {
		return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest request) {
		this.request = request;
		String accessToken = request.getHeader("Authorization");
		if (accessToken != null && accessToken.startsWith("Bearer")) {
			return accessToken.substring(7);
		}
		return null;
	}

	public List<String> resolveRefreshToken() {
		String refreshTokenWithBearer = request.getHeader("refreshToken");
		if (refreshTokenWithBearer != null && refreshTokenWithBearer.startsWith("Bearer")) {
			String refreshToken = refreshTokenWithBearer.substring(7);
			String email = getEmail(refreshToken);
			User user = userService.findByEmail(email);
			if (refreshToken.equals(user.getRefreshToken())) {
				try {
					Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(refreshToken);
				} catch (ExpiredJwtException e) {
					throw new RuntimeException("Refresh token is expired;");
				}
				List<String> tokens = createTokens(email, user.getUserType());
				user.setRefreshToken(tokens.get(1));
				userService.save(user);
				return tokens;
			}
		}
		return null;
	}

	public boolean validateToken(String token) {
		if (token != null) {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		}
		return false;
	}

	public HttpServletResponse setResponse(List<String> tokens, HttpServletResponse response) {
		response.setHeader("accessToken", tokens.get(0));
		response.setHeader("refreshToken", tokens.get(1));
		return response;
	}

	private String accessToken(String email, UserType userType) {
		Claims claims = getClaims(email, userType);
		Date currentTime = new Date(System.currentTimeMillis());
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(currentTime)
			.setExpiration(new Date(currentTime.getTime() + validateMilliseconds))//
			.signWith(getKeyForSign())
			.compact();
	}

	private String refreshToken(String email) {
		Claims claims = Jwts.claims().setSubject(email);
		return Jwts.builder()//
			.setClaims(claims)
			.setIssuedAt(new Date())//
			.setExpiration(new Date(System.currentTimeMillis() + refreshTokenTime))//
			.signWith(getKeyForSign())//
			.compact();
	}

	private Key getKeyForSign() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims getClaims(String email, UserType userType) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("userType", userType);
		return claims;
	}

}
