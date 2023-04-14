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

    List<Data> getHotdata() ;
    boolean updateHotdata();
    boolean judgeUpdate(double currentTime);
    boolean addCount(Integer id);
    boolean addData(
                    Integer policyId,
                    String policyTitle,
                    String policyGrade,
                    String pubAgencyId,
                    String pubAgency,
                    String pubAgencyFullname,
                    String pubNumber,
                    String pubTime,
                    String policyType,
                    String policyBody,
                    String province,
                    String city,
                    String policySource,
                    String pubTimeYear
                    );
}
