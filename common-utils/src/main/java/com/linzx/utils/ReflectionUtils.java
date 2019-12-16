package com.linzx.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtils extends org.springframework.util.ReflectionUtils {
    /**
     * 类反射属性缓存  key 类名  value—类属性<key: ,value:字段属性>
     */
    private static final Map<Class<?>, Map<String, Field>> fieldCache = new ConcurrentReferenceHashMap<>();

    /**
     * 类反射方法缓存 key-类名 value-类属性<key: , value:方法属性>
     */
    private static final Map<Class<?>, Map<String, Method>> methodCache = new ConcurrentReferenceHashMap<>();


    /**
     * 获取class中的所有属性
     *
     * @param clazz
     * @return
     */
    public static Field[] getAllDeclareFields(Class clazz) {
        if (clazz.getSuperclass() == Object.class) { // 父类就是顶级类
            return clazz.getDeclaredFields();
        }
        Set<String> names = new HashSet<String>();
        List<Field> list = new ArrayList<Field>();
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if (names.contains(field.getName())) {
                    continue;
                }
                list.add(field);//字段属性
            }
            searchType = searchType.getSuperclass();//父类
        }
        Field[] fields = new Field[list.size()];
        return list.toArray(fields);
    }

    /**
     * 直接通过反射获取对象属性的值
     */
    public static <T> T getFieldValue(Object target, Class<T> clz, String fieldName) {
        if (target == null) {
            return null;
        }
        if (StringUtils.isEmpty(fieldName)) {
            return null;
        }
        if (target instanceof Map) {
            return (T) ((Map<String, ?>) target).get(fieldName);
        }
        Class<?> clazz = getClass(target);
        Map<String, Field> fieldMap = fieldCache.get(clazz);
        if (fieldMap == null || !fieldMap.containsKey(fieldName)) {
            synchronized (clazz) {
                fieldMap = fieldCache.get(clazz);
                if (fieldMap == null) {
                    fieldMap = new ConcurrentReferenceHashMap<String, Field>();
                    fieldCache.put(clazz, fieldMap);
                }
                Field field = findField(clazz, fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    fieldMap.put(fieldName, field);
                }
            }
        }
        Field field = getFieldMust(fieldName, clazz, fieldMap);
        try {
            return (T) field.get(target);
        } catch (Exception e) {
            String message = String.format("类 [%s] 反射访问属性 [%s] 异常!", clazz.getCanonicalName(), fieldName);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * 直接通过反射设置对象属性的值
     */
    public static void setFieldValue(Object target, String fieldName, Object fieldValue) {
        if (target == null || StringUtils.isEmpty(fieldName)) {
            return;
        }
        //map直接返回
        if (target instanceof Map) {
            ((Map) target).put(fieldName, fieldValue);
            return;
        }

        Class<?> clazz = getClass(target);
        Map<String, Field> fieldMap = fieldCache.get(clazz);
        //没有属性
        if (fieldMap == null || !fieldMap.containsKey(fieldName)) {
            synchronized (clazz) {
                fieldMap = fieldCache.get(clazz);
                if (fieldMap == null || !fieldMap.containsKey(fieldName)) {
                    Field field = findField(clazz, fieldName);
                    if (field != null) {
                        field.setAccessible(true);
                        if (fieldMap == null) {
                            fieldMap = new ConcurrentReferenceHashMap<>();//做个长度缓存
                            fieldCache.put(clazz, fieldMap);
                        }
                        fieldMap.put(fieldName, field);
                    }
                }
            }
        }
        Field filed = getFieldMust(fieldName, clazz, fieldMap);
        try {
            filed.set(target, fieldValue);
        } catch (Exception e) {
            String message = String.format("类 [%s] 反射访问属性 [%s] 异常!", clazz.getCanonicalName(), fieldName);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * 反射调用类的静态方法(不支持重载方法的反射调用)
     */
    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object... objects) {
        return invokeMethod(clazz, methodName, objects);
    }

    public static Object invokeMethod(Object target, String methodName, Object[] objects) {
        return invokeMethod(target, methodName, objects, null);
    }
    /**
     * 反射调用方法(不支持重载方法的反射调用)
     */
    public static Object invokeMethod(Object target, String methodName, Object[] objects, Class<?>[] paramType) {
        if (target == null || StringUtils.isEmpty(methodName)) {
            return null;
        }

        Class<?> clazz = null;
        if (target instanceof Class) {//本身就是class类 可能是调用静态方法
            clazz = (Class<?>) target;
        } else {
            clazz = target.getClass();
        }

        Map<String, Method> methodMap = methodCache.get(clazz);
        //没有属性
        if (methodMap == null || !methodMap.containsKey(methodName)) {
            synchronized (clazz) {
                methodMap = methodCache.get(clazz);
                if (methodMap == null || !methodMap.containsKey(methodName)) {
                    // 不支持重载方法放射调用
                    Method method = null;
                    if (objects == null || objects.length == 0) {
                        method = findMethod(clazz, methodName, new Class<?>[]{});
                    } else if (paramType != null){
                        method = findMethod(clazz, methodName, paramType);
                    } else {
                        paramType = new Class[objects.length];
                        for (int i = 0; i < objects.length; i++) {
                            paramType[i] = objects[0].getClass();
                        }
                        method = findMethod(clazz, methodName, paramType);
                    }
                    if (method != null) {
                        method.setAccessible(true);
                        if (methodMap == null) {
                            methodMap = new ConcurrentReferenceHashMap<>();//做个长度缓存
                            methodCache.put(clazz, methodMap);
                        }
                        methodMap.put(methodName, method);
                    }
                }
            }

        }
        if (methodMap == null || !methodMap.containsKey(methodName)) {
            throw new RuntimeException(String.format("类 [%s] 不存在方法 [%s]", clazz.getCanonicalName(), methodName));
        }
        Method method = methodMap.get(methodName);
        try {
            return method.invoke(target, objects);
        } catch (Exception e) {
            String message = String.format("类 [%s] 反射访问方法 [%s] 异常!", clazz.getCanonicalName(), methodName);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * 获取注解注释的属性（要求唯一）
     */
    public static <A extends Annotation> Field findUniqueFieldWithAnnotation(Class<?> clz, final Class<A> type) {
        final List<Field> fields = new ArrayList<Field>();
        doWithFields(clz, new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException {
                fields.add(field);
            }
        }, new FieldFilter() {
            @Override
            public boolean matches(Field field) {
                return field.isAnnotationPresent(type);
            }
        });

        if (fields.size() > 1) {
            throw new IllegalStateException("被注释" + type.getSimpleName() + "声明的域不唯一");
        } else if (fields.size() == 1) {
            return fields.get(0);
        }
        return null;
    }

    /**
     * 获得全部使用指定注释声明的方法
     */
    public static Method[] getDeclaredMethodsWith(Class<?> clazz, Class<? extends Annotation> annotionClass) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotionClass)) {
                methods.add(method);
            }
        }
        return methods.toArray(new Method[0]);
    }

    private static Field getFieldMust(String fieldName, Class<?> clazz, Map<String, Field> fieldMap) {
        if (fieldMap == null || !fieldMap.containsKey(fieldName)) {
            throw new RuntimeException(String.format("类 [%s] 不存在属性 [%s]", clazz.getCanonicalName(), fieldName));
        }
        return fieldMap.get(fieldName);
    }


    private static Class<?> getClass(Object target) {
        if (target instanceof Class) {
            return (Class<?>) target;
        } else {
            return target.getClass();
        }
    }

}
