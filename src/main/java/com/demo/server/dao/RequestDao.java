package com.demo.server.dao;

import com.demo.server.bean.Request;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Repository
public interface RequestDao {
    int insertRequest(Request request);
    Request selectRequestByFromId(long fromId);
    Request selectRequestByToId(long toId);
    int acceptRequest(long id);
    int rejectRequest(long id);
}
