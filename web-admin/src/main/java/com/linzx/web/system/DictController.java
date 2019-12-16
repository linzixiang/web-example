package com.linzx.web.system;

import com.linzx.framework.utils.PageHelper;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.system.domain.Dict;
import com.linzx.system.service.impl.DictService;
import com.linzx.utils.Convert;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统字典  信息操作处理
 *
 * @author linzixiang
 * @date 2019_05_133
 */
@Controller
@RequestMapping("/system/dict")
public class DictController extends BaseController {

    private String prefix = "system/dict";

    @Autowired
    private DictService dictService;

    @RequiresPermissions("system:dict:view")
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
    * 列表查询
    */
    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list(Dict dict) {
        PageHelper.startPage();
        List<Dict> dictList = dictService.selectList(dict).get();
        return AjaxResult.success(dictList);
    }

    /**
     * 新增
     */
    @GetMapping("/preAdd")
    public String preAdd(ModelMap mmap) {
        return prefix + "/add";
    }

    @RequiresPermissions("system:dict:add")
    @PostMapping("/saveAdd")
    @ResponseBody
    public AjaxResult saveAdd(Dict dict) {
        Long dictId = dictService.insert(dict).get();
        return AjaxResult.success(dictId);
    }

    /**
     * 修改
     */
    @GetMapping("/preEdit/{dictId}")
    public String preEdit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
        Dict dict = dictService.getById(dictId).get();
        mmap.put("dict", dict);
        return prefix + "/edit";
    }

    @RequiresPermissions("system:dict:edit")
    @PostMapping("/saveEdit")
    @ResponseBody
    public AjaxResult saveEdit(Dict dict) {
        dictService.update(dict);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:dict:remove")
    @PostMapping("/remove/{dictId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("dictId") Long dictId) {
        dictService.deleteByIds(Convert.toLongArray(dictId.toString()));
        return AjaxResult.success();
    }

    /** 自定义接口 **/
    @PostMapping("/checkDictCodeUnique")
    @ResponseBody
    public AjaxResult checkDictCodeUnique(@RequestParam(value = "dictCode") String dictCode ,
                                          @RequestParam(value = "selfId", required = false) String selfId) {
        Boolean isExist = dictService.isDictCodeExist(dictCode, selfId);
        return AjaxResult.success(!isExist);
    }

}