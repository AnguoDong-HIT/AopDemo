package com.dag.aop.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class TestQueryParser extends TestBase{
    @Test
    public void testQueryParser(){
        try (Analyzer analyzer = new IKAnalyzer()) {
            QueryParser queryParser = new QueryParser("name", analyzer);
            Query query = queryParser.parse("小米手机");
            System.out.println(query);
            System.out.println(query.getClass());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
