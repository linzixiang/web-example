package com.linzx.web.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 表单相关
 */
@Controller
@RequestMapping("/demo/form")
public class DemoFormController {

    private String prefix = "demo/form" ;

    /**
     * 按钮页
     */
    @GetMapping("/button")
    public String button() {
        return prefix + "/button" ;
    }

    /**
     * 下拉框
     */
    @GetMapping("/select")
    public String select() {
        return prefix + "/select" ;
    }

    /**
     * 表单校验
     */
    @GetMapping("/validate")
    public String validate() {
        return prefix + "/validate" ;
    }

    /**
     * 功能扩展（包含文件上传）
     */
    @GetMapping("/jasny")
    public String jasny() {
        return prefix + "/jasny" ;
    }

    /**
     * 拖动排序
     */
    @GetMapping("/sortable")
    public String sortable() {
        return prefix + "/sortable" ;
    }

    /**
     * 选项卡 & 面板
     */
    @GetMapping("/tabs_panels")
    public String tabs_panels() {
        return prefix + "/tabs_panels" ;
    }

    /**
     * 栅格
     */
    @GetMapping("/grid")
    public String grid() {
        return prefix + "/grid" ;
    }

    /**
     * 表单向导
     */
    @GetMapping("/wizard")
    public String wizard() {
        return prefix + "/wizard" ;
    }

    /**
     * 文件上传
     */
    @GetMapping("/upload")
    public String upload() {
        return prefix + "/upload" ;
    }

    /**
     * 日期和时间页
     */
    @GetMapping("/datetime")
    public String datetime() {
        return prefix + "/datetime" ;
    }

    /**
     * 左右互选组件
     */
    @GetMapping("/duallistbox")
    public String duallistbox() {
        return prefix + "/duallistbox" ;
    }

    /**
     * 基本表单
     */
    @GetMapping("/basic")
    public String basic() {
        return prefix + "/basic" ;
    }

    /**
     * 卡片列表
     */
    @GetMapping("/cards")
    public String cards() {
        return prefix + "/cards" ;
    }

    /**
     * 搜索自动补全
     */
    @GetMapping("/autocomplete")
    public String autocomplete() {
        return prefix + "/autocomplete" ;
    }

    /**
     * 获取用户数据
     */
//    @GetMapping("/userModel")
//    @ResponseBody
//    public AjaxResult userModel() {
//        AjaxResult ajax = new AjaxResult();
//
//        ajax.put("code", 200);
//        ajax.put("value", users);
//        return ajax;
//    }

    /**
     * 获取数据集合
     */
//    @GetMapping("/collection")
//    @ResponseBody
//    public AjaxResult collection() {
//        String[] array = { "ruoyi 1", "ruoyi 2", "ruoyi 3", "ruoyi 4", "ruoyi 5" };
//        AjaxResult ajax = new AjaxResult();
//        ajax.put("value", array);
//        return ajax;
//    }
    }
