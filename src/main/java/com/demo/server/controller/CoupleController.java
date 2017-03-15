package com.demo.server.controller;

import com.demo.server.bean.ResultMsg;
import com.demo.server.service.CoupleService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Vonderland on 2017/3/15.
 */
@Controller("coupleController")
public class CoupleController {
    private CoupleService coupleService;

    @Autowired
    public CoupleController(CoupleService coupleService) {
        this.coupleService = coupleService;
    }

    @RequestMapping(value = "/sendRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String sendRequest(@RequestHeader(value = "Authorization", required = false) String token,
                                @RequestParam(value = "email") String email) {
        ResultMsg resultMsg = coupleService.sendRequest(token, email);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/acceptRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String acceptRequest(@RequestHeader(value = "Authorization", required = false) String token,
                              @RequestParam(value = "id") long id) {
        ResultMsg resultMsg = coupleService.acceptRequest(token, id);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/rejectRequest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String rejectRequest(@RequestHeader(value = "Authorization", required = false) String token,
                                @RequestParam(value = "id") long id) {
        ResultMsg resultMsg = coupleService.rejectRequest(token, id);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/checkRequest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String checkRequest(@RequestHeader(value = "Authorization", required = false) String token) {
        ResultMsg resultMsg = coupleService.checkRequest(token);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/hasLover", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String hasLover(@RequestHeader(value = "Authorization", required = false) String token) {
        ResultMsg resultMsg = coupleService.hasLover(token);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/isBlack", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String isBlack(@RequestHeader(value = "Authorization", required = false) String token) {
        ResultMsg resultMsg = coupleService.isBlack(token);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/setIsBlack", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String setInBlack(@RequestHeader(value = "Authorization", required = false) String token,
                                @RequestParam(value = "isBlack") boolean isBlack) {
        ResultMsg resultMsg = coupleService.setIsBlack(token, isBlack);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }

    @RequestMapping(value = "/breakUp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String breakUp(@RequestHeader(value = "Authorization", required = false) String token) {
        ResultMsg resultMsg = coupleService.breakUp(token);

        Gson gson = new Gson();
        return gson.toJson(resultMsg);
    }
}
