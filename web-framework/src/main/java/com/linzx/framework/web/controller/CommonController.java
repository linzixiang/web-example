package com.linzx.framework.web.controller;

import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.service.impl.CommonService;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.framework.web.vo.TableCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 框架通用控制层
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

    @Autowired
    private CommonService commonService;

    /**
     * 查询字典
     */
    @GetMapping("/queryCode")
    @ResponseBody
    public AjaxResult queryCode(@RequestParam("code") String tableCode,
                                @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        List<TableCodeVo> tableCodeVos = commonService.queryTableCode(tableCode, searchKeyword);
        return AjaxResult.success(tableCodeVos);
    }

    /**
     * 查询系统字典
     */
    @GetMapping("/queryDict")
    @ResponseBody
    public AjaxResult queryDict(@RequestParam("queryCode") String queryCode) {
        return AjaxResult.success(commonService.queryDictOptions(queryCode));
    }

}
