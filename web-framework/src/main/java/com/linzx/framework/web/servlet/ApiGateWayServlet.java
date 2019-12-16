package com.linzx.framework.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.bean.ApiBean;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.framework.utils.MessageUtils;
import com.linzx.framework.utils.ServletUtils;
import com.linzx.framework.web.vo.AjaxResult;
import com.linzx.utils.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求转发控制，可通过接口id访问Controller
 */
public class ApiGateWayServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
        super.service(req, resp);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiKey = request.getParameter("apiKey");
        if (StringUtils.isEmpty(apiKey)){
            AjaxResult ajaxResult = new AjaxResult(AjaxResult.Type.ERROR, "访问/api路径，必须带参数apiKey", null);
            ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
            return;
        }
        ApiBean apiGateWay = ContextManager.getApiGateWay(apiKey);
        if (apiGateWay == null) {
            apiGateWay = new ApiBean();
            String urlPath = apiKey.replaceAll("\\.", "/");
            apiGateWay.setUrlMap(urlPath);
        }
        request.getRequestDispatcher(apiGateWay.getUrlMap()).forward(request, response);
    }
}
