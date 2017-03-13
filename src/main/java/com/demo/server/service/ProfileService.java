package com.demo.server.service;

import com.demo.server.bean.Authorization;
import com.demo.server.bean.Profile;
import com.demo.server.bean.ResultMsg;
import com.demo.server.bean.User;
import com.demo.server.dao.AuthDao;
import com.demo.server.dao.UserDao;
import com.demo.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Vonderland on 2017/3/11.
 */
@Service("profileService")
public class ProfileService {
    @Resource
    private UserDao userDao;
    @Resource
    private AuthDao authDao;

    public ResultMsg getUser(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }

        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
        } else {
            resultMsg.setCode(100);
            Profile profile = new Profile(user);
            resultMsg.setData(profile);
        }
        return resultMsg;
    }

    public ResultMsg updateAvatar(String token, String avatar) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }

        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
        } else {
            int rowCount = userDao.updateAvatar(uid, avatar);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                resultMsg.setCode(100);
                User newUser = userDao.selectUserByUid(uid);
                Profile profile = new Profile(newUser);
                resultMsg.setData(profile);
            }
        }
        return resultMsg;
    }

    public ResultMsg updateNickName(String token, String nickName) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }

        long uid = TokenUtil.getUidFromToken(token);
        Authorization authorization = authDao.selectAuthByUid(uid);
        if (authorization == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } else if (!token.equals(authorization.getToken())) {
            resultMsg.setCode(105);
            return resultMsg;
        }
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
        } else {
            int rowCount = userDao.updateNickName(uid, nickName);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                resultMsg.setCode(100);
                User newUser = userDao.selectUserByUid(uid);
                Profile profile = new Profile(newUser);
                resultMsg.setData(profile);
            }
        }
        return resultMsg;
    }

    private boolean checkTokenInvalidation(String token, ResultMsg resultMsg) {
        if (token == null) {
            resultMsg.setCode(108);
            return false;
        }
        long uid = TokenUtil.getUidFromToken(token);
        Authorization authorization = authDao.selectAuthByUid(uid);
        if (authorization == null) {
            resultMsg.setCode(107);
            return false;
        } else if (!token.equals(authorization.getToken())) {
            resultMsg.setCode(105);
            return false;
        }
        return true;
    }
}
