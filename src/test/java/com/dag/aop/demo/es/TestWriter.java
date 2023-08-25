package com.dag.aop.demo.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.dag.aop.demo.TestBase;
import com.dag.aop.demo.domain.example.SkuExample;
import com.dag.aop.demo.domain.po.Sku;
import com.dag.aop.demo.mapper.SkuMapper;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestWriter extends TestBase {
  @Autowired private ElasticsearchClient esClient;

  @Autowired private SkuMapper skuMapper;

  @Test
  public void testCreateDocuments() throws IOException {
    List<Sku> skuList = skuMapper.selectByExample(new SkuExample());
    //        BulkRequest.Builder builder = new BulkRequest.Builder();
    BulkIngester<Object> ingester =
        BulkIngester.of(
            b -> b.client(esClient)
            //                .maxOperations(10000)
            //                .flushInterval(1, TimeUnit.SECONDS)
            );
    System.out.println(skuList.size());
    for (Sku sku : skuList) {
      ingester.add(op -> op.index(i -> i.index("products").id(sku.getId()).document(sku)));
    }
  }

  @Test
  public void testAddDocument() throws IOException {
    //    new CreateIndexRequest.Builder().mappings(m -> m.properties("products", p -> p.))
    Sku sku = Sku.builder().name("xiaomi").build();
    IndexResponse response = esClient.index(i -> i.index("product").document(sku));
  }
}
