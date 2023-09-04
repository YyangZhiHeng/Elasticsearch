package com.study;

import com.alibaba.fastjson.JSON;
import com.study.pojo.User;
import com.study.pojo.UserDoc;
import com.study.service.EsStudyService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static com.study.pojo.UserConstants.MAPPING_TEMPLATE;

@SpringBootTest
public class UserRestClientTest {
    private RestHighLevelClient client;
    @Autowired
    private EsStudyService esStudyService;

    @Test
    void testCreate(){
        System.out.println(client);
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
        //2.准备Json文档
        request.source(JSON.toJSONString(userDoc),XContentType.JSON);
        //3.发送请求
        client.index(request,RequestOptions.DEFAULT);
        System.out.println("成功插入文档数据");
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
        //4.把json字符串反序列化为java对象
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

    //4.根据ID删除
    @Test
    void deleteUserById() throws IOException {
        DeleteRequest request = new DeleteRequest("user", "1");
        client.delete(request,RequestOptions.DEFAULT);
        System.out.println("删除成功");
    }

    //批量导入
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

    @BeforeEach
    void setUp(){
        this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://localhost:9200")));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }
}
