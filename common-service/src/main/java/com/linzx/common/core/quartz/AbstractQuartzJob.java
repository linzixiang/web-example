package com.linzx.common.core.quartz;

import com.linzx.common.domain.QuartzJob;
import com.linzx.common.enums.ScheduleConstants;
import com.linzx.common.utils.BeanUtils;
import com.linzx.framework.utils.SpringUtils;
import com.linzx.utils.ReflectionUtils;
import com.linzx.utils.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractQuartzJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzJob quartzJob = new QuartzJob();
        Object obj = context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES);
        BeanUtils.copyBeanProp(quartzJob, obj);
        try {
            before(context, quartzJob);
            if (quartzJob != null) {
                doExecute(context, quartzJob);
            }
            after(context, quartzJob, null);
        } catch (Exception e) {
            logger.error("任务执行异常  - ：", e);
            after(context, quartzJob, e);
        }
    }

    private void before(JobExecutionContext context, QuartzJob quartzJob) {
        threadLocal.set(new Date());
    }

    private void after(JobExecutionContext context, QuartzJob quartzJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();
        Date endTime = new Date();
        long runMs = endTime.getTime() - startTime.getTime();
        logger.info(quartzJob.getJobName() + " 总共耗时：" + runMs + "毫秒");
    }

    protected abstract void doExecute(JobExecutionContext context, QuartzJob quartzJob);

    /**
     * 获取bean的name
     */
    protected String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    /**
     * 获取方法名
     */
    protected String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    /**
     * 获取方法参数
     */
    public List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr)) {
            return new ArrayList<>();
        }
        String[] methodParams = methodStr.split(",");
        List<Object[]> classs = new LinkedList<>();
        for (int i = 0; i < methodParams.length; i++) {
            String str = StringUtils.trimToEmpty(methodParams[i]);
            // String字符串类型，包含'
            if (StringUtils.contains(str, "'")) {
                classs.add(new Object[]{StringUtils.replace(str, "'", ""), String.class});
            }
            // boolean布尔类型，等于true或者false
            else if (StringUtils.equals(str, "true") || StringUtils.equalsIgnoreCase(str, "false")) {
                classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            }
            // long长整形，包含L
            else if (StringUtils.containsIgnoreCase(str, "L")) {
                classs.add(new Object[]{Long.valueOf(StringUtils.replaceIgnoreCase(str, "L", "")), Long.class});
            }
            // double浮点类型，包含D
            else if (StringUtils.containsIgnoreCase(str, "D")) {
                classs.add(new Object[]{Double.valueOf(StringUtils.replaceIgnoreCase(str, "D", "")), Double.class});
            }
            // 其他类型归类为整形
            else {
                classs.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classs;
    }

    /**
     * 执行定时任务的方法
     */
    protected void executionMethod(String invokeTarget) {
        String beanName = getBeanName(invokeTarget);
        Object bean = SpringUtils.getBean(beanName);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> paramsList = getMethodParams(invokeTarget);
        Object[] params = new Object[paramsList.size()];
        int index = 0;
        for (Object[] os : paramsList) {
            params[index] = (Object) os[0];
            index++;
        }
        ReflectionUtils.invokeMethod(bean, methodName, params);
    }

}
