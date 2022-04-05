package com.example.demo;

import java.lang.reflect.Field;

// reference - udacity tutorial module
public class testutils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (f.isAccessible()) {
            }
            f.setAccessible(true);
            wasPrivate = true;
            f.set(target, toInject);
            if (!wasPrivate) {
                return;
            }
            f.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
