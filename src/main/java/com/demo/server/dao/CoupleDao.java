package com.demo.server.dao;

import com.demo.server.bean.Couple;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Repository
public interface CoupleDao {
    int insertCouple(Couple couple);
    Couple selectCoupleByLover(long loverId);
    Couple selectCoupleByLovers(long loverAId, long loverBId);
    int updateBreakUpState(long coupleId, boolean isBroken);
}
