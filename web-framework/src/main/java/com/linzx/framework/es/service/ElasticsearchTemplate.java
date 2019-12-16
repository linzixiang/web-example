package com.linzx.framework.es.service;

import com.linzx.framework.es.bean.AggsType;
import com.linzx.framework.es.bean.Down;
import com.linzx.framework.es.bean.PageList;
import com.linzx.framework.es.bean.PageSortHighLight;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;

import java.util.List;
import java.util.Map;

public interface ElasticsearchTemplate<T,M> {

    /**
     * 非分页，默认的查询条数
     */
    int DEFALT_PAGE_SIZE = 200;
    /**
     * 搜索建议默认条数
     */
    int COMPLETION_SUGGESTION_SIZE = 10;

    /**
     * 高亮字段默认tag
     */
    String HIGHLIGHT_TAG = "";

    /**
     * 创建索引mapping时，是否默认创建keyword
     */
    boolean DEFAULT_KEYWORDS = true;

    /**
     * SCROLL查询 2小时
     */
    long DEFAULT_SCROLL_TIME = 2;

    /**
     * SCROLL查询 每页默认条数
     */
    int DEFAULT_SCROLL_PERPAGE = 100;

    double[] DEFAULT_PERCSEGMENT = {50.0,95.0,99.0};

    /**
     * 获取RestHighLevelClient
     */
    RestHighLevelClient getRestClient();

    /**
     * 通过Low Level REST Client 查询
     */
    Response request(Request request) throws Exception;
    /**
     * 新增索引
     */
    boolean save(T t) throws Exception;

    /**
     * 新增索引集合
     */
    BulkResponse save(List<T> list) throws Exception;

    /**
     * 按照有值字段更新索引
     */
    boolean update(T t) throws Exception;

    /**
     * 覆盖更新索引
     * @param t
     */
    boolean updateCover(T t) throws Exception;

    /**
     * 删除索引
     * @param t
     */
    boolean delete(T t) throws Exception;

    /**
     * 删除索引
     * @param id
     */
    boolean deleteById(M id, Class<T> clazz) throws Exception;


    /**
     * 【最原始】查询
     * @param searchRequest
     * @return
     * @throws Exception
     */
    SearchResponse search(SearchRequest searchRequest) throws Exception;

    /**
     * TODO 这里回头有时间研究一下jpa的原理，如何能够直接给interface生成代理类，并托管spring
     * 非分页查询
     * 目前暂时传入类类型
     */
    List<T> search(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 查询数量
     */
    long count(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 支持分页、高亮、排序的查询
     */
    PageList<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight, Class<T> clazz) throws Exception;

    /**
     * scroll方式查询(默认了保留时间为Constant.DEFAULT_SCROLL_TIME)
     */
    List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * scroll方式查询
     * @param queryBuilder
     * @param clazz
     * @param time 保留小时
     * @return
     * @throws Exception
     */
    List<T> scroll(QueryBuilder queryBuilder, Class<T> clazz, long time) throws Exception;

    /**
     * Template方式搜索，Template已经保存在script目录下
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     */
    @Deprecated
    List<T> searchTemplate(Map<String, Object> template_params, String templateName, Class<T> clazz) throws Exception;

    /**
     * Template方式搜索，Template内容以参数方式传入
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     * @param template_params
     * @param templateSource
     * @param clazz
     * @return
     */
    @Deprecated
    List<T> searchTemplateBySource(Map<String, Object> template_params,String templateSource,Class<T> clazz) throws Exception;

    /**
     * 保存Template
     * look at https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.6/java-search-template.html
     * 暂时无法使用该方法，原因为官方API SearchTemplateRequestBuilder仍保留对transportClient 的依赖，但Migration Guide 中描述需要把transportClient迁移为RestHighLevelClient
     * @param templateName
     * @param templateSource
     * @param clazz
     * @return
     */
    @Deprecated
    List<T> saveTemplate(String templateName,String templateSource,Class<T> clazz) throws Exception;

    /**
     * 搜索建议
     * @param fieldName
     * @param fieldValue
     * @param clazz
     * @return
     * @throws Exception
     */
    List<String> completionSuggest(String fieldName,String fieldValue,Class<T> clazz) throws Exception;

    /**
     * 根据ID查询
     * @param id
     * @param clazz
     * @return
     * @throws Exception
     */
    T getById(M id, Class<T> clazz) throws Exception;

    /**
     * 根据ID列表批量查询
     * @param ids
     * @param clazz
     * @return
     * @throws Exception
     */
    List<T> mgetById(M[] ids, Class<T> clazz) throws Exception;

    /**
     * id数据是否存在
     * @param id
     * @param clazz
     * @return
     */
    boolean exists(M id,Class<T> clazz) throws Exception;

    /**
     * 普通聚合查询
     * 以bucket分组以aggstypes的方式metric度量
     * @param bucketName
     * @param metricName
     * @param aggsType
     * @param clazz
     * @return
     */
    Map aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception;


    /**
     * 以aggstypes的方式metric度量
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    double aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;


    /**
     * 下钻聚合查询(无排序默认策略)
     * 以bucket分组以aggstypes的方式metric度量
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketNames
     * @return
     * @throws Exception
     */
    List<Down> aggswith2level(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz , String... bucketNames) throws Exception;


    /**
     * 统计聚合metric度量
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Stats statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以bucket分组，统计聚合metric度量
     * @param bucketName
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Map<String,Stats> statsAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz, String bucketName) throws Exception;


    /**
     * 通用（定制）聚合基础方法
     * @param aggregationBuilder
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Aggregations aggs(AggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;


    /**
     * 基数查询
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    long cardinality(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 百分比聚合 默认聚合见Constant.DEFAULT_PERCSEGMENT
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @return
     * @throws Exception
     */
    Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz) throws Exception;

    /**
     * 以百分比聚合
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param customSegment
     * @return
     * @throws Exception
     */
    Map percentilesAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz,double... customSegment) throws Exception;



    /**
     * 以百分等级聚合 (统计在多少数值之内占比多少)
     * @param metricName
     * @param queryBuilder
     * @param clazz
     * @param customSegment
     * @return
     * @throws Exception
     */
    Map percentileRanksAggs(String metricName, QueryBuilder queryBuilder, Class<T> clazz,double... customSegment) throws Exception;



    /**
     * 过滤器聚合
     * new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male"))
     * @param metricName
     * @param aggsType
     * @param clazz
     * @param queryBuilder
     * @param filters
     * @return
     * @throws Exception
     */
    Map filterAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder,Class<T> clazz, FiltersAggregator.KeyedFilter... filters) throws Exception;

    /**
     * 直方图聚合
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @return
     * @throws Exception
     */
    Map histogramAggs(String metricName,  AggsType aggsType,QueryBuilder queryBuilder,Class<T> clazz,String bucketName,double interval) throws Exception;


    /**
     * 日期直方图聚合
     * @param metricName
     * @param aggsType
     * @param queryBuilder
     * @param clazz
     * @param bucketName
     * @param interval
     * @return
     * @throws Exception
     */
    Map dateHistogramAggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, Class<T> clazz, String bucketName, DateHistogramInterval interval) throws Exception;

}
