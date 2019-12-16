package com.linzx.framework.core.context;

import com.linzx.framework.bean.*;
import com.linzx.utils.CollectionUtils;
import com.linzx.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 全局管理
 */
@Component
public class ContextManager {

    private static CacheManager cacheManager;

    public ContextManager(@Autowired  CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /** 选项字典配置文件缓存 */
    private static final Map<String, CodeBean> codeMap = new ConcurrentHashMap<>();

    /** api配置文件缓存 */
    private static final Map<String, ApiBean> apiMap = new ConcurrentHashMap<>();

    /** 系统字典配置文件缓存 **/
    private static final Map<String, DictBean> dictMap = new ConcurrentHashMap<>();
    // 字典选项可以有多个组合
    private static final Map<String, List<String>> dictGroupMap = new ConcurrentHashMap<>();

    /** 敏感字段缓存 **/
    private static final Map<String, SensitiveBean> sensitiveMap = new ConcurrentSkipListMap<>();

    /** Excel表格导出 **/
    private static final Map<String, ExcelExportBean> excelExportBeanMap = new ConcurrentSkipListMap<>();

    public static Cache getCache(CacheBean.CacheKey cacheKey) {
        Cache cache = cacheManager.getCache(cacheKey.get());
        return cache;
    }

    public static Cache getCache(String moduleName, String keyName) {
        CacheBean.CacheKey cacheKey = new CacheBean.CacheKey(moduleName, keyName);
        Cache cache = cacheManager.getCache(cacheKey.get());
        return cache;
    }

    public static void addCode(String keyCode, CodeBean codeBean) {
        if (StringUtils.isEmpty(keyCode)) {
            throw new RuntimeException("CodeBean属性" + CodeBean.UNIQUE_KEYNAME + "不允许为空");
        }
        if (codeMap.containsKey(keyCode)) {
            throw new RuntimeException("CodeBean属性" + CodeBean.UNIQUE_KEYNAME + "已存在：" + keyCode);
        }
        codeMap.put(keyCode, codeBean);
    }

    public static CodeBean getCode(String keyCode) {
        return codeMap.get(keyCode);
    }

    public static void addApiGateWay(String apiId, ApiBean apiBean) {
        if (StringUtils.isEmpty(apiId)) {
            throw new RuntimeException("ApiBean属性" + ApiBean.UNIQUE_KEYNAME + "不允许为空");
        }
        if (getApiGateWay(apiId) != null) {
            throw new RuntimeException("ApiBean属性" + ApiBean.UNIQUE_KEYNAME + "已存在：" + apiId);
        }
        apiMap.put(apiId, apiBean);
    }

    public static ApiBean getApiGateWay(String apiId) {
        ApiBean apiBean = apiMap.get(apiId);
        return apiBean;
    }

    public static void addDict(String queryCode, DictBean dictBean) {
        if (StringUtils.isEmpty(queryCode)) {
            throw new RuntimeException("DictBean属性" + DictBean.UNIQUE_KEYNAME + "不允许为空");
        }
        if (codeMap.containsKey(queryCode)) {
            throw new RuntimeException("DictBean属性" + DictBean.UNIQUE_KEYNAME + "已存在：" + queryCode);
        }
        List<String> dictCodeList = dictGroupMap.get(dictBean.getDictCode());
        if (dictCodeList == null) {
            dictCodeList = new ArrayList<>();
            dictGroupMap.put(dictBean.getDictCode(), dictCodeList);
        }
        dictCodeList.add(queryCode);
        dictMap.put(queryCode, dictBean);
    }

    public static DictBean getDict(String queryCode) {
        return dictMap.get(queryCode);
    }

    /**
     * 获取该字典下的所有组合
     */
    public static List<String> getDictGroup(String dictCode) {
        return dictGroupMap.get(dictCode);
    }

    public static void addSensitive(String sensitiveCode, SensitiveBean sensitiveBean) {
        if (StringUtils.isEmpty(sensitiveCode)) {
            throw new RuntimeException("SensitiveBean属性" + SensitiveBean.UNIQUE_KEYNAME + "不允许为空");
        }
        if (sensitiveMap.containsKey(sensitiveCode)) {
            throw new RuntimeException("DictBean属性" + DictBean.UNIQUE_KEYNAME + "已存在：" + sensitiveCode);
        }
        sensitiveMap.put(sensitiveCode, sensitiveBean);
    }

    public static SensitiveBean getSensitive(String sensitiveCode) {
        return sensitiveMap.get(sensitiveCode);
    }

    public static List<SensitiveBean> getAllSensitive () {
        Collection<SensitiveBean> sensitiveBeans = sensitiveMap.values();
        List<SensitiveBean> sensitiveBeanList = new ArrayList<>();
        for (SensitiveBean sensitiveBean : sensitiveBeans) {
            sensitiveBeanList.add(sensitiveBean);
        }
        return sensitiveBeanList;
    }

    public static void addExcelExportBean(String keyName, ExcelExportBean excelExportBean) {
        if (CollectionUtils.isNotEmpty(excelExportBean.getContent())) {
            for (ExcelExportContent column : excelExportBean.getContent()) {
                excelExportBean.addField(column.getField());
            }
            excelExportBeanMap.put(keyName, excelExportBean);
        }
    }

    public static ExcelExportBean getExcelExportBean(String keyName) {
        return excelExportBeanMap.get(keyName);
    }

}
