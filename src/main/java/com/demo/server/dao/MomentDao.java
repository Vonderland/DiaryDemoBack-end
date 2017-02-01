package com.demo.server.dao;

import com.demo.server.bean.Moment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Vonderland on 2017/2/1.
 */
@Repository
public interface MomentDao {
    int insertMoment(Moment moment);
    int updateMoment(@Param("moment") Moment moment, @Param("id") long id);
    int deleteMoment(@Param("id") long id);
    List<Moment> selectMoment(@Param("size") int size, @Param("timeCursor") long timeCursor);
    List<Moment> selectAllMoment();
}
