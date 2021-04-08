package com.dnd.example.springboot.service;

import com.dnd.example.springboot.domain.post.Posts;
import com.dnd.example.springboot.domain.post.PostsRepository;
import com.dnd.example.springboot.web.dto.PostsResponseDto;
import com.dnd.example.springboot.web.dto.PostsSaveRequestDto;
import com.dnd.example.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // JPA 의 엔티티 매니저가 활성화된 상태로(Spring Data Jpa 에서는 default true)
        // 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면
        // 영속성 컨텍스트가 유지된 상태임
        // 이 상태에서 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 반영.
        // 이 개념을 더티 체킹 이라고 함.
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }
}
