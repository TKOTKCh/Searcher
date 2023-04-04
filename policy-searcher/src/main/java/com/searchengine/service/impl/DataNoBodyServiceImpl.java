package com.searchengine.service.impl;

import com.searchengine.dao.DataNoBodyDao;
import com.searchengine.entity.DataNoBody;
import com.searchengine.service.DataNoBodyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataNoBodyServiceImpl implements DataNoBodyService {
    @Autowired
    private DataNoBodyDao dataNoBodyDao;

    // 搜索Data
    @Override
    public List<DataNoBody> getAllDataNoBodys(String table) {
        return dataNoBodyDao.getAllDataNoBodys(table);
    }
}
