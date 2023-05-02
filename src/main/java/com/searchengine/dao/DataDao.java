package com.searchengine.dao;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.searchengine.entity.Data;
import com.searchengine.entity.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataDao extends BaseMapper<Data> {

    List<Record> selectAllRecords();

    List<Record> selectPartialRecords(@Param("limit") int limit, @Param("offset") int offset);

    List<Record> selectRecordsByWord(String word);

    Record selectById(@Param("id") int id);

    int insertRecord(Record record);

    List<Data> getSomeDatas(@Param("limit") int limit, @Param("offset") int offset);

    //用于search搜索
    List<Data> getDataBySplit(@Param("sql")String sql, @Param("pageSize")int pageSize, @Param("offset")int offset);

    List<Data> getCompleteDataBySplit(@Param("sql")String sql, @Param("pageSize")int pageSize, @Param("offset")int offset);

    //更新热门数据-一般是每两小时更新一次,
    // 其中clearHotdata是将hotdata表清空
    // updateHotdata是将data表点击量最高的十个插入hotdata表中
    // clearCount是将点击量全部改0重新计算
    boolean clearHotdata();

    boolean updateHotdata();

    boolean clearCount();

    //增加点击量
    boolean addCount(@Param("id")Integer id);

    //添加政策
    boolean addData(@Param("policyId")Integer policyId,
                    @Param("policyTitle")String policyTitle,
                    @Param("policyGrade")String policyGrade,
                    @Param("pubAgencyId")String pubAgencyId,
                    @Param("pubAgency")String pubAgency,
                    @Param("pubAgencyFullname")String pubAgencyFullname,
                    @Param("pubNumber")String pubNumber,
                    @Param("pubTime")String pubTime,
                    @Param("policyType")String policyType,
                    @Param("policyBody")String policyBody,
                    @Param("province")String province,
                    @Param("city")String city,
                    @Param("policySource")String policySource,
                    @Param("pubTimeYear")Integer pubTimeYear);
    //获得热门数据
    List<Data> getHotdata();

    //得到政策与搜索词之间的相关性，用于search搜索的打分模块
    List<Data>getDataRelevance(@Param("sql")String sql);

    //得到政策与搜索词之间的相关性，带筛选的,用于search搜索的打分模块
    List<Data>getDataRelevanceLimit(@Param("sql1")String sql1,@Param("sql2")String sql2);

    //从statistic_data表中获得政策总数
    int getNumberOfData();
    void updateNumberOfData(@Param("num") Integer num);
    void increDataByID(@Param("id") String id);

    //从statistic_data表中获得政策title的总长度
    int getTitleTotalLength();
    void updateTitleTotalLength(@Param("length") double length);
}
