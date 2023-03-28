package com.search;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.search.utils.jieba.keyword.Keyword;
import com.search.utils.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class testTopN {

    @Test
    public  void testTFIDF(){
        System.out.println("\n------------------test TFIDF\n");
        //        String content="孩子上了幼儿园 安全防拐教育要做好";
        String content="孩子上了幼儿园 安全防拐教育要做好";
        int topN=5;
        TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
        List<Keyword> list=tfidfAnalyzer.analyze(content,topN);
        for(Keyword word:list)
            System.out.print(word.getName()+":"+word.getTfidfvalue()+",");
    }



    @Test
    public  void testDemo() {
        System.out.println("\n------------------test index\n");
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的",
                        "蚂蚁摄影宝典上册 64个生活场景学参数 入门书籍教程拍摄技巧",
                        "微距蚂蚁续集"
        };
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
    }

    @Test
    public  void testDemoSearch() {
        System.out.println("\n------------------test search\n");
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的",
                        "蚂蚁摄影宝典上册 64个生活场景学参数 入门书籍教程拍摄技巧",
                        "微距蚂蚁续集"
                };
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH).toString());
        }
    }

}
