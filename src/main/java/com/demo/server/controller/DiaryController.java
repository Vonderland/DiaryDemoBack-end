package com.demo.server.controller;

import com.demo.server.bean.ResultMsg;
import com.demo.server.service.DiaryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Vonderland on 2017/1/29.
 */
@Controller("diaryController")
public class DiaryController {
    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @RequestMapping(value = "/allDiaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getAllDiaries(){
        ResultMsg resultMsg = diaryService.getAllDiaries();
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/diaries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getDiaries(@RequestParam(value = "size", required = false, defaultValue = "3") String size,
                             @RequestParam(value = "timeCursor", required = false, defaultValue = "-1") String timeCursor){
        long cursor = Long.parseLong(timeCursor);
        if (cursor < 0) {
            cursor = System.currentTimeMillis();
        }
        ResultMsg resultMsg = diaryService.getDiaries(Integer.parseInt(size), cursor);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/")
}
