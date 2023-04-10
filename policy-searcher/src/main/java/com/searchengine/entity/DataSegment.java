package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// DataSegment表的Java原始数据
public class DataSegment {
    Integer dataId;//对应的政策的id，这里的dataid对应的是data表的id列，而不是policy_id
    Integer segId;//分词的id
    Double tf;//编号为segid的分词在编号为dataid的政策信息中的tf值
    Double idf;//编号为segid的分词的idf值
    Double bm25;//编号为segid的分词在编号为dataid的政策信息中的bm25值
    Integer count;//编号为segid的分词在编号为dataid的政策信息中的出现次数

}
