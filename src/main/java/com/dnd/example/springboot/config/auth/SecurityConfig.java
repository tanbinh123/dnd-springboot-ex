package com.dnd.example.springboot.config.auth;

import com.dnd.example.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // 스프링 시큐리티 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // h2-console 화면을 사용하기 위해 disable 해야 함
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 disable 해야 함
                .and() // SecurityBuilder 를 리턴, 이전 설정의 disable() 에서 HeadersConfigurer 를 리턴하니 다른 설정을 위해 and() 를 호출하는 것임
                .authorizeRequests() // url 별 권한관리를 설정하는 옵션의 시작점
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest().authenticated() // 위에서 설정된 URL 외의 나머지들
                .and()
                .logout() // 로그아웃시의 설정 시작
                .logoutSuccessUrl("/") // 로그아웃 성공시 / 로 이동
                .and()
                .oauth2Login() // 로그인 설정 시작
                .userInfoEndpoint() // 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                .userService(customOAuth2UserService); // 소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록
    }
}
