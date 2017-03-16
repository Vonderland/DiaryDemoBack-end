package com.demo.server.dao;

import com.demo.server.bean.Request;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Repository
public interface RequestDao {
    int insertRequest(Request request);
    Request selectRequestByIds(@Param("fromId")long fromId, @Param("toId")long toId);
    Request selectRequestByToId(@Param("toId")long toId);
    int acceptRequest(@Param("id")long id);
    int rejectRequest(@Param("id")long id);
    Request selectRequestById(@Param("id")long id);
}
