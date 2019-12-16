package com.linzx.framework.es.utils;

import com.linzx.framework.es.annotation.ESID;
import com.linzx.framework.es.annotation.ESMapping;
import com.linzx.framework.es.annotation.ESMetaData;
import com.linzx.framework.es.bean.MappingData;
import com.linzx.framework.es.bean.MetaData;
import com.linzx.utils.ReflectionUtils;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class IndexTools {

    /**
     * 根据注解内容创建MetaData
     */
     public static <T> MetaData createMetaData(Class<T> clazz) {
        ESMetaData annotation = clazz.getAnnotation(ESMetaData.class);
        if(annotation == null){
            return null;
        }
        MetaData metaData = new MetaData();
        metaData.setIndexName(annotation.indexName());
        metaData.setIndexType(annotation.indexType());
        metaData.setPrintLog(annotation.printLog());
        metaData.setNumberOfShards(annotation.numberOfShards());
        metaData.setNumberOfReplicas(annotation.numberOfReplicas());
        return metaData;
    }

    public static <T> String getESId(T t) throws IllegalAccessException {
        Field field = ReflectionUtils.findUniqueFieldWithAnnotation(t.getClass(), ESID.class);
        String id = "";
        if (field != null) {
            id = field.get(t).toString();
        }
        return id;
    }

    /**
     * 获取不是null的对象属性值
     */
    public static Map<String, Object> getFieldValIfNotNull(Object obj) {
        Field[] fields = ReflectionUtils.getAllDeclareFields(obj.getClass());
        Map<String, Object> retMap = new HashMap<>();
        for(int i = 0;i < fields.length;i++) {
            Field field = fields[i];
            Object value = ReflectionUtils.getFieldValue(obj, obj.getClass(), field.getName());
            if (value != null) {
                retMap.put(field.getName(), value);
            }
        }
        return retMap;
    }


    public static MappingData[] getMappingData(Class<?> clazz){
        Field[] fields = ReflectionUtils.getAllDeclareFields(clazz);
        MappingData[] mappingDataList = new MappingData[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if(fields[i].getName().equals("serialVersionUID")){
                continue;
            }
            mappingDataList[i] = getMappingData(fields[i]);
        }
        return mappingDataList;
    }

    public static MappingData getMappingData(Field field){
        if(field == null){
            return null;
        }
        field.setAccessible(true);
        MappingData mappingData = new MappingData();
        mappingData.setFieldName(field.getName());
        if(field.getAnnotation(ESMapping.class) != null){
            ESMapping esMapping = field.getAnnotation(ESMapping.class);
            mappingData.setDataType(esMapping.datatype().toString().replaceAll("_type",""));
            mappingData.setAnalyzer(esMapping.analyzer().toString());
            mappingData.setAutocomplete(esMapping.autocomplete());
            mappingData.setIgnoreAbove(esMapping.ignoreAbove());
            mappingData.setSearchAnalyzer(esMapping.searchAnalyzer().toString());
            mappingData.setKeyword(esMapping.keyword());
            mappingData.setSuggest(esMapping.suggest());
            mappingData.setAllowSearch(esMapping.allowSearch());
            mappingData.setCopyTo(esMapping.copyTo());
        }else{
            mappingData.setDataType("text");
            mappingData.setAnalyzer("standard");
            mappingData.setAutocomplete(false);
            mappingData.setIgnoreAbove(256);
            mappingData.setSearchAnalyzer("standard");
            mappingData.setKeyword(true);
            mappingData.setSuggest(false);
            mappingData.setAllowSearch(true);
            mappingData.setCopyTo("");
        }
        return mappingData;
    }

}
