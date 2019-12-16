package com.linzx.framework.web.service.impl;

import com.linzx.common.constant.Constants;
import com.linzx.framework.bean.CacheBean;
import com.linzx.framework.bean.CodeBean;
import com.linzx.framework.bean.DictBean;
import com.linzx.framework.core.context.ContextManager;
import com.linzx.framework.exception.BizCommonException;
import com.linzx.framework.mapper.CommonMapper;
import com.linzx.framework.shiro.utils.ShiroUtils;
import com.linzx.framework.utils.SpringUtils;
import com.linzx.framework.web.service.DictCodeService;
import com.linzx.framework.web.vo.DictOptionVo;
import com.linzx.framework.web.vo.TableCodeVo;
import com.linzx.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commonService")
public class CommonService {

    @Autowired
    private CommonMapper commonMapper;

    /**
     * 系统字典查询
     */
    public List<DictOptionVo> queryDictOptions(String queryCode) {
        Cache cache = ContextManager.getCache(new CacheBean.CacheKey("system", "sysDict"));
        Cache.ValueWrapper dictOptionsWrapper = cache.get(queryCode);
        if (dictOptionsWrapper != null) {
            return (List<DictOptionVo>) dictOptionsWrapper.get();
        }
        DictCodeService dictCodeService = SpringUtils.getBeanByType(DictCodeService.class);
        if (dictCodeService == null) {
            throw new RuntimeException("需要先实现就接口：DictCodeService");
        }
        DictBean dictBean = ContextManager.getDict(queryCode);
        if (dictBean == null) {
            dictBean = new DictBean();
            dictBean.setDictCode(queryCode);
            dictBean.setExcludeDelete(Constants.BOOLEAN_TRUE);
            dictBean.setExcludeStop(Constants.BOOLEAN_TRUE);
        }
        List<DictOptionVo> dictOptions = dictCodeService.queryDictOptions(dictBean);
        cache.put(queryCode, dictOptions);
        return dictOptions;
    }

    /**
     * 查询获取字典选项
     */
    public DictOptionVo getDictOption(String queryCode, String value) {
        List<DictOptionVo> dictOptionVoList = queryDictOptions(queryCode);
        for (DictOptionVo dictOption : dictOptionVoList) {
             if (ObjectUtils.equals(dictOption.getValue(), value)) {
                 return dictOption;
             }
        }
        return null;
    }

    /**
     * 查询业务字典（表编码）
     */
    public List<TableCodeVo> queryTableCode(String tableCode, String searchKeyword) {
        CodeBean codeBean = ContextManager.getCode(tableCode);
        if (codeBean == null) {
            throw new BizCommonException("");
        }
        List<TableCodeVo> list = commonMapper.selectCodeList(codeBean, searchKeyword);
         return list;
    }



    /**
     * 菜单权限校验
     */
    public String hasPermi(String permission) {
        return ShiroUtils.isPermittedOperator(permission) ? "" : "hidden";
    }

    /**
     * 角色权限校验
     */
    public String hasRole(String role) {
        return ShiroUtils.hasRoleOperator(role) ? "" : "hidden";
    }

}
