package com.searchengine.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.searchengine.dto.RecordDto;
import com.searchengine.entity.Data;
import com.searchengine.entity.Record;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DataService {

    List<Record> queryAllRecord();

    List<Record> selectPartialRecords(int limit, int offset);

    List<Record> queryRecordByWord(String word);

    List<Record> queryRecordFilter(String word);

    List<RecordDto> search(String searchInfo);

    Boolean addRecord(Record record);


    List<Data> getSomeDatas(int limit, int offset);

    IPage<Data> findPage(Integer pageNum, Integer pageSize, String username, String email, String phone, String address);

    Map<String, Object> getHotdata() ;
    boolean updateHotdata();
    boolean judgeUpdate(double currentTime);
    boolean addCount(Integer id);
}
