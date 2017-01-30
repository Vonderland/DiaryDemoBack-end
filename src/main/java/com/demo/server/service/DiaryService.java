package com.demo.server.service;

import com.demo.server.bean.Diary;
import com.demo.server.bean.ResultMsg;
import com.demo.server.dao.DiaryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vonderland on 2017/1/29.
 */
@Service("diaryService")
public class DiaryService {
    @Resource
    private DiaryDao diaryDao;

    public ResultMsg addDiary(Diary diary) {
        ResultMsg resultMsg = new ResultMsg();
        if (diary == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("diary is null");
        } else {
            try {
                int rowCount = diaryDao.insertDiary(diary);
                if (rowCount == 0) {
                    resultMsg.setCode(102);
                } else {
                    resultMsg.setCode(100);
                }
            } catch (Exception e) {
                System.out.println("addDiary " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg updateDiary(Diary diary, long id) {
        ResultMsg resultMsg = new ResultMsg();
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
                }
            } catch (Exception e) {
                System.out.println("updateDiary " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg deleteDiary(long id) {
        ResultMsg resultMsg = new ResultMsg();
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

    public ResultMsg getDiaries(int size, long timeCursor) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            List<Diary> diaryList = diaryDao.selectDiaries(size, timeCursor);
            resultMsg.setCode(100);
            resultMsg.setSize(diaryList.size());
            resultMsg.setData(diaryList);
        } catch (Exception e) {
            System.out.println("getDiaries " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg getAllDiaries() {
        ResultMsg resultMsg = new ResultMsg();
        try {
            List<Diary> diaryList = diaryDao.selectAllDiaries();
            resultMsg.setCode(100);
            resultMsg.setSize(diaryList.size());
            resultMsg.setData(diaryList);
        } catch (Exception e) {
            System.out.println("getAllDiaries " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }
}
