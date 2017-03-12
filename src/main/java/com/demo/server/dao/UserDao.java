package com.demo.server.dao;

import com.demo.server.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Vonderland on 2017/3/11.
 */
@Repository
public interface UserDao {
    int insertUser(User user);
    User selectUserByUid(@Param("uid")long uid);
    User selectUserByEmail(@Param("email")String email);
    int updateAvatar(@Param("uid")long uid, @Param("avatar")String avatar);
    int updateNickName(@Param("uid")long uid, @Param("nickName")String nick);
    int updatePassword(@Param("uid")long uid, @Param("password")String password);
}
