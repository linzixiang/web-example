package com.linzx.utils;

import java.util.Collection;

public class CollectionUtils {

    /**
     * 判断一个对象数组是否非空
     */
    public static boolean isEmpty(Object[] objects) {
        return objects == null || (objects.length == 0);
    }

    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * 判断一个对象集合是否非空
     */
    public static boolean isEmpty(Collection list) {
        return list == null || (list.size() == 0);
    }

    public static boolean isNotEmpty(Collection list) {
        return !isEmpty(list);
    }
}
