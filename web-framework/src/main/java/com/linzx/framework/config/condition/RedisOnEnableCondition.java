package com.linzx.framework.config.condition;

import com.linzx.utils.Convert;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * redis是否可用，控制相关bean的加载
 */
public class RedisOnEnableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String redisEnable = conditionContext.getEnvironment().getProperty("spring.redis.enable");
        Boolean redisEnableBool = Convert.toBool(redisEnable, false);
        return redisEnableBool;
    }

}
