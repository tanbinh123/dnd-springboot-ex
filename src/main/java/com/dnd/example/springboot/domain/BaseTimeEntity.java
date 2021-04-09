package com.dnd.example.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 상속될 경우 필드들이 컬럼으로 인식되도록 함
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity 에 Auditing 기능을 포함시킴
public class BaseTimeEntity {

    @CreatedDate // Entity 가 생성되어 저장될 때 시간이 자동으로 저장
    private LocalDateTime createdDate;

    @LastModifiedDate // 조회한 Entity 의 값을 변경할 때 시간이 자동으로 저장
    private LocalDateTime modifiedDate;
}
