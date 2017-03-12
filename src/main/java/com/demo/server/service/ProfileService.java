package com.demo.server.service;

import com.demo.server.bean.ResultMsg;
import com.demo.server.dao.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Vonderland on 2017/3/11.
 */
@Service("profileService")
public class ProfileService {
    @Resource
    private UserDao userDao;

    public ResultMsg getUser(String token) {
        ResultMsg resultMsg = new ResultMsg();
        return resultMsg;
    }

    public ResultMsg updateAvatar(String token, String avatar) {
        ResultMsg resultMsg = new ResultMsg();
        return resultMsg;
    }

    public ResultMsg updateNickName(String token, String nickName) {
        ResultMsg resultMsg = new ResultMsg();
        return resultMsg;
    }
}
