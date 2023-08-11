package com.dag.aop.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

public class TestAnalyzer extends TestBase{
    private static final String INDEX_DIR_TEST = "Documents/sku_dir_test";
    @Test
    public void testCJKAnalyzer() throws IOException {
        try (Analyzer analyzer = new IKAnalyzer()) {
            Directory directory = FSDirectory.open(Paths.get(INDEX_DIR_TEST));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            Document document = new Document();
            document.add(new IntPoint("price", 18));
            document.add(new StoredField("price", 18));
            indexWriter.addDocument(document);
            indexWriter.close();
        }
    }
    
    @Test
    public void test() throws ParseException, IOException {
        // 创建查询解析器
        QueryParser parser = new QueryParser("price", new IKAnalyzer());

        // 创建查询对象
        Query query = IntPoint.newExactQuery("price", 18);

        // 使用查询解析器解析查询语句
        Query parsedQuery = parser.parse("price:18");

        // 创建索引搜索器
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIR_TEST));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(indexReader);

        // 执行查询
        TopDocs results = searcher.search(query, 10);

        // 遍历结果集
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            // 获取文档编号
            int docId = scoreDoc.doc;
            // 获取文档得分
            float score = scoreDoc.score;
            // 获取文档
            Document doc = searcher.doc(docId);
            // 获取IntPoint字段的值
            int price = doc.getField("price").numericValue().intValue();
            // 处理文档
            System.out.println("docId:" + docId + ", score:" + score + ", price:" + price);
        }

        // 关闭索引搜索器
        indexReader.close();
    }
}
