package com.searchengine.service;

import com.searchengine.dto.DataDto;
import com.searchengine.entity.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {

    List<Data> queryAllRecord();

    List<Data> selectPartialRecords(int limit, int offset);

    List<Data> queryRecordByWord(String word);

    List<Data> queryRecordFilter(String word);

    List<DataDto> search(String searchInfo);

    Boolean addRecord(Data data);

}
