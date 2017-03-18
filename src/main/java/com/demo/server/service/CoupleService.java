package com.demo.server.service;

import com.demo.server.bean.*;
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
    @Resource
    private DiaryDao diaryDao;
    @Resource
    private MomentDao momentDao;

    public ResultMsg sendRequest(String token, String email) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }// token 不合法
        long uid = TokenUtil.getUidFromToken(token);
        User fromUser = userDao.selectUserByUid(uid);
        if (fromUser == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        if (fromUser.getEmail().equals(email)) {
            resultMsg.setCode(118);
            return resultMsg;
        } // 不能自己给自己发
        Couple fromCouple = coupleDao.selectCoupleByLover(uid);
        if (fromCouple != null) {
            resultMsg.setCode(115);
            return resultMsg;
        } // 用户自己已经有另一半
        User toUser = userDao.selectUserByEmail(email);
        if (toUser == null) {
            resultMsg.setCode(112);
            return resultMsg;
        } // 对方邮箱未注册
        long loverId = toUser.getUid();
        Couple toCouple = coupleDao.selectCoupleByLover(loverId);
        if (toCouple != null) {
            resultMsg.setCode(113);
            return resultMsg;
        } // 对方已经有另一半
        Request existedRequest = requestDao.selectRequestByIds(uid, loverId);
        if (existedRequest != null) {
            resultMsg.setCode(114);
            return resultMsg;
        } // 已经发送过请求，等待对方处理
        Request request = new Request();
        request.setFromId(uid);
        request.setFromEmail(fromUser.getEmail());
        request.setFromNickName(fromUser.getNickName());
        request.setSendTime(System.currentTimeMillis());
        request.setToId(loverId);
        int rowCount = requestDao.insertRequest(request);
        if (rowCount == 1) {
            resultMsg.setCode(100);
        } else {
            resultMsg.setCode(102);
        }
        return resultMsg;
    }

    public ResultMsg acceptRequest(String token, long id) {
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
        Couple couple = coupleDao.selectCoupleByLover(uid);
        if (couple != null) {
            resultMsg.setCode(115);
            return resultMsg;
        } // 用户自己已经有另一半
        Request request = requestDao.selectRequestById(id);
        long fromId = request.getFromId();
        Couple fromCouple = coupleDao.selectCoupleByLover(fromId);
        if (fromCouple != null) {
            resultMsg.setCode(113);
            return resultMsg;
        } // 对方已经有另一半
        int rowCount = requestDao.acceptRequest(id);
        if (rowCount == 1) {
            Couple newCouple = new Couple();
            newCouple.setLoverAId(uid);
            newCouple.setLoverBId(fromId);
            newCouple.setTogetherTime(System.currentTimeMillis());
            int count = coupleDao.insertCouple(newCouple);
            if (count == 1) {
                resultMsg.setCode(100);
            } else {
                resultMsg.setCode(102);
            }
        } else {
            resultMsg.setCode(102);
        }
        return resultMsg;
    }

    public ResultMsg rejectRequest(String token, long id) {
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
        int rowCount = requestDao.rejectRequest(id);
        if (rowCount == 1) {
            resultMsg.setCode(100);
        } else {
            resultMsg.setCode(102);
        }
        return resultMsg;
    }

    public ResultMsg checkRequest(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        Request request = requestDao.selectRequestByToId(uid);
        resultMsg.setCode(100);
        if (request != null) {
            resultMsg.setSize(1);
            resultMsg.setData(request);
        }
        return resultMsg;
    }

    public ResultMsg hasLover(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在

        Couple couple = coupleDao.selectCoupleByLover(uid);
        resultMsg.setCode(100);
        if (couple == null) {
            resultMsg.setData(false);
        } else {
            resultMsg.setData(true);
        }
        return resultMsg;
    }

    public ResultMsg isBlack(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在

        Couple couple = coupleDao.selectCoupleByLover(uid);
        if (couple == null) {
            resultMsg.setCode(116);
            return resultMsg;
        }
        long loverId = couple.getLoverAId() == uid ? couple.getLoverBId() : couple.getLoverAId();
        BlackHouse blackHouse = blackHouseDao.selectBlackHouseById(loverId, uid);
        resultMsg.setCode(100);
        if (blackHouse == null) {
            resultMsg.setData(false);
        } else {
            resultMsg.setData(true);
        }
        return resultMsg;
    }

    public ResultMsg setIsBlack(String token, boolean isBlack) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        Couple couple = coupleDao.selectCoupleByLover(uid);
        if (couple == null) {
            resultMsg.setCode(116);
            return resultMsg;
        }
        long loverId = couple.getLoverAId() == uid ? couple.getLoverBId() : couple.getLoverAId();
        if (isBlack) {
            BlackHouse existedBlackHouse = blackHouseDao.selectBlackHouseById(loverId, uid);
            if (existedBlackHouse == null) {
                BlackHouse blackHouse = new BlackHouse();
                blackHouse.setFromId(uid);
                blackHouse.setToId(loverId);
                int rowCount = blackHouseDao.insertBlackHouse(blackHouse);
                if (rowCount == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                }
            } else {
                resultMsg.setCode(117);// 对方已经先发制人关小黑屋
            }
        } else {
            blackHouseDao.updateBlackHouseState(uid, loverId, true);
            resultMsg.setCode(100);
        }
        return resultMsg;
    }

    public ResultMsg breakUp(String token) {
        ResultMsg resultMsg = new ResultMsg();
        if (!checkTokenInvalidation(token, resultMsg)) {
            return resultMsg;
        }
        long uid = TokenUtil.getUidFromToken(token);
        User user = userDao.selectUserByUid(uid);
        if (user == null) {
            resultMsg.setCode(107);
            return resultMsg;
        } // 用户不存在
        Couple couple = coupleDao.selectCoupleByLover(uid);
        if (couple == null) {
            resultMsg.setCode(116);
            return resultMsg;
        }// 已经分手了
        long loverId = couple.getLoverAId() == uid ? couple.getLoverBId() : couple.getLoverAId();
        int rowCount = coupleDao.breakUp(uid, loverId);
        if (rowCount == 0) {
            resultMsg.setCode(102);
        } else {
            resultMsg.setCode(100);
            diaryDao.deleteDiaryByUid(uid);
            diaryDao.deleteDiaryByUid(loverId);
            momentDao.deleteMomentByUid(uid);
            momentDao.deleteMomentByUid(loverId);
            blackHouseDao.updateBlackHouseState(uid, loverId, true);
            blackHouseDao.updateBlackHouseState(loverId, uid, true);
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
