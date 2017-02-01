package com.demo.server.service;

import com.demo.server.bean.Moment;
import com.demo.server.bean.ResultMsg;
import com.demo.server.dao.MomentDao;
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

    public ResultMsg addMoment(Moment moment) {
        ResultMsg resultMsg = new ResultMsg();
        if (moment == null) {
            resultMsg.setCode(101);
            resultMsg.setMessage("moment is null");
        } else {
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

    public ResultMsg updateMoment(Moment moment, long id) {
        ResultMsg resultMsg = new ResultMsg();
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
                    momentList.add(moment);
                    resultMsg.setData(momentList);
                }
            } catch (Exception e) {
                System.out.println("updateMoment " + Arrays.toString(e.getStackTrace()));
                resultMsg.setCode(103);
            }
        }
        return resultMsg;
    }

    public ResultMsg deleteMoment(long id) {
        ResultMsg resultMsg = new ResultMsg();
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

    public ResultMsg getMoment(int size, long timeCursor) {
        ResultMsg resultMsg = new ResultMsg();
        try {
            List<Moment> momentList = momentDao.selectMoment(size, timeCursor);
            resultMsg.setCode(100);
            resultMsg.setSize(momentList.size());
            resultMsg.setData(momentList);
        } catch (Exception e) {
            System.out.println("getMoment " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }

    public ResultMsg getAllMoment() {
        ResultMsg resultMsg = new ResultMsg();
        try {
            List<Moment> momentList = momentDao.selectAllMoment();
            resultMsg.setCode(100);
            resultMsg.setSize(momentList.size());
            resultMsg.setData(momentList);
        } catch (Exception e) {
            System.out.println("getAllMoment " + Arrays.toString(e.getStackTrace()));
            resultMsg.setCode(103);
        }
        return resultMsg;
    }
}
