package com.ggit.orderstorage.config;

import com.ggit.orderstorage.service.security.jwt.JwtTokenFilter;
import com.ggit.orderstorage.service.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebConfig {

	private final JwtTokenProvider jwtTokenProvider;

	public WebConfig(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.headers(header -> header.frameOptions(FrameOptionsConfig::disable))
			.formLogin(Customizer.withDefaults())
			.authorizeHttpRequests(request -> request.requestMatchers("/**").permitAll()
//				.anyRequest().authenticated()
			)
			.logout(LogoutConfigurer::permitAll)
			.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JavaMailSenderImpl mailSender() {
		var javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setProtocol("SMTP");
		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setPort(587);
		return javaMailSender;

	}
}
