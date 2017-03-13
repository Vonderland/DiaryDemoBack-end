package com.demo.server.service;

import com.demo.server.bean.Authorization;
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
@Service("accountService")
public class AccountService {
    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;

    public ResultMsg register(User user) {
        ResultMsg resultMsg = new ResultMsg();
        if (user == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("user is null");
            return resultMsg;
        }
        User existUser = userDao.selectUserByEmail(user.getEmail());
        if (existUser != null) {
            resultMsg.setCode(106);
            resultMsg.setMessage("the email address has been used");
            return resultMsg;
        }
        try {
            int rowCount = userDao.insertUser(user);
            System.out.println("rowCount = " + rowCount);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                long uid = user.getUid();
                System.out.println("uid = " + uid);
                String token = TokenUtil.generateToken(uid);
                Authorization existAuth = authDao.selectAuthByUid(uid);
                Authorization auth = new Authorization();
                auth.setUid(uid);
                auth.setToken(token);
                if (existAuth == null) {
                    int count = authDao.insertAuth(auth);
                    if (count == 0) {
                        resultMsg.setCode(102);
                    } else {
                        resultMsg.setCode(100);
                        resultMsg.setData(auth);
                    }
                } else {
                    int count = authDao.updateAuth(uid, token);
                    if (count == 0) {
                        resultMsg.setCode(102);
                    } else {
                        resultMsg.setCode(100);
                        resultMsg.setData(auth);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("register " + e);
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg login(String email, String password) {
        ResultMsg resultMsg = new ResultMsg();
        User user = userDao.selectUserByEmail(email);
        if (user == null) {
            resultMsg.setCode(107);
        } else {
            if (password.equals(user.getPassword())) {
                Long uid = user.getUid();
                Authorization auth = authDao.selectAuthByUid(uid);
                if (auth == null) {
                    auth = new Authorization();
                    auth.setUid(uid);
                    String token = TokenUtil.generateToken(uid);
                    auth.setToken(token);
                    int count = authDao.insertAuth(auth);
                    if (count == 0) {
                        resultMsg.setCode(102);
                    } else {
                        resultMsg.setCode(100);
                        resultMsg.setData(auth);
                    }
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setData(auth);
                }
            } else {
                resultMsg.setCode(110);
            }
        }
        return resultMsg;
    }

    public ResultMsg resetPassword(String token, String password, String newPassword) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        }
        if (password.equals(user.getPassword())) {
            int rowCount = userDao.updatePassword(uid, newPassword);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                String newToken = TokenUtil.generateToken(uid);
                Authorization auth = new Authorization();
                auth.setToken(newToken);
                auth.setUid(uid);
                int count = authDao.updateAuth(uid, newToken);
                if (count == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setData(auth);
                }
            }
        } else {
            resultMsg.setCode(110);
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
