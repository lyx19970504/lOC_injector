package com.example.mylibrary;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 将事件进行拦截并执行自定义的方法
 */
public class MyInvocationHandler implements InvocationHandler {

    private static final String TAG = "MyInvocationHandler";
    //拦截对象
    private Object target;
    //需要拦截的方法
    private Map<String,Method> map;

    public MyInvocationHandler(Object target) {
        this.target = target;
        map = new HashMap<>();
    }

    //invoke方法匹配接口中的所有方法
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        String name = method.getName();  //这里的method是onClick
        method = map.get(name);  //当onClick方法被调用时，将method替换成自定义方法

        //此时的method已经是自定义方法了
        if(method != null){
            if(method.getGenericParameterTypes().length == 0) {
                return method.invoke(target);
            }
            return method.invoke(target,objects);
        }
        return null;
    }


    //添加拦截的方法
    public void addMethod(String methodName,Method method){
        map.put(methodName,method);

    }
}
