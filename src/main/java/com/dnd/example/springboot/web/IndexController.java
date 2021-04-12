package com.dnd.example.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
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
}
