package com.dag.aop.demo;

import com.dag.aop.demo.convert.SkuConvert;
import com.dag.aop.demo.domain.dto.SkuDTO;
import com.dag.aop.demo.domain.po.Sku;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TestSearch extends TestBase {

  private static final String INDEX_DIR = "Documents/sku_dir";

  @Test
  public void testIndexSearch() {
    // 1.创建分词器，对搜索对关键词进行分词使用
    try (Analyzer analyzer = new IKAnalyzer()) {
      // 2.创建查询对象
      QueryParser queryParser = new QueryParser("name", analyzer);
      // 3.创建搜索query
      Query name = queryParser.parse("华为手机");
      PhraseQuery.Builder phraseQueryBuilder = new PhraseQuery.Builder();
      phraseQueryBuilder.add(new Term("name", "华为手机"));

      Query termQuery = new TermQuery(new Term("name", "华为手机"));
      Query fuzzyQuery = new FuzzyQuery(new Term("name", "华为手机"), 2);

      // 多字段优先级查询
      String[] fields = {"name", "categoryName", "brandName"};
      Map<String, Float> boosts = Maps.newHashMap();
      boosts.put("brandName", 1000000f);
      MultiFieldQueryParser multiFieldQueryParser =
          new MultiFieldQueryParser(fields, analyzer, boosts);
      Query multiQuery = multiFieldQueryParser.parse("华为");

      Query price = IntPoint.newRangeQuery("price", 100, 1000);
      // 查询条件的与或非,过滤出。非不能单独查询
      BooleanQuery.Builder query = new BooleanQuery.Builder();
      query.add(name, BooleanClause.Occur.MUST);
      query.add(price, BooleanClause.Occur.MUST);

      // 4.创建Directory目录对象，指定索引库位置
      Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));
      // 5.输入流
      IndexReader indexReader = DirectoryReader.open(directory);
      // 6.搜索
      IndexSearcher indexSearcher = new IndexSearcher(indexReader);
      TopDocs topDocs = indexSearcher.search(multiQuery, 50);
      System.out.println("topDocs.totalHits: " + topDocs.totalHits);
      Arrays.stream(topDocs.scoreDocs)
          .forEach(
              scoreDoc -> {
                try {
                  Document doc = indexSearcher.doc(scoreDoc.doc);
                  System.out.println(doc);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });

      indexReader.close();
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test() {
    Sku sku = Sku.builder().name("sku").build();
    SkuDTO skuDTO = SkuConvert.INSTANCE.toDto(sku);
    System.out.println(skuDTO);
  }
}
