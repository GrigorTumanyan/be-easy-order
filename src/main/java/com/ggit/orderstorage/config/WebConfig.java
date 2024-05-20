package com.ggit.orderstorage.config;

import com.ggit.orderstorage.service.security.UserDetailService;
import com.ggit.orderstorage.service.security.jwt.JwtTokenFilter;
import com.ggit.orderstorage.service.security.jwt.JwtTokenProvider;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

	@Value("${gmail.password}")
	private String gmailPassword;

	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailService userDetailService;

	public WebConfig(JwtTokenProvider jwtTokenProvider, UserDetailService userDetailService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailService = userDetailService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.headers(header -> header.frameOptions(FrameOptionsConfig::disable))
			.formLogin(Customizer.withDefaults())
			.authorizeHttpRequests(request -> request.requestMatchers("/auth/**", "/h2-console/**").permitAll()
				.anyRequest().authenticated()
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

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setPort(587);
		javaMailSender.setUsername("examplejava07@gmail.com");
		javaMailSender.setPassword(gmailPassword);
		javaMailSender.setJavaMailProperties(props);

		return javaMailSender;
	}


	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
		throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
