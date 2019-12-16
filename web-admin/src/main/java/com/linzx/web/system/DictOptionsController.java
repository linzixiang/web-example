package com.linzx.web.system;

import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.system.domain.Dict;
import com.linzx.system.domain.DictOptions;
import com.linzx.system.service.IDictService;
import com.linzx.system.service.impl.DictOptionsService;
import com.linzx.utils.Convert;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统字典选项  信息操作处理
 *
 * @author linzixiang
 * @date 2019_05_133
 */
@Controller
@RequestMapping("/system/dict/options")
public class DictOptionsController extends BaseController {

    private String prefix = "system/dict/options";

    @Autowired
    private DictOptionsService dictOptionsService;

    @Autowired
    private IDictService dictService;

    @RequiresPermissions("system:dictOptions:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
    * 列表查询
    */
    @RequiresPermissions("system:dictOptions:list")
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(DictOptions dictOptions) {
        List<DictOptions> dictOptionsList = dictOptionsService.selectList(dictOptions).get();
        return AjaxResult.success(dictOptionsList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd/{dictCode}")
    public String preAdd(ModelMap mmap, @PathVariable("dictCode") String dictCode) {
        Dict dict = dictService.getBybBizCode(dictCode);
        mmap.put("dict", dict);
        return prefix + "/add";
    }

    @RequiresPermissions("system:dictOptions:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(DictOptions dictOptions) {
        Long dictOptionsId = dictOptionsService.insert(dictOptions).get();
        return AjaxResult.success(dictOptionsId);
    }

    /**
     * 修改
     */
    @GetMapping("/preEdit/{dictOptionsId}")
    public String preEdit(@PathVariable("dictOptionsId") Long dictOptionsId, ModelMap mmap) {
        DictOptions dictOptions = dictOptionsService.getById(dictOptionsId).get();
        mmap.put("dictOptions", dictOptions);
        return prefix + "/edit";
    }

    @RequiresPermissions("system:dictOptions:edit")
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult saveEdit(DictOptions dictOptions) {
        dictOptionsService.update(dictOptions);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:dictOptions:remove")
    @PostMapping("/remove/{dictOptionsId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("dictOptionsId") Long dictOptionsId) {
        dictOptionsService.deleteByIds(Convert.toLongArray(dictOptionsId.toString()));
        return AjaxResult.success();
    }

}