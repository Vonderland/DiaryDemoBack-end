package com.demo.server.dao;

import com.demo.server.bean.BlackHouse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Repository
public interface BlackHouseDao {
    BlackHouse selectBlackHouseById(@Param("fromId")long fromId, @Param("toId")long toId);
    int insertBlackHouse(BlackHouse blackHouse);
    int updateBlackHouseState(@Param("fromId")long fromId, @Param("toId")long toId, @Param("isBlack")boolean isBlack);
}
