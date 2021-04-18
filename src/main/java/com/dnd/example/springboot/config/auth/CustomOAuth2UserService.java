package com.dnd.example.springboot.config.auth;

import com.dnd.example.springboot.config.auth.dto.OAuthAttributes;
import com.dnd.example.springboot.config.auth.dto.SessionUser;
import com.dnd.example.springboot.domain.user.User;
import com.dnd.example.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        // 스프링 시큐리티의 OAuth2 를 사용했을 때
        // 인증 결과로 리턴되는 클래스
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        // 구글만 사용 (구글의 경우 : "google")
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드값
        // 구글만 지원 (구글의 기본 코드 : "sub")
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService 를 통해 가져온 OAuth2User 의 attribute 를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // TODO 사용자가 필수 제공 항목(email 등)을 제공하지 않을 경우,
        // DB 에러가 발생하여 로그인 처리가 불가능한 상태가 됨
        // 하지만 provider 에게는 동의를 완료한 상태라 필수 제공 항목을 수정할 수가 없음
        // 이를 원복하려면 사용자가 동의철회를 해야 함
        // 동의철회는 Naver, Google 등에 직접 들어가서 하는 수 밖에 없음
        User user = saveOrUpdate(attributes);

        // SessionUser : 세션에 사용자 정보를 저장하기 위한 Dto 클래스
        // 세션 저장소 개선 방안 (2대 이상 서버 고려)
        // 1. 데이터 베이스에 저장 (성능 이슈)
        // 2. Redis 에 저장 (별도 시스템 필요)
        // 3. 쿠키에 저장 (보안 이슈)
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
