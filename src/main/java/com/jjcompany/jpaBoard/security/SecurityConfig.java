package com.jjcompany.jpaBoard.security;

import org.springframework.boot.autoconfigure.security.servlet.AntPathRequestMatcherProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 해당클래스가 스프링 환경설정 클래스임을 알림
@EnableWebSecurity// 모든 웹에 대한 요청이 스프링 시큐리티의 컨트롤 하에 있음을 알림
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers(
				new AntPathRequestMatcher("/**")).permitAll()
			
			//로그인과 로그아웃 관련 설정
			.and() // 로그인 설정
				.formLogin()
				.loginPage("/login")//로그인 페이지가 보이게하는 요청
				.defaultSuccessUrl("/") //로그인 성공시 이동할 페이지 요청
			.and() //로그아웃 설정
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//로그아웃 요청
				.logoutSuccessUrl("/") // 로그아웃 성공시에 이동할 페이지 요청
				.invalidateHttpSession(true); // 세션삭제 -> 로그아웃
		
		;
		return http.build();
	}
	
	
}
