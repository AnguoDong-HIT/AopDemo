package com.dag.aop.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

  @Bean
  public ElasticsearchClient elasticsearchClient() {
    String serverUrl = "http://localhost:9200";
    String apiKey = "";

    // Create the low-level client
    RestClient restClient =
        RestClient.builder(HttpHost.create(serverUrl))
            //                .setDefaultHeaders(new Header[]{
            //                        new BasicHeader("Authorization", "ApiKey " + apiKey)
            //                })
            .build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport =
        new RestClientTransport(restClient, new JacksonJsonpMapper());

    // And create the API client

    return new ElasticsearchClient(transport);
  }

  @Bean
  public ElasticsearchAsyncClient asyncClient() {
    String serverUrl = "http://localhost:9200";
    RestClient restClient = RestClient.builder(HttpHost.create(serverUrl)).build();
    ElasticsearchTransport transport =
        new RestClientTransport(restClient, new JacksonJsonpMapper());
    return new ElasticsearchAsyncClient(transport);
  }
}
