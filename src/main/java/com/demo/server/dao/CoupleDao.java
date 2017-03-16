package com.demo.server.dao;

import com.demo.server.bean.Couple;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Repository
public interface CoupleDao {
    int insertCouple(Couple couple);
    Couple selectCoupleByLover(@Param("loverId")long loverId);
    Couple selectCoupleByLovers(@Param("loverAId")long loverAId, @Param("loverBId")long loverBId);
    int updateBreakUpState(@Param("coupleId")long coupleId, @Param("isBroken")boolean isBroken);
    int breakUp(@Param("loverAId")long loverAId, @Param("loverBId")long loverBId);
}
