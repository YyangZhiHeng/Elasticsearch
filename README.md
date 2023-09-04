# Elasticsearch
1. **Windows下安装(注意es、kibana、jdk版本)**

   地址：<https://www.elastic.co/cn/downloads/past-releases#elasticsearch>

   DSL语法文档：[Elasticsearch Guide [7.17\] | Elastic](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/index.html)

2. **安装kibana(需要与es版本适配)**

   地址：<https://www.elastic.co/cn/downloads/past-releases#kibana>

3. **修改kibana配置文件**

   安装目录下的\config\kibana.yml文件，打开注解（es服务器地址）

   ```
   elasticsearch.hosts: ["http://localhost:9200"]
   ```

   ![修改配置文件](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/config.png)


4. **安装IK分词器(ik分词器版本必须与es版本完全一致，es7.17.12必须用ik7.17.12，不能用ik7.17.1)**

   1. 下载地址：https://github.com/medcl/elasticsearch-analysis-ik

   2. 常用属性：

      * ik_smart：最少切分

      * ik_max_word：最大切分

   3. **es基本概念**

      ![倒排索引执行流程](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/index.png)

      1. 文档和分词
         * 文档：每一条数据就是一个文档
         * 分词：对文档中的内容分词，得到的词语就是词条
      2. 倒排索引
         * 正向索引：基于文档id创建索引。查询词条时必须先找到文档，而后判断是否包含词条
         * 倒排索引：对文档内容分词，对词条创建索引，并记录词条所在文档的信息。查询时先根据词条查询到文档id，而后获取到文档
   
   4. **索引库**
   
      1. mapping映射，常见的属性
   
         ![mapping](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/mapping.png)
   
         特殊属性：经纬度
   
         ![geo_point](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/geo_point.png)
   
         多个查询条件，搜索一个目标：
   
         ![copy_to](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/copy_to.png)
   
         * text：分词
         * keyword：不分词
   
      2.   创建索引库 
   
         ~~~DSL
         # 创建索引库
         PUT /study
         {
           "mappings": {
             "properties": {
               "info":{
                 "type": "text",
                 "analyzer": "ik_smart",
                 "index": true
               },
               "email":{
                 "type": "keyword",
                 "index": false
               },
               "name":{
                 "type": "object",
                 "properties": {
                   "firstName": {
                     "type":"keyword",
                     "index":"true"
                   },
                   "lastName":{
                     "type": "keyword",
                     "index":"true"
                   }
                 }
               }
             }
           }
         }
         ~~~
   
      3. 查看索引库
   
         ~~~DSL
         # 查看索引库
         
         GET /study
         ~~~
   
      4. 删除索引库
   
         ~~~DSL
         # 删除索引库
         
         DELETE /study
         ~~~
   
      5. 修改索引库
   
         ~~~DSL
         # 修改索引库, 字段名已经有了是修改，没有则是添加
         
         PUT /study/_mapping
         {
           "properties":{
             "age":{
               "type":"integer"
             }
           }
         }
         ~~~
   
         6. **文档CRUD，****注意中英文输入法逗号**
   
            1. 增POST
   
               ~~~DSL
               # 新增文档
               POST /study/_doc/1
               {
                 "age":"100",
                 "email" : "123@321.com",
                 "info" : "家里蹲大学",
                 "name" : {
                   "firstName":"老",
                   "lastName":"王"
                 }
               }
               ~~~
   
            2. 删DELETE
   
               ~~~DSL
               #删除文档
               DELETE /study/_doc/1
               ~~~
   
            3. 改PUT
   
               ~~~DSL
               # 全量修改，先删除ID=1的文档信息，再插入
               PUT /study/_doc/1
               {
                 "age":"100",
                 "email" : "123@321.com",
                 "info" : "家里蹲大学",
                 "name" : {
                   "firstName":"老",
                   "lastName":"王，全量修改"
                 }
               }
               # 如果修改的文档存在，修改返回结果是update
                 "result" : "updated",
                 -----------------------------------------------------------------------------------
                 # 全量修改，修改文档不存在，修改结果为插入
               PUT /study/_doc/3
               {
                 "age":"100",
                 "email" : "123@321.com",
                 "info" : "家里蹲大学",
                 "name" : {
                   "firstName":"老",
                   "lastName":"王，全量修改，修改文档不存在"
                 }
               }
               # 如果修改的文档不存在，修改返回结果：create
                 "result" : "created",
                 -----------------------------------------
                 # 增量修改
               
               POST /study/_update/1
               {
                 "doc": {
                   "name":{
                     "lastName":"局部修改"
                   }
                 }
               }
               # 返回结果
                 "result" : "updated",
               ~~~
   
               
   
            4. 查GET
   
               ~~~DSL
               # 查看文档
               GET /study/_doc/1
               
               # 查看全部文档
               GET /study/_search
               ~~~
   
         7. **RestClient客户端：[Elasticsearch Clients | Elastic](https://www.elastic.co/guide/en/elasticsearch/client/index.html)**
         
            1. 初始化RestClient
         
               1. 引入依赖
         
                  ~~~java
                   <dependency>
                              <groupId>org.elasticsearch.client</groupId>
                              <artifactId>elasticsearch-rest-high-level-client</artifactId>
                    </dependency>
                  ~~~
         
                  
         
               2. 覆盖默认版本
         
                  ~~~java
                     <properties>
                          <java.version>1.8</java.version>
                          <elasticsearch.version>7.17.1</elasticsearch.version>
                      </properties>
                  ~~~
         
                  ~~~java
                  <dependency>
                              <groupId>com.alibaba</groupId>
                              <artifactId>fastjson</artifactId>
                              <version>1.2.76</version>
                          </dependency>
                  ~~~
         
                  
         
               3. 初始化RestHighLevelClient
         
                  ~~~java
                  RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://localhost:9200")));
                  "http://localhost:9200":填写你ES的访问地址，我的是Windows本地地址
                  ~~~
         
            2. 索引库操作
         
               ~~~java
                //创建索引库
                   @Test
                   void createUserIndex() throws IOException {
                       //1.创建request对象
                       CreateIndexRequest request = new CreateIndexRequest("user");
                       //2.准备请求参数，DSL语句
                       request.source(MAPPING_TEMPLATE, XContentType.JSON);
                       //3.发送请求
                       client.indices().create(request, RequestOptions.DEFAULT);
                       System.out.println("创建索引成功");
                   }
               
               	//删除索引库
                   @Test
                   void deleteUserIndex() throws IOException {
                       //1.创建request对象
                       DeleteIndexRequest request = new DeleteIndexRequest("user");
                       //2.发送请求
                       client.indices().delete(request, RequestOptions.DEFAULT);
                       System.out.println("删除索引成功");
                   }
               
               	 //判断索引是否存在
                   @Test
                   void existsUserIndex() throws IOException {
                       //1.创建request对象
                       GetIndexRequest request = new GetIndexRequest("user");
                       //2.发送请求
                       boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
                       System.out.println(exists ? "索引库存在":"索引库未创建");
                   }
               
                 //索引库文档的增删改查
                   //1.添加文档
                   @Test
                   void addUserDoc() throws IOException {
                       //先从数据库中查询到数据，再对数据进行操作
                       User user = esStudyService.getById(1);
                       //把java对象转换为文档类型
                       UserDoc userDoc = new UserDoc(user);
                       //1.准备request对象
                       IndexRequest request = new IndexRequest("user").id(String.valueOf(user.getId()));
                       //2.准备Json文档，使用了阿里巴巴的fastjson插件
                       request.source(JSON.toJSONString(userDoc),XContentType.JSON);
                       //3.发送请求
                       client.index(request,RequestOptions.DEFAULT);
                       System.out.println("成功插入文档数据");
                   }
               
               	 //批量导入
               	//含有字符串拼接的，比如经纬度
                   @Test
                   void addsUserToDoc() throws IOException {
                       //先查询数据库数据
                       List<User> userList = esStudyService.list();
                       //创建request对象
                       BulkRequest request = new BulkRequest();
                       //准备参数,把查询到的user对象数据，封装到UserDoc对象中
                       for (User user : userList) {
                           UserDoc userDoc = new UserDoc(user);
                           request.add(new IndexRequest("user")
                                   .id(String.valueOf(userDoc.getId()))
                                   .source(JSON.toJSONString(userDoc),XContentType.JSON));//把userDoc对象转换为JSon对象
                       }
                       //发送请求
                       client.bulk(request,RequestOptions.DEFAULT);
                   }
               
               
               	//没有字符串拼接
               	//批量导入
                   @Test
                   void addsUserToDoc() throws IOException {
                       //先查询数据库所有数据
                       List<Bank> bankList = itBankService.list();
                       //创建request对象
                       BulkRequest request = new BulkRequest();
                       //准备参数,先通过id插叙到一条java数据对象，再把对象转换为Json格式
                       for (Bank bank : bankList) {
                           request.add(new IndexRequest("bank")//bank：索引库名
                                   .id(String.valueOf(bank.getId()))
                                   .source(JSON.toJSONString(bank),XContentType.JSON));//把bank对象转换为JSon格式
                       }
                       //发送请求
                       client.bulk(request,RequestOptions.DEFAULT);
                       System.out.println("插入成功");
                   }
               	 //2.根据ID查询文档数据，转换为java对象输出
                   @Test
                   void findUserDocById() throws IOException {
                       //1.准备request对象
                       GetRequest request = new GetRequest("user", "1");
                       //2.发送请求，得到响应
                       GetResponse response = client.get(request, RequestOptions.DEFAULT);
                       //3.解析响应结果，得到json字符串信息
                       String json = response.getSourceAsString();
                       //4.把json字符串反序列化为java对象，实体类记得添加@NoArgsConstructor注解，不然会报异常
                       UserDoc userDoc = JSON.parseObject(json, UserDoc.class);
                       System.out.println(userDoc);
                   }
               	    //3.局部修改
                   @Test
                   void updateUser() throws IOException {
                       //1.准备request对象
                       UpdateRequest request = new UpdateRequest("user", "1");
                       //2.准备请求参数
                       request.doc(
                               "name","老王2.0",
                               "age","50"
                       );
                       //3.发送请求
                       client.update(request,RequestOptions.DEFAULT);
                   }
               ~~~
               
         
         8. **常用的查询语法**
         
            1. ~~~DSL
               # 查看bank所有索引，能查到所有数据，显示大概十条左右
               GET /bank/_search
               {
                 "query": {
                   "match_all": {}
                 }
               }
               
               ~~~
         
            2. ~~~DSL
               # 根据某个字段查询
               GET /bank/_search
               {
                 "query": {
                   "match": {
                     "code":"1"
                   }
                 }
               }
               ~~~
         
            3. ~~~DSL
               # 多字段查询，只要其中一个字段包含查询的条件即可
               GET /bank/_search
               {
                 "query": {
                   "multi_match": {
                     "query": "1",
                     "fields": ["name","code","content"]
                   }
                 }
               }
               ~~~
         
            4. ~~~DSL
               # term精确查询，一般查询不分词的字段，需要与查询条件一致，也可以查询分词字段，包含查询条件即可
               GET /bank/_search
               {
                 "query": {
                   "term": {
                     "name": {
                       "value": "spring"
                     }
                   }
                 }
               }
               ~~~
         
            5. ~~~DSL
               # range范围查询，>=gte()lte<= , >gt()lt<
               GET /bank/_search
               {
                 "query": {
                   "range": {
                     "code": {
                       "gte": 1,
                       "lte": 2
                     }
                   }
                 }
               }
               ~~~
         
            6. ~~~DSL
               # 地理查询,取一个点范围内
               GET /user/_search
               {
                 "query": {
                   "geo_distance":{
                     "distance":"3km",
                     "address": "20.1, 30.1"
                   }
                 }
               }
               ~~~
         
            7. ~~~DSL
               # 一个矩形内范围内
               GET /user/_search
               {
                 "query": {
                   "geo_bounding_box":{
                     "address":{
                       "top_left":{
                         "lat":20.1,
                         "lon":70.1
                       },
                       "bottom_right":{
                         "lat":19.1,
                         "lon":70.2
                       }
                     }
                   }
                 }
               }
               ~~~
         
            8. ~~~DSL
               # 排序查询，age数值相同的情况下，按id排序
               GET /user/_search
               {
                 "query": {
                   "match_all": {}
                 },
                 "sort": [
                   {
                     "age": {
                       "order": "desc"
                     },
                     "id":{
                       "order": "asc"
                     }
                   }
                 ]
               }
               ~~~
         
            9. ~~~DSL
               #地理排序查询
               GET /user/_search
               {
                 "query": {
                   "match_all": {}
                 },
                 "sort": [
                   {
                     "_geo_distance": {
                       "address": {
                         "lat": 20.1,
                         "lon": 30.1
                       },
                       "order": "asc",
                       "unit": "km"
                     }
                   }
                 ]
               }
               ~~~
         
            10. ~~~DSL
                # 分页查询 (page-1)*size
                GET /bank/_search
                {
                  "query": {
                    "match_all": {}
                  },
                  "sort": [
                    {
                      "code": {
                        "order": "asc"
                      }
                    }
                  ],
                  "from": 8,
                  "size": 8
                }
                ~~~
         
            11. ~~~DSL
                # 高亮搜索默认，<em></em>，需要自己在CSS样式里面编写格式
                GET /user/_search
                {
                  "query": {
                    "match": {
                      "all": "老王"
                    }
                  },
                  "highlight": {
                    "fields": {
                      "name": {
                        "pre_tags": "<em>",
                        "post_tags": "</em>", 
                        "require_field_match": "false"
                      }
                    }
                  }
                }
                
                # 高亮搜索,搜搜字段要与高亮字段完全匹配，可以 "require_field_match": "false"指定为false取消完全匹配
                # 
                GET /user/_search
                {
                  "query": {
                    "match": {
                      "all": "老王"
                    }
                  },
                  "highlight": {
                    "fields": {
                      "name": {
                        "require_field_match": "false"
                      }
                    }
                  }
                }
                ~~~
         
         9. **聚合**
         
            1. 什么是聚合：
         
               - 对文档数据的统计、分析、计算
         
            2. 聚合常见的种类：
         
               - Bucket：对文档数据分组，统计每组数量
               - Metric：对文档数据计算，例如升降序
               - Pipeline：基于其他聚合结果再做聚合
         
            3. 参与聚合运算的字段类型：
         
               - keyword
         
               - 数值
         
               - 日期
         
               - 布尔
         
                 ![juhe](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/juhe.png)
         
               
         
            4. Bucket：
         
               1. ~~~DSL
                  # Bucket聚合,第一个size现实源文档数据，0不显示，第二个size显示聚合搜索的分类数量，2显示两个
                  GET /bank/_search
                  {
                    "size": 0,
                    "aggs": {
                      "nameAgg": { #聚合名称
                        "terms": {
                          "field": "code", # 需要运算的字段，es7最好用keyword类型的字段
                          "size": 8,
                          "order": {
                            "_key": "desc" # 搜索结果的字段，不是源文档数据字段
                          }
                        }
                      }
                    }
                  }
                  ~~~
         
               2.  ~~~DSL
                   # 限定聚合范围
                   GET /bank/_search
                   {
                     "query": {
                       "range": {
                         "code": {
                           "lte": 4
                         }
                       }
                     },
                     "size": 32,
                     "aggs": {
                       "idAgg": {
                         "terms": {
                           "field": "id",
                           "size": 8
                         }
                       }
                     }
                   }
                   ~~~
         
               3. 
         
            5. Metric：
         
               ![Metrics1.0](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/Metrics1.0.png)
         
               ​		
         
               ​		![Metric2.0](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/Metric2.0.png)
         
            6. pipeline：
         
         10. **自动补全搜索**
         
         11. **数据同步**
         
             1. MQ消息队列
         
         12. **集群部署**：https://github.com/lmenezes/cerebro监测工具
         
             1. 集群节点
         
                ![jiedian](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/jiedian.png)
         
                - master node---主节点：
                - master eligible---候选主节点：
         
                - data node---数据节点：
         
                - ingest node---辅助节点：
         
                - coordinating node---协调节点：协调分片搜索和聚合操作
         
                - client node---客户端节点：处理客户的请求
         
                  
         
             2. 脑裂问题：因为一些原因，比如网络问题，导致其他节点连不上主节点，剩下的节点又重新选举了一个节点作为主节点，当故障修复后，出现两个主节点，导致数据的不一致，这就是脑裂问题
         
                ![naolie](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/naolie.png)
         
             3. 分布式查询和储存
         
                ![fenbushichucun](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/fenbushichucun.png)
         
                通过id用hash算法得到一个值，再对值进行%（取余）分片数，列如三个分片，对3取余有三个结果：0、1、2，分别对应三个分片，然后把数据储存到得到对应结果的分片中
         
                **分布式储存举例：**
         
                ![liezi](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/liezi.png)
         
             4. 分布式查询
         
                ![fenbushichaxun](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/fenbushichaxun.png)
         
                
         
             5. 总结
         
                ![zongjie](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/zongjie.png)
         
             6. 主节点故障转移
         
                ![guzhangzhuanyi](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/guzhangzhuanyi.png)
         
                总结：
         
                ![zongjie01](https://github.com/YyangZhiHeng/Elasticsearch/blob/master/picture/zongjie01.png)
         
         13. 
