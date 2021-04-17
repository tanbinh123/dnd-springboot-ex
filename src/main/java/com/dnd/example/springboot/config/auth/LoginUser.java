package com.dnd.example.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 메소드 파라미터로 선언된 객체에만 사용 가능으로 지정
@Retention(RetentionPolicy.RUNTIME) // 런타임시에도 메모리에 유지, 어플리케이션 실행 중 값을 가져오려면 RUNTIME 으로 설정해야 함
public @interface LoginUser {
}
