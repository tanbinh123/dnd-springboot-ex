package com.dnd.example.springboot.web;

import com.dnd.example.springboot.config.auth.dto.SessionUser;
import com.dnd.example.springboot.service.PostsService;
import com.dnd.example.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
// 화면이동을 담당하는 컨트롤러
// mustache 파일을 연결시켜주는 역할 -> String 을 리턴(mustache 경로)
// 그래서 @RestController 가 아닌 @Controller 를 사용 
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {

        // 서버 템플릿엔진에서 사용할 수 있는 객체를 저장
        model.addAttribute("posts", postsService.fndAllDesc());

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        // mustache starter 가
        // controller 에서 문자열을 반환하면
        // 앞의 경로와 뒤의 확장자를 자동으로 지정한다.
        // 앞 : src/main/resources/templates
        // 뒤 : .mustache
        // 따라서 src/main/resources/templates/index.mustache 로 전환되어 view resolver 가 처리
        return "index";
    }

    @GetMapping("/posts/save")
    public String register() {

        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String update(
            @PathVariable Long id,
            Model model
    ) {
        PostsResponseDto dto = postsService.findById(id);

        model.addAttribute("post", dto);

        return "posts-update";
    }
}
