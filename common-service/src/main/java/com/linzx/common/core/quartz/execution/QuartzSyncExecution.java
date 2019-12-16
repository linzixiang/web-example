package com.linzx.common.core.quartz.execution;

import com.linzx.common.core.quartz.AbstractQuartzJob;
import com.linzx.common.domain.QuartzJob;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 */
public class QuartzSyncExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, QuartzJob quartzJob) {
        super.executionMethod(quartzJob.getInvokeTarget());
    }

}
