package com.dnd.example.springboot.web;

import com.dnd.example.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
// @WebMvcTest 사용시 에러 케이스
// 1.
// @WebMvcTest 는 @Controller 를 스캔함
// @Repository, @Service, @Component 는 스캔하지 않음
// SecurityConfig 는 읽었으나 CustomOAuth2UserService 는 읽을 수가 없어서 테스트 케이스에서 에러가 발생함
// 스캔 대상에서 SecurityConfig 를 제거하자
// -> 시큐리티는 적용되는데, 관련 설정만 뺀 것임
// -> MockMvc 사용시 ROLE 상관없이 @WithMockUser 만 적용되면 통신 성공함
// 2.
// @EnableJpaAuditing 을 사용하기 위해선 최소 하나의 @Entity 클래스가 필요함
// @WebMvcTest 에서는 @Entity 를 스캔하지 않음
// @WebMvcTest 는 @EnableJpaAuditing 를 사용할 필요가 없는데 스캔되었음
// -> 왜? @SpringBootApplication 와 함께 사용해서..
// -> @EnableJpaAuditing 가 스캔되지 않도록 @SpringBootApplication 와 분리하자
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    public void hello_should_return() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    @WithMockUser
    public void hello_dto_should_return() throws Exception {
        String name = "TEST";
        int amount = 5000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}
