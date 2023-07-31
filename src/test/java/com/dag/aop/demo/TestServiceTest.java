package com.dag.aop.demo;

import com.dag.aop.demo.mapper.SkuMapper;
import com.dag.aop.demo.pojo.Sku;
import com.dag.aop.demo.pojo.SkuExample;
import com.dag.aop.demo.pojo.User;
import com.dag.aop.demo.service.TestService;
import com.dag.aop.demo.service.UserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author: donganguo
 * @date: 2021/6/8 11:07 上午
 * @Description:
 */
@Slf4j
public class TestServiceTest extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceTest.class.getName());


    @Resource
    private TestService testService;

    @Resource
    private UserService userService;

    @Autowired
    private SkuMapper skuMapper;

    @Test
    public void testGetString() {
        System.out.println(testService.getSting());
    }

    @Test
    public void test() {
        userService.insertSelective(User.builder()
                .id(3)
                .userId("3")
                .username("ag")
                .password("admin")
                .build());
        System.out.println(userService.selectByPrimaryKey(3));;
    }

    @Test
    public void createIndexTest() throws Exception {
        //采集数据，创建文档对象，创建分词，创建目录对象，创建IndexWriterConfig，创建IndexWriter，写入到文档集，释放资源
        List<Sku> skuList = skuMapper.selectByExample(new SkuExample());

        List<Document> documents = Lists.newArrayList();

        for (Sku sku : skuList) {
            Document document = new Document();
            // 域对象
            document.add(new TextField("id", sku.getId(), Field.Store.YES));
            document.add(new TextField("name", sku.getName(), Field.Store.YES));
            document.add(new TextField("price", String.valueOf(sku.getPrice()), Field.Store.YES));
            document.add(new TextField("image", sku.getImage(), Field.Store.YES));
            document.add(new TextField("categoryName", sku.getCategoryName(), Field.Store.YES));
            document.add(new TextField("brandName", sku.getBrandName(), Field.Store.YES));
            documents.add(document);
        }
        System.out.println(documents.size());
        // 分词器,目录，indexWriter
        try (Analyzer analyzer = new StandardAnalyzer()) {
            Directory directory = FSDirectory.open(Paths.get("Documents/sku_dir"));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            // addDocument和addDocuments分段不一样
            for (Document document : documents) {
                indexWriter.addDocument(document);
            }

            indexWriter.close();
        }
    }
}