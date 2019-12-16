package com.linzx.framework.es.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linzx.framework.es.bean.MappingData;
import com.linzx.framework.es.bean.MetaData;
import com.linzx.framework.es.service.ElasticsearchIndex;
import com.linzx.framework.es.utils.IndexTools;
import com.linzx.utils.ObjectUtils;
import com.linzx.utils.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ElasticsearchIndexImpl<T> implements ElasticsearchIndex<T> {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void createIndex(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.createMetaData(clazz);
        CreateIndexRequest request = new CreateIndexRequest(metaData.getIndexName());
        JSONObject mappingJson = new JSONObject();
        JSONObject mappingProperties = new JSONObject();
        mappingJson.put("properties", mappingProperties);
        MappingData[] mappingDataList = IndexTools.getMappingData(clazz);
        boolean isAutocomplete = false;
        for (int i = 0; i < mappingDataList.length; i++) {
            MappingData mappingData = mappingDataList[i];
            if(mappingData == null || mappingData.getFieldName() == null){
                continue;
            }
            JSONObject property = new JSONObject();
            property.put("type", mappingData.getDataType());
            if (StringUtils.isNotEmpty(mappingData.getCopyTo())) {
                property.put("copy_to", mappingData.getCopyTo());
            }
            if (!mappingData.isAllowSearch()) {
                property.put("index", false);
            }
            if (mappingData.isAutocomplete() && StringUtils.inStringIgnoreCase(
                    mappingData.getDataType(), "text", "keyword")) {
                property.put("analyzer", "autocomplete");
                property.put("search_analyzer", "standard");
                isAutocomplete = true;
            } else if (ObjectUtils.equals("text", mappingData.getDataType())) {
                property.put("analyzer", mappingData.getAnalyzer());
                property.put("search_analyzer", mappingData.getSearchAnalyzer());
            }
            JSONObject fields = new JSONObject();
            if(mappingData.isKeyword() && !ObjectUtils.equals(mappingData.getDataType(), "keyword") ) {
                JSONObject keywordField = new JSONObject();
                keywordField.put("type", "keyword");
                keywordField.put("ignore_above", mappingData.getIgnoreAbove());
                fields.put("keyword", keywordField);

            }
            if (mappingData.isSuggest()) {
                JSONObject suggestField = new JSONObject();
                suggestField.put("type", "completion");
                suggestField.put("analyzer", mappingData.getAnalyzer());
                fields.put("suggest", suggestField);
            }
            if (!fields.isEmpty()) {
                property.put("fields", fields);
            }
            mappingProperties.put(mappingData.getFieldName(), property);
        }

        if(isAutocomplete){
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumberOfShards())
                    .put("index.number_of_replicas", metaData.getNumberOfReplicas())
                    .put("analysis.filter.autocomplete_filter.type","edge_ngram")
                    .put("analysis.filter.autocomplete_filter.min_gram",1)
                    .put("analysis.filter.autocomplete_filter.max_gram",20)
                    .put("analysis.analyzer.autocomplete.type","custom")
                    .put("analysis.analyzer.autocomplete.tokenizer","standard")
                    .putList("analysis.analyzer.autocomplete.filter",new String[]{"lowercase","autocomplete_filter"})
            );
        }else{
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumberOfShards())
                    .put("index.number_of_replicas", metaData.getNumberOfReplicas())
            );
        }

        request.mapping(metaData.getIndexType(),//类型定义
                mappingJson.toJSONString(),//类型映射，需要的是一个JSON字符串
                XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
            boolean acknowledged = createIndexResponse.isAcknowledged();//指示是否所有节点都已确认请求
            System.out.println(acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropIndex(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.createMetaData(clazz);
        String indexName = metaData.getIndexName();
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Override
    public boolean exists(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.createMetaData(clazz);
        String indexName = metaData.getIndexName();
        String indexType = metaData.getIndexType();
        GetIndexRequest request = new GetIndexRequest();
        request.indices(indexName);
        request.types(indexType);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }



}
