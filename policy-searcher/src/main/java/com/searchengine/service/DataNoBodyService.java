package com.searchengine.service;

import com.searchengine.entity.DataNoBody;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataNoBodyService {
    List<DataNoBody> getAllDataNoBodys();
}
