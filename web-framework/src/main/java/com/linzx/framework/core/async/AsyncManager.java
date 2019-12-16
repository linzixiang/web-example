package com.linzx.framework.core.async;

import com.linzx.common.utils.ThreadUtils;
import com.linzx.framework.core.async.task.AbstractCallableTask;
import com.linzx.framework.core.async.task.AbstractRunnableTask;
import com.linzx.framework.shiro.core.session.ShiroSessionValidationScheduler;
import com.linzx.framework.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AsyncManager implements DisposableBean, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(AsyncManager.class);

    private static final ThreadLocal<List<Runnable>> runnableTask = new ThreadLocal<>();

    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 异步操作任务调度线程池
     */
    @Autowired
    private ScheduledExecutorService executor;

    @Autowired(required = false)
    private ShiroSessionValidationScheduler shiroSessionValidationScheduler;

    /** 单例模式 */
    private static AsyncManager me = null;

    public static AsyncManager me() {
        if (me == null) {
            me = SpringUtils.getBean(AsyncManager.class);
        }
        return me;
    }

    /**
     * 执行任务
     */
    public void execute(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行异步任务，带返回值
     */
    public <P, V> Future<V> submit(AbstractCallableTask<P, V> task) {
        Future<V> submit = executor.submit(task);
        return submit;
    }

    /**
     * 执行异步任务，无返回值
     */
    public <P> void execute(AbstractRunnableTask<P> task) {
        executor.execute(task);
    }

    /**
     * 事物提交之后执行异步任务，如果任务执行失败允许丢失
     **/
    public <P> void executeAfterTransitionCommit(AbstractRunnableTask<P> task) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            executor.execute(task);
            return;
        }
        List<Runnable> taskList = runnableTask.get();
        if (taskList == null) {
            taskList = new ArrayList<>();
            runnableTask.set(taskList);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

                /** 事物提交后执行 **/
                @Override
                public void afterCommit() {
                    List<Runnable> runnableList = runnableTask.get();
                    for (Runnable task : runnableList) {
                        try {
                            executor.execute(task);
                        } catch (Exception e) {
                            logger.error("异步任务执行失败", e);
                        }

                    }
                }

                /** 事物提交成功后，或者回滚执行 **/
                @Override
                public void afterCompletion(int status) {
                    runnableTask.remove();
                }
            });
        }
        taskList.add(task);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }

    @PreDestroy
    public void preDestroy() {
        try {
            if (shiroSessionValidationScheduler != null && shiroSessionValidationScheduler.isEnabled()) {
                logger.info("====关闭会话验证任务====");
                shiroSessionValidationScheduler.disableSessionValidation();
            }
            logger.info("====关闭后台任务任务线程池====");
            AsyncManager.me().shutdown();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("--------------AsyncManager destroy----------------");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        me = SpringUtils.getBean(AsyncManager.class);
    }
}
