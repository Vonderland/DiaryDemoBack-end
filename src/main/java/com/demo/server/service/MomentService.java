package com.demo.server.service;

import com.demo.server.bean.*;
import com.demo.server.dao.AuthDao;
import com.demo.server.dao.CoupleDao;
import com.demo.server.dao.MomentDao;
import com.demo.server.dao.UserDao;
import com.demo.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vonderland on 2017/2/1.
 */
@Service("momentService")
public class MomentService {
    @Resource
    private MomentDao momentDao;
    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;
    @Resource
    private CoupleDao coupleDao;

    public ResultMsg addMoment(String token, Moment moment) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }// token 不合法
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        if (moment == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("moment is null");
        } else {
            moment.setUid(uid);
            try {
                int rowCount = momentDao.insertMoment(moment);
                System.out.println("rowCount = " + rowCount);
                if (rowCount == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setSize(rowCount);
                    List<Moment> momentList = new ArrayList<Moment>();
                    momentList.add(moment);
                    resultMsg.setData(momentList);
                }
            } catch (Exception e) {
                System.out.println("addMoment " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg updateMoment(String token, Moment moment, long id) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }// token 不合法
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        if (moment == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("moment is null");
        } else {
            try {
                int rowCount = momentDao.updateMoment(moment, id);
                if (rowCount == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setSize(1);
                    List<Moment> momentList = new ArrayList<Moment>();
                    momentList.add(momentDao.selectMomentByID(id));
                    resultMsg.setData(momentList);
                }
            } catch (Exception e) {
                System.out.println("updateMoment " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg deleteMoment(String token, long id) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }// token 不合法
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        try {
            int rowCount = momentDao.deleteMoment(id);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                resultMsg.setCode(100);
            }
        } catch (Exception e) {
            System.out.println("deleteMoment " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg getMoment(String token, int size, long timeCursor) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }// token 不合法
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        try {
            Couple couple = coupleDao.selectCoupleByLover(uid);
            long loverId = -1;
            if (couple != null) {
                loverId = couple.getLoverAId() == uid ? couple.getLoverBId() : couple.getLoverAId();
            }
            List<Moment> momentList = momentDao.selectMoment(size, timeCursor, uid, loverId);
            resultMsg.setCode(100);
            resultMsg.setSize(momentList.size());
            resultMsg.setData(momentList);
        } catch (Exception e) {
            System.out.println("getMoment " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg getAllMoment(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }// token 不合法
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        try {
            Couple couple = coupleDao.selectCoupleByLover(uid);
            long loverId = -1;
            if (couple != null) {
                loverId = couple.getLoverAId() == uid ? couple.getLoverBId() : couple.getLoverAId();
            }
            List<Moment> momentList = momentDao.selectAllMoment(uid, loverId);
            resultMsg.setCode(100);
            resultMsg.setSize(momentList.size());
            resultMsg.setData(momentList);
        } catch (Exception e) {
            System.out.println("getAllMoment " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
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
