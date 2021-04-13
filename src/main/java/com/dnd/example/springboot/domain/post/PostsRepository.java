package com.dnd.example.springboot.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    // 조회는 복잡한 조건 등으로 인해 Entity 클래스만으로 처리하기 어려워
    // 조회용 프레임워크를 추가로 사용함 (querydsl, mybatis 등)
    // 등록/수정/삭제는 SpringDataJpa 를 사용.
    // 저자는 querydsl 을 추천 (쿠팡, 배민 등에서 사용)
    // 테이블, 쿼리 명은 Entity class 와 속성 이름을 써야 한다. (table 의 이름, 속성이 아님)
    // https://kogle.tistory.com/22?category=863426
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
