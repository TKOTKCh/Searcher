package com.search.BM25;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BM25Test {
    @Test
    public void test(){
        BM25 bm25= new BM25(1.2,0.75);

        String keywords = "租房";

        List<String> docs = new ArrayList<String>();
        docs.add("深圳保障房计划给出最新公租房、安居房消息！没房的赶紧来看！");
        docs.add("在深圳，有多少人每个月最大的一笔支出就是房租。所以大家都挺关心公租房消息的，毕竟公租房能让房租这笔支出少一些又或者是期待安居房能让自己有点买房的机会。");
        docs.add("深圳的保障房工作近几年进展就很不错。从最初的廉租房、公租房、经济适用住房，发展到今天的公租房、安居房和人才住房。");
        docs.add("保障群体从最初的户籍低收入家庭，扩展到现在的户籍中低收入家庭、人才家庭，以及为城市提供基本公共服务的公交司机、环卫工人和先进制造业职工等群体");
        docs.add("好消息，新版租房合同来袭，在深圳租房的你有福了！");

        Map<String,Double> idfMap = new HashMap<String,Double>();
        loadIDFMap(idfMap, this.getClass().getResourceAsStream("/jieba/idf_dict.txt"));

        int i = 0;
        for(String doc:docs){
            i++;
            System.out.println(keywords+"第"+ i +"-bm25计算："+bm25.cal(keywords, doc, docs, idfMap));
        }

    }

    public static void loadIDFMap(Map<String, Double> map, InputStream in){
        BufferedReader bufr;
        try
        {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line=null;
            while((line=bufr.readLine())!=null) {
                String[] kv=line.trim().split(" ");
                map.put(kv[0],Double.parseDouble(kv[1]));
            }
            try
            {
                bufr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
