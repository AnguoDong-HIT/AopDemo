package com.dag.aop.demo.es;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.alibaba.fastjson.JSON;
import com.dag.aop.demo.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestSearch extends TestBase {

  @Autowired private ElasticsearchAsyncClient asyncClient;

  @Autowired private ElasticsearchClient esClient;

  @Test
  public void testAsync() throws Exception {
    log.info(String.valueOf(esClient.exists(b -> b.index("products").id("foo")).value()));

    asyncClient
        .exists(b -> b.index("products").id("foo"))
        .whenComplete(
            (response, exception) -> {
              if (exception != null) {
                log.error("Failed to index", exception);
              } else {
                log.info(JSON.toJSONString(response));
                log.info("Product exists");
              }
            });
    Thread.sleep(5000L);
  }
}
