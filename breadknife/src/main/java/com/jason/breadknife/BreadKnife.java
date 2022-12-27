package com.jason.breadknife;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

public class BreadKnife {

    public static void bind(Activity activity) {
        //获取activity的decorView(根view)
        View view = activity.getWindow().getDecorView();
        String qualifiedName = activity.getClass().getName();

        //找到该activity对应的Bind类的名字
        String generateClass = qualifiedName + "_ViewBinding";
        //然后调用Bind类的构造方法,从而完成activity里view的初始化
        try {
            Class.forName(generateClass).getConstructor(activity.getClass(), View.class).newInstance(activity, view);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
