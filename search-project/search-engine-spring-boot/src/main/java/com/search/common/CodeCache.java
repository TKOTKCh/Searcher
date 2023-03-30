package com.search.common;

import com.search.dao.SegmentDao;
import com.search.entity.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import com.search.TrieTree.Trie;
@Component
public class CodeCache {

    @Autowired
    private SegmentDao segmentDao;


    public Trie init(){
        List<Segment> segmentations = segmentDao.getAllSeg();
        Trie trie = new Trie();
        for (Segment segmentation : segmentations) {
            String word = segmentation.getWord();
            trie.add(word);
        }
        return trie;
    }
}
