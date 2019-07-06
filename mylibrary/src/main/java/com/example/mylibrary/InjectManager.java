package com.example.mylibrary;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * 该类负责对Activity的布局，控件，事件进行注入
 */
public class InjectManager {

    private static final String TAG = "InjectManager";
    
    public static void injectInto(Activity activity){
        //对布局进行注入
        injectLayout(activity);

        //对控件进行注入
        injectViews(activity);

        //对事件进行注入
        injectEvents(activity);
    }


    private static void injectLayout(Activity activity) {
        //获取Activity类
        Class<? extends Activity> clazz = activity.getClass();
        //获取Activity类上的ContentView注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView != null){
            int layoutId = contentView.value(); //获取属性值，布局ID

            //采用setContentView的方法
            //activity.setContentView(layoutId);


            //采用反射的方法
            try {
                Method method = clazz.getMethod("setContentView",int.class); //获取setContentView方法
                method.invoke(activity,layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectViews(Activity activity) {
        //获取Activity类
        Class<? extends Activity> clazz = activity.getClass();
        //获取Activity类中所有的参数
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            InjectView annotation = field.getAnnotation(InjectView.class);

            //得到有InjectView注解的参数
            if(annotation != null){
                int viewId = annotation.value();//获取属性值，控件ID

                try {
                    Method method = clazz.getMethod("findViewById", int.class); //获取父类的findViewById方法
                    Object view = method.invoke(activity, viewId);  // 调用并返回findViewById方法返回值
                    field.setAccessible(true);  //设置可访问私有属性
                    field.set(activity,view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            //获取每个方法的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //获取每个方法的注解的注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if(annotationType != null){
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    //判断是否是有EventBase注解的注解
                    if(eventBase != null){
                        //获取EventBase的三个参数
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();
                        String callBackListener = eventBase.callBackListener();

                        //使用代理完成事件的拦截、修改
                        MyInvocationHandler handler = new MyInvocationHandler(activity);
                        handler.addMethod(callBackListener,method);
                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),new Class[]{listenerType},handler);


                        try {
                            //通过注解类型，获取onClick注解的value值
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);
                            if(viewIds != null) {
                                for (int viewId : viewIds) {
                                    View view = activity.findViewById(viewId);
                                    //获取制定方法，例子：view如果是Button，获取它的setOnClickListener方法
                                    Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                    setter.invoke(view,listener);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
