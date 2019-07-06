package com.example.mylibrary;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件总类，是注解的注解
 */

@Target(ElementType.ANNOTATION_TYPE)   //这是一个注解的注解
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {

    String listenerSetter();                   //对应于setOn...Listener
    Class<?> listenerType();                   //对应于new On...Listener
    String callBackListener();                     //对应于接口实现方法,如:onClick
}
