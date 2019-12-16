package com.linzx.common.task;

import com.linzx.utils.StrFormatter;
import org.springframework.stereotype.Component;

@Component
public class CustomerTask {

    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        String format = StrFormatter.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i);
        System.out.println(format);
    }

    public void strParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void noParams() {
        System.out.println("执行无参方法");
    }
}
