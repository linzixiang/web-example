package com.linzx.framework.web.support.serialize;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.linzx.framework.bean.SensitiveBean;
import com.linzx.framework.core.context.ThreadLocalVariable;
import com.linzx.framework.utils.SpringUtils;
import com.linzx.utils.ReflectionUtils;

import java.util.Map;

/**
 * 敏感字段序列化
 */
public class SensitivePropSerialize implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        Map<String, SensitiveBean> sensitiveBeanMap = ThreadLocalVariable.getSensitiveVariable();
        if (sensitiveBeanMap != null && sensitiveBeanMap.containsKey(name)) {
            SensitiveBean sensitiveBean = sensitiveBeanMap.get(name);
            String[] beanMethodArr = sensitiveBean.getFormatBeanMethod().split("\\.");
            Object bean = SpringUtils.getBean(beanMethodArr[0]);
            return ReflectionUtils.invokeMethod(bean, beanMethodArr[1], new Object[]{value});
        }
        return value;
    }

}
