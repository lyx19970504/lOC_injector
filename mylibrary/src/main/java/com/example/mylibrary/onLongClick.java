package com.example.mylibrary;


import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@EventBase(listenerSetter = "setOnLongClickListener",listenerType = View.OnLongClickListener.class,callBackListener = "onLongClick")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface onLongClick {

    int[] value();
}
