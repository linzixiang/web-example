package com.linzx.web.tool;

import com.linzx.framework.utils.PageHelper;
import com.linzx.framework.web.BaseController;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.generator.domain.TableInfo;
import com.linzx.generator.service.IGenService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/tool/gen")
public class GenController extends BaseController {

    private String prefix = "tool/gen";

    @Autowired
    private IGenService genService;

    @RequiresPermissions("tool:gen:view")
    @GetMapping("/index")
    public String gen() {
        return prefix + "/index";
    }

    @RequiresPermissions("tool:gen:list")
    @PostMapping("/list")
    @ResponseBody
    public AjaxResult list(TableInfo tableInfo) {
        PageHelper.startPage();
        List<TableInfo> list = genService.selectTableList(tableInfo);
        return AjaxResult.success(list);
    }

    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genService.generatorCode(tableName);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

}
