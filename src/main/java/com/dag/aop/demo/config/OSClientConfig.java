// package com.dag.aop.demo.config;
//
// import org.apache.http.HttpHost;
// import org.opensearch.client.RestClient;
// import org.opensearch.client.json.jackson.JacksonJsonpMapper;
// import org.opensearch.client.opensearch.OpenSearchClient;
// import org.opensearch.client.transport.rest_client.RestClientTransport;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class OSClientConfig {
//
//  @Bean
//  public OpenSearchClient osClient() {
//    String serverUrl = "http://localhost:9600";
//
//    // Create the low-level client
//    RestClient restClient = RestClient.builder(HttpHost.create(serverUrl)).build();
//
//    // Create the transport with a Jackson mapper
//
//    RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//
//    // And create the API client
//
//    return new OpenSearchClient(transport);
//  }
// }
