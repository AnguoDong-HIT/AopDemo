package com.dag.aop.demo;

import com.dag.aop.demo.mapper.SkuMapper;
import com.dag.aop.demo.domain.po.Sku;
import com.dag.aop.demo.domain.example.SkuExample;
import com.google.common.collect.Lists;
import com.huaban.analysis.jieba.JiebaSegmenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author: donganguo
 * @date: 2021/6/8 11:07 上午
 * @Description:
 */
@Slf4j
public class TestWriter extends TestBase {

    @Autowired
    private SkuMapper skuMapper;

    private static final String INDEX_DIR = "Documents/sku_dir";

    private static final String INDEX_DIR_JIEBA = "Documents/sku_dir_jieba";

    @Test
    public void TestCreateIndex() throws Exception {
        //采集数据，创建文档对象，创建分词，创建目录对象，创建IndexWriterConfig，创建IndexWriter，写入到文档集，释放资源
        List<Sku> skuList = skuMapper.selectByExample(new SkuExample());

        List<Document> documents = Lists.newArrayList();

        for (Sku sku : skuList) {
            Document document = new Document();
            // 文档添加字段
            // 字段 是否分词，是否索引，是否存储
            document.add(new StringField("id", sku.getId(), Field.Store.YES));
            document.add(new TextField("name", sku.getName(), Field.Store.YES));

            // 价格范围查询需分词，数值类型分词,索引不存储
            document.add(new IntPoint("price", sku.getPrice()));
            document.add(new StoredField("price", sku.getPrice()));


            document.add(new StoredField("image", sku.getImage()));
            document.add(new StringField("categoryName", sku.getCategoryName(), Field.Store.YES));
            document.add(new StringField("brandName", sku.getBrandName(), Field.Store.YES));
            documents.add(document);
        }
        log.info("documents.size(): {}", documents.size());

        // 分词器,目录，IndexWriter
        try (Analyzer analyzer = new IKAnalyzer()) {
            Directory directory = MMapDirectory.open(Paths.get(INDEX_DIR));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            // addDocument和addDocuments分段不一样
            for (Document document : documents) {
                indexWriter.addDocument(document);
            }

            indexWriter.close();
        }
    }

    @Test
    public void testUpdateIndex() {
        Document document = new Document();
        // 文档添加字段
        // 字段 是否分词，是否索引，是否存储
        document.add(new StringField("id", "100000003145", Field.Store.YES));
        document.add(new TextField("name", "xiaomi 15", Field.Store.YES));

        // 价格范围查询需分词，数值类型分词索引不存储
        document.add(new IntPoint("price", 5000));
        document.add(new StoredField("price", 5000));


        document.add(new StoredField("image", "xiaomi.jpg"));
        document.add(new StringField("categoryName", "phone", Field.Store.YES));
        document.add(new StringField("brandName", "xiaomi", Field.Store.YES));

        try (Analyzer analyzer = new IKAnalyzer()) {
            Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            TermQuery termQuery = new TermQuery(new Term("id", "100000003145"));
            TopDocs topDocs = searcher.search(termQuery, 2);
            Arrays.stream(topDocs.scoreDocs).forEach(scoreDoc -> {
                try {
                    Document doc = searcher.doc(scoreDoc.doc);
                    System.out.println(doc);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.updateDocument(new Term("id", "100000003145"), document);
            log.info("update {}", document);

            indexWriter.commit();
            indexWriter.close();

            // commit之后不重启应用也拿不到新的？
            reader = DirectoryReader.open(directory);
            IndexSearcher newSearcher = new IndexSearcher(reader);
            topDocs = newSearcher.search(termQuery, 2);
            Arrays.stream(topDocs.scoreDocs).forEach(scoreDoc -> {
                try {
                    Document doc = searcher.doc(scoreDoc.doc);
                    System.out.println(doc);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeleteIndex() {
        try (Analyzer analyzer = new StandardAnalyzer()) {
            Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            Term term = new Term("id", "998188");
            indexWriter.deleteDocuments(term);
            log.info("delete documents by term {}", term);
            indexWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDemo() {
        var segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
    }

}