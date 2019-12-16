post请求格式：
    "reqHeader": {}&"pageNum": "第几页，从1开始计数(默认1)"&pageSize": "每页显示记录数，默认20"
    &"orderByColumn": "排序列"&"orderDirection": "排序方向，desc 或者 asc"

json响应格式
   成功：{ "bizCode": 0, "bizMsg": "成功", "data": {} }
   成功：{ "bizCode": 0, "bizMsg": "成功", "data": {"totalNum": "总数量", "rows": []} }
   失败：{"timestamp": "时间戳","status": "http响应状态","error":"OK","path":"请求地址","bizMsg":"错误提交","bizCode":"业务错误码"}

对象包命名方式：
    BO（Business Object）业务对象
    DAO（Data Access Object）数据访问对象
    DO（Domain Object）领域对象
    DTO（Data Transfer Object）数据传输对象
    PO（Persistent Object）持久对象
    POJO（Plain Ordinary Java Object）简单无规则 Java 对象
    VO（View Object）值对象