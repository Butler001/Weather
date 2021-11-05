package com.example.mvplibrary.utils;

import android.os.Build;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import androidx.annotation.RequiresApi;
import androidx.collection.SimpleArrayMap;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 空判断类
 */
public class ObjectUtils {

    private ObjectUtils() {
        throw new UnsupportedOperationException("you can't instantiate me.....");
    }

    public static boolean isEmpty(final Object object) {
        if (object == null) {
            return true;
        }
        if (object.getClass().isArray() && Array.getLength(object) == 0) {
            return true;
        }
        if (object instanceof CharSequence && object.toString().length() == 0) {
            return true;
        }
        if (object instanceof Collection && ((Collection<?>) object).isEmpty()){
            return true;
        }
        if (object instanceof Map && ((Map<?, ?>) object).isEmpty()){
            return true;
        }
        if (object instanceof SimpleArrayMap && ((SimpleArrayMap) object).isEmpty()){
            return true;
        }
        if (object instanceof SparseArray && ((SparseArray<?>) object).size() == 0){
            return true;
        }
        if (object instanceof SparseBooleanArray && ((SparseBooleanArray) object).size() == 0){
            return true;
        }
        if (object instanceof SparseIntArray && ((SparseIntArray) object).size() == 0){
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            if (object instanceof SparseLongArray && ((SparseLongArray) object).size() ==0){
                return true;
            }
        }
        if (object instanceof androidx.collection.LongSparseArray && ((LongSparseArray<?>) object).size() ==0){
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            if (object instanceof LongSparseArray && ((LongSparseArray<?>) object).size() ==0){
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(final CharSequence obj) {
        return obj == null || obj.toString().length() == 0;
    }

    public static boolean isEmpty(final Collection obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(final Map obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(final SimpleArrayMap obj) {
        return obj == null || obj.isEmpty();
    }

    public static boolean isEmpty(final SparseArray obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(final SparseBooleanArray obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(final SparseIntArray obj) {
        return obj == null || obj.size() == 0;
    }

    public static boolean isEmpty(final androidx.collection.LongSparseArray obj) {
        return obj == null || obj.size() == 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean isEmpty(final SparseLongArray obj) {
        return obj == null || obj.size() == 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isEmpty(final android.util.LongSparseArray obj) {
        return obj == null || obj.size() == 0;
    }

    /**
     * Return whether object is not empty.
     *
     * @param obj The object.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isNotEmpty(final Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final CharSequence obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final Collection obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final Map obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final SimpleArrayMap obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final SparseArray obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final SparseBooleanArray obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final SparseIntArray obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotEmpty(final androidx.collection.LongSparseArray obj) {
        return !isEmpty(obj);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean isNotEmpty(final SparseLongArray obj) {
        return !isEmpty(obj);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isNotEmpty(final android.util.LongSparseArray obj) {
        return !isEmpty(obj);
    }

    /**
     * 两个参数相等，返回true
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean equals(Object obj1 , Object obj2){
        return obj1 == obj2 || (obj1 != null && obj1.equals(obj2));
    }

    /**
     * 要求对象不为空
     * @param objects
     */
    public static void requireNotNull(Object... objects){
        if (objects == null){
            throw new NullPointerException();}
        for (Object object : objects){
            if (object == null){
                throw new NullPointerException();
            }
        }

    }

    /**
     * 返回一个对象，默认或者是创造
     * @param object
     * @param defaultObject
     * @param <T>
     * @return
     */
    public static <T> T getOrDefaultObject(final T object, final T defaultObject){
        if (object == null){
            return defaultObject;
        }
        return object;
    }

    /**
     * Return the hash code of object.
     *
     * @param o The object.
     * @return the hash code of object
     */
    public static int hashCode(final Object o) {
        return o != null ? o.hashCode() : 0;
    }


}
