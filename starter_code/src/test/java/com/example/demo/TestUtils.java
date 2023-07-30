package com.example.demo;
import java.lang.reflect.Field;


public class TestUtils {
    public static void injectObject(Object object, String field, Object toInject){
        boolean isAccessible= false;
        try{
            Field f=  object.getClass().getDeclaredField(field);
            //check if field is accessible
            if(!f.isAccessible()){
                 f.setAccessible(true);
                 isAccessible=true;
            }
            try {
                f.set(object, toInject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(isAccessible){
                f.setAccessible(false);
            }
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }

    }

}
