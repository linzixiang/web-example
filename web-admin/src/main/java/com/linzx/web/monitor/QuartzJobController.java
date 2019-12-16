package com.linzx.web.monitor;

import com.linzx.common.service.impl.QuartzJobService;
import com.linzx.framework.utils.PageHelper;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.framework.web.BaseController;
import com.linzx.common.domain.QuartzJob;
import com.linzx.web.monitor.request.QuartzJobSaveAddDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务调度  信息操作处理
 *
 * @author linzixiang
 * @date 2019_07_208
 */
@Controller
@RequestMapping("/monitor/quartzJob")
public class QuartzJobController extends BaseController {

    private String prefix = "monitor/quartzJob";

    @Autowired
    private QuartzJobService quartzJobService;

    @RequiresPermissions("monitor:quartzJob:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
    * 列表查询
    */
    @RequiresPermissions("monitor:quartzJob:list")
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(QuartzJob quartzJob) {
        PageHelper.startPage();
        List<QuartzJob> quartzJobList = quartzJobService.selectList(quartzJob).get();
        return AjaxResult.success(quartzJobList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd")
    public String preAdd(ModelMap mmap) {
        return prefix + "/add";
    }

    @RequiresPermissions("monitor:quartzJob:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(QuartzJobSaveAddDto quartzJobDto) {
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setJobName(quartzJobDto.getJobName());
        quartzJob.setJobGroup(quartzJobDto.getJobGroup());
        quartzJob.setInvokeTarget(quartzJobDto.getInvokeTarget());
        quartzJob.setCronExpression(quartzJobDto.getCronExpression());
        quartzJob.setMisfirePolicy(quartzJobDto.getMisfirePolicy());
        quartzJob.setConcurrent(quartzJobDto.getConcurrent());
        quartzJob.setStatus(quartzJobDto.getStatus());
        quartzJob.setRemark(quartzJobDto.getRemark());
        Long quartzJobId = quartzJobService.insertQuartzJob(quartzJob);
        return AjaxResult.success(quartzJobId);
    }

    /**
     * 预览
     */
    @GetMapping("/detail/{quartzJobId}")
    public String preEdit(@PathVariable("quartzJobId") Long quartzJobId, ModelMap mmap) {
        QuartzJob quartzJob = quartzJobService.getById(quartzJobId).get();
        mmap.put("quartzJob", quartzJob);
        return prefix + "/detail";
    }

    /**
     * 删除
     */
    @RequiresPermissions("monitor:quartzJob:remove")
    @PostMapping("/remove/{quartzJobId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("quartzJobId") Long quartzJobId) {
        quartzJobService.deleteQuartzJobByIds(quartzJobId.toString());
        return AjaxResult.success();
    }

    /**
     * 任务调度状态修改
     */
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(@RequestParam("quartzJobId") Long quartzJobId,
                                   @RequestParam("status") Integer status) throws Exception {
        QuartzJob quartzJob = quartzJobService.getById(quartzJobId).get();
        if (status.equals(quartzJob.getStatus())) {
            return AjaxResult.error("状态未改变");
        }
        quartzJobService.changeJobStatus(quartzJobId,quartzJob.getJobGroup(), status);
        return AjaxResult.success();
    }

    /**
     * 立即执行任务
     */
    @PostMapping("/runImmediate")
    @ResponseBody
    public AjaxResult runImmediate(@RequestParam("quartzJobId") Long quartzJobId) throws Exception {
        quartzJobService.runImmediate(quartzJobId);
        return AjaxResult.success();
    }

    /**
     * 检查cron表达式
     */
    @PostMapping("/checkCronExpressionIsValid")
    @ResponseBody
    public AjaxResult checkCronExpression(String cronExpression) {
        boolean isCronExpressionValid = quartzJobService.checkCronExpression(cronExpression);
        return AjaxResult.success(isCronExpressionValid);
    }

}