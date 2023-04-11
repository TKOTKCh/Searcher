package com.searchengine.springboot;

import com.searchengine.dao.SegmentDao;
import com.searchengine.entity.Segment;
import com.searchengine.utils.Trie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class CodeCacheTest {

    /**
     * 踩坑，写测试一定记得加上@SpringBoot的注解
     * 然后一般像这个的对应函数内部有注解（@Autowrited）
     * 最好像这个测试用例一样copy过来进行测试
     * */
    @Autowired
    private SegmentDao segmentDao;

    String query = "蚂";

    public Trie init(){
        List<Segment> segmentations = segmentDao.getAllSegByTableName("segment_datatitle");
        Trie trie = new Trie();
        for (Segment segmentation : segmentations) {
            String word = segmentation.getWord();
            trie.add(word);
        }
        return trie;
    }
    @Test
    public void testTrie(){
        Trie trie = init();
        List<String> prefixWord = trie.getRelatedWords(query);
        System.out.println("prefixWord 如下所示:");
        System.out.println(prefixWord);
    }
}
