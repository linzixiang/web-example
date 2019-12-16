package com.linzx.web.system;

import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.framework.web.BaseController;
import com.linzx.system.domain.Config;
import com.linzx.system.service.impl.ConfigService;
import com.linzx.utils.Convert;
import com.linzx.web.system.request.config.ConfigSaveAddDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 系统配置 信息操作处理
 *
 * @author linzixiang
 * @date 2019_07_203
 */
@Controller
@RequestMapping("/system/config")
public class ConfigController extends BaseController {

    private String prefix = "system/config";

    @Autowired
    private ConfigService configService;

    @RequiresPermissions("system:config:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
    * 列表查询
    */
    @RequiresPermissions("system:config:list")
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(Config config) {
        List<Config> configList = configService.selectConfigList(config, false);
        return AjaxResult.success(configList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd")
    public String preAdd(ModelMap mmap) {
        return prefix + "/add";
    }

    @RequiresPermissions("system:config:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(ConfigSaveAddDto saveAddDto) {
        Config config = new Config();
        config.setConfigCode(saveAddDto.getConfigCode());
        config.setConfigName(saveAddDto.getConfigName());
        config.setConfigValue(saveAddDto.getConfigValue());
        config.setRemark(saveAddDto.getRemark());
        Optional<Long> configId = configService.insert(config);
        return AjaxResult.success(configId.get());
    }

    /**
     * 修改
     */
    @GetMapping("/preEdit/{configId}")
    public String preEdit(@PathVariable("configId") Long configId, ModelMap mmap) {
        Optional<Config> config = configService.getById(configId);
        mmap.put("config", config.get());
        return prefix + "/edit";
    }

    @RequiresPermissions("system:config:edit")
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult saveEdit(Config config) {
        configService.update(config);
        return AjaxResult.success();
    }

    @RequiresPermissions("system:config:export")
    @GetMapping("/export")
    public void export(Config config, HttpServletResponse response, HttpServletRequest request) throws IOException {
//        String exportColumns = MapUtils.getString(config.getParams(), Config.EXCEL_EXPORT_COLUMNS, "");
        String exportColumns = "configName,configCode,configValue,sysInner,createdTime";
        List<Config> configList = configService.selectConfigList(config, true);
        super.downloadExcel(configList, "SysConfig", Convert.toStrArray(exportColumns), response, request);
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:config:remove")
    @PostMapping("/remove/{configId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("configId") Long configId) {
        configService.deleteByIds(Convert.toLongArray(configId.toString()));
        return AjaxResult.success();
    }

    /**
     * 检查编码是否唯一
     */
    @PostMapping("/checkConfigCodeUnique")
    @ResponseBody
    public AjaxResult checkConfigCodeUnique(@RequestParam("configCode") String configCode,
                                            @RequestParam(value = "selfId",required = false) Long selfId) {
        return AjaxResult.success(true);
    }

}