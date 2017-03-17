package com.demo.server.service;

import com.demo.server.bean.*;
import com.demo.server.dao.AuthDao;
import com.demo.server.dao.CoupleDao;
import com.demo.server.dao.DiaryDao;
import com.demo.server.dao.UserDao;
import com.demo.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vonderland on 2017/1/29.
 */
@Service("diaryService")
public class DiaryService {
    @Resource
    private DiaryDao diaryDao;
    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;
    @Resource
    private CoupleDao coupleDao;

    public ResultMsg addDiary(String token, Diary diary) {
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
        if (diary == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("diary is null");
        } else {
            diary.setUid(uid);
            try {
                int rowCount = diaryDao.insertDiary(diary);
                System.out.println("rowCount = " + rowCount);
                if (rowCount == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setSize(rowCount);
                    List<Diary> diaryList = new ArrayList<Diary>();
                    diaryList.add(diary);
                    resultMsg.setData(diaryList);
                }
            } catch (Exception e) {
                System.out.println("addDiary " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg  updateDiary(String token, Diary diary, long id) {
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
        if (diary == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("diary is null");
        } else {
            try {
                int rowCount = diaryDao.updateDiary(diary, id);
                if (rowCount == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setSize(1);
                    List<Diary> diaryList = new ArrayList<Diary>();
                    diaryList.add(diaryDao.selectDiaryByID(id));
                    resultMsg.setData(diaryList);
                }
            } catch (Exception e) {
                System.out.println("updateDiary " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg updateDiaryNotChangePic(String token, Diary diary, long id) {
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
        if (diary == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("diary is null");
        } else {
            try {
                int rowCount = diaryDao.updateDiaryNotChangePic(diary, id);
                if (rowCount == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                    resultMsg.setSize(1);
                    List<Diary> diaryList = new ArrayList<Diary>();
                    diaryList.add(diaryDao.selectDiaryByID(id));
                    resultMsg.setData(diaryList);
                }
            } catch (Exception e) {
                System.out.println("updateDiary " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg deleteDiary(String token, long id) {
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
            int rowCount = diaryDao.deleteDiary(id);
            if (rowCount == 0) {
                resultMsg.setCode(102);
            } else {
                resultMsg.setCode(100);
            }
        } catch (Exception e) {
            System.out.println("deleteDiary " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg getDiaries(String token, int size, long timeCursor) {
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
            List<Diary> diaryList = diaryDao.selectDiaries(size, timeCursor, uid, loverId);
            resultMsg.setCode(100);
            resultMsg.setSize(diaryList.size());
            resultMsg.setData(diaryList);
        } catch (Exception e) {
            System.out.println("getDiaries " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg getAllDiaries(String token) {
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
            List<Diary> diaryList = diaryDao.selectAllDiaries(uid, loverId);
            resultMsg.setCode(100);
            resultMsg.setSize(diaryList.size());
            resultMsg.setData(diaryList);
        } catch (Exception e) {
            System.out.println("getAllDiaries " + Arrays.toString(e.getStackTrace()));
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
