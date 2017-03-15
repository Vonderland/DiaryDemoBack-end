package com.demo.server.dao;

import com.demo.server.bean.BlackHouse;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Repository
public interface BlackHouseDao {
    BlackHouse selectBlackHouseById(long fromId, long toId);
    int insertBlackHouse(BlackHouse blackHouse);
    int updateBlackHouseState(long fromId, long toId, boolean isBlack);
}
