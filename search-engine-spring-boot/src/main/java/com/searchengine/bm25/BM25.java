package com.searchengine.bm25;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * BM25 相关度算法实现
 * 公式：   Similarity(word|documents)=IDFScore∗(k+1)∗tf / k∗(1.0−b+b∗|D|avgDl)+tf
 */
public class BM25 {
    //常量k，用来限制TF值的增长极限 默认1.2
    private double k = 1.2;
    //b是一个常数，它的作用是规定L对评分的影响有多大
    private double b;

    /**
     * BM25 相关度算法实现
     * @param k 常量k，用来限制TF值的增长极限 默认1.2
     * @param b b是一个常数，它的作用是规定L对评分的影响有多大
     */
    public BM25(double k, double b) {
        super();
        this.k = k;
        this.b = b;
    }

    /**
     * BM25 相关度算法实现 k=1.2
     * @param b b是一个常数，它的作用是规定L对评分的影响有多大
     */
    public BM25(double b) {
        super();
        this.b = b;
    }

    /**
     * 计算 关键词 在文档中的相关值
     * @param idf 关键词的逆文档频率
     * @param tf  关键词在文档中的词频
     * @param L  当前文档长度/平均文档长度
     * @return
     */
    public double cal(double idf,double tf,double L){
        double v = 0;
        v= (idf*(k+1)*tf) /(k * (1.0-b+b*L)+tf);
        return v;
    }

    /**
     * 计算 关键词 在文档中的相关值
     * @param keywords 关键词
     * @param doc 关键词所在文档的全文
     * @param docs 所有文档集
     * @param idfMap idf字典表
     * @return
     */
    public double cal(String keywords,String doc,List<String> docs,Map<String,Double> idfMap){
        Double idf = idfMap.get(keywords);
        if(null==idf) idf=1.0d;
        Double tf =  (double)StringUtils.countMatches(doc, keywords)*keywords.length() / doc.length();
        double avgLength = calAvgLength(docs);
        Double L =  (double)doc.length()/avgLength;
        return cal(idf,tf,L);
    }

    private double calAvgLength(List<String> docs) {
        if(docs==null || docs.size() == 0) {
            throw new RuntimeException("给定文档集不能为空");
        }

        int s =  0;
        for(String d:docs){
            s+=d.length();
        }
        return (double)s/(double)docs.size();
    }

}

//    public static int count (String source, String key) {
//        int key_index = source.indexOf(key);//key第一个出现的索引位置
//        int count = 0;
//        while (key_index != -1) {
//            key_index = source.indexOf(key, key_index + 1);//从这个索引往后开始第一个出现的位置
//            count += 1;
//        }
//        return count;
//    }
