package com.qigu.readword.mycode.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qigu.readword.domain.User;
import com.qigu.readword.domain.VipOrder;
import com.qigu.readword.mycode.wechat.service.MyWxPayService;
import com.qigu.readword.mycode.wechat.utils.MD5Utils;
import com.qigu.readword.mycode.wechat.utils.MyStringUtils;
import com.qigu.readword.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/wechat/pay")
public class WxPayController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WxPayService wxPayService;

    @Resource
    private UserService userService;

    @Resource
    private MyWxPayService myWxPayService;


    private ExecutorService executorService = Executors.newCachedThreadPool();//不固定线程数量的线程池;

    @Resource
    private WxMaService wxService;


    @RequestMapping("/unifiedOrder/{productId}")
    public String unifiedOrder(@RequestParam String code, @PathVariable Long productId, HttpServletRequest httpServletRequest) {
        JSONObject json = new JSONObject();
        try {
            json.put("success", false);
            WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            Optional<User> userOptional = userService.getUserWithAuthoritiesByLogin(openid.toLowerCase());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                VipOrder vipOrder = myWxPayService.createOrder(productId, openid, user);
                WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = myWxPayService.toWxPayUnifiedOrderRequest(vipOrder, getRemoteAddress(httpServletRequest));
                WxPayUnifiedOrderResult wxPayUnifiedOrderResult = this.wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
                logger.info("wxPayUnifiedOrderResult ->{}", wxPayUnifiedOrderResult);
                String nonceStr = MyStringUtils.get32RandomString();
                long time = System.currentTimeMillis() / 1000;
                String stringBuilder = "appId=" + this.wxPayService.getConfig().getAppId() + "&" +
                    "nonceStr=" + nonceStr + "&" +
                    "package=" + "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId() + "&" +
                    "signType=" + "MD5" + "&" +
                    "timeStamp=" + String.valueOf(time) + "&" +
                    "key=" + this.wxPayService.getConfig().getMchKey();
                String sign = MD5Utils.md5(stringBuilder).toUpperCase();


                json.put("timeStamp", String.valueOf(time));
                json.put("nonceStr", nonceStr);
                json.put("package", "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId());
                json.put("signType", "MD5");
                json.put("paySign", sign);
                json.put("success", true);
                logger.info("-------再签名:" + json.toString());
                return json.toString();

            }

        } catch (Exception e) {
            try {
                json.put("success", false);
                json.put("errMsg", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return json.toString();
        //   return
    }


    @PostMapping("/paynotice")
    public void payNotice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("=================weixin is now notice,time is" + LocalDate.now() + " ============");
        BufferedReader reader = request.getReader();

        String line;
        StringBuilder stringbuffer = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringbuffer.append(line);
        }
        reader.close();
        logger.info("the accepte message is ==>" + stringbuffer.toString());
        executorService.execute(() -> {
            myWxPayService.updateVipOrder(stringbuffer.toString());
        });
        logger.info("====notify end====");
        response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");

    }

    private String getRemoteAddress(HttpServletRequest request) {
        String ip = "";
        ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if (!ip.matches("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"))
            ip = "127.0.0.1";
        return ip;
    }
}

