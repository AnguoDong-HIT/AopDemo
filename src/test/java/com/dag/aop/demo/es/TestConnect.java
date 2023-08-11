package com.dag.aop.demo.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetMappingResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.alibaba.fastjson.JSON;
import com.dag.aop.demo.TestBase;
import com.dag.aop.demo.domain.po.Sku;
import com.dag.aop.demo.mapper.SkuMapper;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestConnect extends TestBase {
  @Autowired private ElasticsearchClient elasticsearchClient;

  @Autowired private SkuMapper skuMapper;

  /**
   * indices即目录directory
   *
   * @throws IOException
   */
  @Test
  public void testCreateIndices() throws IOException {
    BooleanResponse is_exists = elasticsearchClient.indices().exists(e -> e.index("products"));
    if (is_exists.value()) {
      System.out.println("indices exist");
    } else {
      CreateIndexResponse response = elasticsearchClient.indices().create(c -> c.index("products"));
      log.info(JSON.toJSONString(response));
    }
  }

  @Test
  public void testDeleteIndices() throws IOException {
    elasticsearchClient.indices().delete(d -> d.index("products"));
  }

  @Test
  public void testSearch() throws IOException {

    GetResponse<Sku> skuGetResponse =
        elasticsearchClient.get(g -> g.index("products").id("100000022652"), Sku.class);
    System.out.println(skuGetResponse);

    SearchResponse<Sku> search =
        elasticsearchClient.search(
            s ->
                s.index("products")
                    .query(
                        q ->
                            q.bool(
                                b ->
                                    b.must(must -> must.match(m -> m.field("name").query("华为")))
                                        .must(
                                            must ->
                                                must.match(
                                                    m -> m.field("categoryName").query("手机"))))),
            Sku.class);
    //            MatchQuery matchQuery = new
    // MatchQuery.Builder().query("name").field("华为").build();
    //            SearchRequest searchRequest  = new
    // SearchRequest.Builder().index("products").query(new
    //            Query.Builder().match(matchQuery).build()).build();
    //            elasticsearchClient.search(searchRequest, Sku.class);
    System.out.println(search.hits().hits().size());
    for (Hit<Sku> hit : search.hits().hits()) {
      System.out.println(hit.source());
    }
  }

  @Test
  public void testSearchAll() throws IOException {
    MatchAllQuery matchAllQuery = new MatchAllQuery.Builder().build();
    SearchResponse<Sku> response =
        elasticsearchClient.search(
            s -> s.index("products").query(q -> q.matchAll(matchAllQuery)).size(10000), Sku.class);
    List<Hit<Sku>> hits = response.hits().hits();
    System.out.println(hits.size());
  }

  @Test
  public void testMapping() throws IOException {
    GetMappingResponse mapping = elasticsearchClient.indices().getMapping(g -> g.index("products"));
    Map<String, Property> map = Maps.newHashMap();
    map.put("name", Property.of(p -> p.text(t -> t.index(true).store(true))));
    map.put("id", Property.of(p -> p.keyword(t -> t.index(true).store(true))));
    map.put("price", Property.of(p -> p.integer(t -> t.index(true).store(true))));

    CreateIndexResponse response =
        elasticsearchClient.indices().create(c -> c.index("test").mappings(m -> m.properties(map)));
    System.out.println(mapping);
  }

  @Test
  public void testSearchMultiQuery() throws IOException {
    SearchResponse<Sku> multiQueryResponse =
        elasticsearchClient.search(
            s ->
                s.index("products")
                    .query(
                        q ->
                            q.multiMatch(
                                m -> m.fields("name", "categoryName", "brandName^10").query("华为")))
                    .size(100),
            Sku.class);
    List<Hit<Sku>> hits = multiQueryResponse.hits().hits();
    hits.forEach(System.out::println);
  }

  @Test
  public void testSearchBoostingQuery() throws Exception {
    SearchResponse<Sku> boostQueryResponse =
        elasticsearchClient.search(
            s ->
                s.index("products")
                    .size(100)
                    .query(
                        q ->
                            q.boosting(
                                b ->
                                    b.positive(p -> p.match(m -> m.field("brandName").query("华为")))
                                        .negative(
                                            n ->
                                                n.range(
                                                    r ->
                                                        r.field("price")
                                                            .from("10000")
                                                            .to("100000")))
                                        .negativeBoost(0.2))),
            Sku.class);
    List<Hit<Sku>> hits = boostQueryResponse.hits().hits();
    hits.forEach(System.out::println);
  }
}
