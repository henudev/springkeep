package com.h3c.bigdata.zhgx.function.report.controller;

import com.h3c.bigdata.zhgx.common.constant.CaCheMapConst;
import com.h3c.bigdata.zhgx.common.web.controller.BaseController;
import com.h3c.bigdata.zhgx.function.report.model.SseResultModel;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lvyacong
 * @date 2019/12/12 14:49
 */
@RestController
@RequestMapping("/monitor")
@Slf4j
public class MonitorController extends BaseController {

    private Map<String, SseEmitter> cache = new ConcurrentHashMap<>();

    @GetMapping(value = "/sse",
            headers = "accept=text/event-stream")
    public SseEmitter monitor(@RequestParam("access-token") String accessToken) {
        String userId = "";
        UserDetails userDetails = CaCheMapConst.USERCACHE.getIfPresent(accessToken);
        if (null != userDetails) {
            userId = userDetails.getUsername();
        }
        //0L永不超时
        SseEmitter emitter = new SseEmitter(0L);
        if (null == cache.get(userId)) {
            cache.put(userId, emitter);
        }
        return emitter;
    }

    public void send(String userId, String type, String messageCode) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SseResultModel sseModel = new SseResultModel();
        sseModel.setType(type);
        sseModel.setMessageCode(messageCode);
        sseModel.setTimestamp(format.format(new Date()));
        JSONObject sseJson = JSONObject.fromObject(sseModel);
        try {
            SseEmitter emitter = cache.get(userId);
//            if (null == emitter) {
//                emitter = new SseEmitter(86400000L);
//                cache.put(userId, emitter);
//            }

            if (emitter != null) {
                //  emitter.send(SseEmitter.event().data(data).build(), MediaType.APPLICATION_JSON_UTF8);
                emitter.send(sseJson.toString(), MediaType.APPLICATION_JSON_UTF8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将sse对象设置为完成
    @GetMapping("/end")
    public void completeSseEmitter(String userId) {
        SseEmitter emitter = cache.get(userId);
        if (emitter != null) {
            cache.remove(userId);
            emitter.complete();
        }

    }
}
