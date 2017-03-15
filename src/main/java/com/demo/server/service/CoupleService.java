package com.demo.server.service;

import com.demo.server.bean.Authorization;
import com.demo.server.bean.Request;
import com.demo.server.bean.ResultMsg;
import com.demo.server.bean.User;
import com.demo.server.dao.*;
import com.demo.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Service("coupleService")
public class CoupleService {
    @Resource
    private BlackHouseDao blackHouseDao;
    @Resource
    private CoupleDao coupleDao;
    @Resource
    private RequestDao requestDao;
    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;

    public ResultMsg sendRequest(String token, String email) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User fromUser = userDao.selectUserByUid(uid);
        if (fromUser == null) {
            resultMsg.setCode(107);
            return resultMsg;
        }
        User toUser = userDao.selectUserByEmail(email);
        if (toUser == null) {
            resultMsg.setCode(112);
            return resultMsg;
        }

        return resultMsg;
    }

    public ResultMsg acceptRequest(String token, long id) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        return resultMsg;
    }

    public ResultMsg rejectRequest(String token, long id) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        return resultMsg;
    }

    public ResultMsg checkRequest(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        return resultMsg;
    }

    public ResultMsg hasLover(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        return resultMsg;
    }

    public ResultMsg isBlack(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        return resultMsg;
    }

    public ResultMsg setIsBlack(String token, boolean isBlack) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        return resultMsg;
    }

    public ResultMsg breakUp(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
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
