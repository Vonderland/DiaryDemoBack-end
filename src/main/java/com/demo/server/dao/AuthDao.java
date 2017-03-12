package com.demo.server.dao;

import com.demo.server.bean.Authorization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/11.
 */
@Repository
public interface AuthDao {
    Authorization selectAuthByUid(@Param("uid")long uid);
    int insertAuth(Authorization authorization);
    int updateAuth(@Param("uid")long uid, @Param("token")String token);
}
