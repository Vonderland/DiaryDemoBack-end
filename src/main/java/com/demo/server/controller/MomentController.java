package com.demo.server.controller;

import com.demo.server.bean.Moment;
import com.demo.server.bean.ResultMsg;
import com.demo.server.service.MomentService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Vonderland on 2017/2/1.
 */
@Controller("momentController")
public class MomentController {
    private final MomentService momentService;

    @Autowired
    public MomentController(MomentService momentService) {
        this.momentService = momentService;
    }

    @RequestMapping(value = "/allMoment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getAllMoment(@RequestHeader(value = "Authorization", required = false) String token) {
        ResultMsg resultMsg = momentService.getAllMoment(token);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/moment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getMoment(@RequestHeader(value = "Authorization", required = false) String token,
                            @RequestParam(value = "size", required = false, defaultValue = "20") String size,
                             @RequestParam(value = "timeCursor", required = false, defaultValue = "-1") String timeCursor) {
        long cursor = Long.parseLong(timeCursor);
        if (cursor < 0) {
            cursor = System.currentTimeMillis();
        }
        ResultMsg resultMsg = momentService.getMoment(token, Integer.parseInt(size), cursor);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/addMoment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String addMoment(@RequestHeader(value = "Authorization", required = false) String token,
                            @RequestParam(value = "title") String title,
                           @RequestParam(value = "location", required = false) String location,
                           @RequestParam(value = "eventTime") String eventTime) {
        ResultMsg resultMsg;
        Moment moment = generateMoment(title, location, eventTime, true);
        resultMsg = momentService.addMoment(token, moment);
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/updateMoment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateMoment(@RequestHeader(value = "Authorization", required = false) String token,
                               @RequestParam(value = "id") String id,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "location") String location,
                              @RequestParam(value = "eventTime") String eventTime) {
        ResultMsg resultMsg;
        Moment moment = generateMoment(title, location, eventTime, false);
        moment.setId(Long.parseLong(id));
        resultMsg = momentService.updateMoment(token, moment, Long.parseLong(id));
        Gson gson = new Gson();
        return gson.toJson(resultMsg);

    }

    @RequestMapping(value = "/deleteMoment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String deleteMoment(@RequestHeader(value = "Authorization", required = false) String token,
                               @RequestParam(value = "id") String id) {
        ResultMsg resultMsg = momentService.deleteMoment(token, Long.parseLong(id));
        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    private Moment generateMoment(String title, String location, String eventTime, boolean add) {
        Moment moment = new Moment();
        moment.setTitle(title);
        moment.setLocation(location);
        moment.setEventTime(Long.parseLong(eventTime));

        //TODO:注意，这里可能会有时区不一致的问题
        Long now = System.currentTimeMillis();
        if (add) {
            moment.setCreateTime(now);
        }
        moment.setUpdateTime(now);
        return moment;
    }
}
