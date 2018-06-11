package com.qigu.readword.mycode.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qigu.readword.domain.SocialUserConnection;
import com.qigu.readword.domain.User;
import com.qigu.readword.domain.VipOrder;
import com.qigu.readword.domain.enumeration.OrderStatus;
import com.qigu.readword.mycode.util.ReadWordConstants;
import com.qigu.readword.mycode.wechat.utils.MD5Utils;
import com.qigu.readword.repository.SocialUserConnectionRepository;
import com.qigu.readword.repository.UserRepository;
import com.qigu.readword.repository.VipOrderRepository;
import com.qigu.readword.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/wechat/pay")
public class WxPayController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WxPayService wxPayService;

    @Resource
    private VipOrderRepository vipOrderRepository;


    @Resource
    private UserService userService;

    @Resource
    private UserRepository userRepository;

    private ExecutorService executorService = Executors.newCachedThreadPool();//不固定线程数量的线程池;

    @Resource
    private WxMaService wxService;


    @RequestMapping("/unifiedOrder")
    public String unifiedOrder(@RequestParam String code, HttpServletRequest httpServletRequest) {
        JSONObject json = new JSONObject();
        try {
            json.put("success", false);
            WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            Optional<User> userOptional = userService.getUserWithAuthoritiesByLogin(openid.toLowerCase());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                VipOrder vipOrder = new VipOrder();
                vipOrder.setMonths(1);
                vipOrder.setTotalPrice(0.05);
                vipOrder.setCreateTime(Instant.now());
                vipOrder.setUser(user);
                vipOrder.setTradeType("JSAPI");
                vipOrder.setOpenId(openid);
                vipOrder.setStatus(OrderStatus.NOPAY);

                vipOrder = vipOrderRepository.save(vipOrder);
                String outTradeNo = StringUtils.leftPad(vipOrder.getId().toString(), 10, '0');
                vipOrder.setOutTradeNo(outTradeNo);
                vipOrderRepository.save(vipOrder);
                logger.info("{}", vipOrder);

                WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
                request.setOutTradeNo(vipOrder.getOutTradeNo());
                Double v = vipOrder.getTotalPrice() * 100;//单位是分
                request.setTotalFee(v.intValue());
                request.setBody("新新看图识字1年会员");
                request.setTradeType("JSAPI");
                String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
                request.setNonceStr(nonceStr);
                request.setOpenid(openid);
                request.setNotifyUrl("http://ectest.nipponpaint.com.cn/wx-mp/wechat/pay/paynotice");
                request.setSpbillCreateIp(getRemoteAddress(httpServletRequest));
                logger.info("nonceStr ->{}", nonceStr);
                WxPayUnifiedOrderResult wxPayUnifiedOrderResult = this.wxPayService.unifiedOrder(request);

                logger.info("wxPayUnifiedOrderResult ->{}", wxPayUnifiedOrderResult);
                String nonceStr2 = UUID.randomUUID().toString().replaceAll("-", "");
                long time = System.currentTimeMillis() / 1000;

                String stringBuilder = "appId=" + this.wxPayService.getConfig().getAppId() + "&" +
                    "nonceStr=" + nonceStr2 + "&" +
                    "package=" + "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId() + "&" +
                    "signType=" + "MD5" + "&" +
                    "timeStamp=" + String.valueOf(time) + "&" +
                    "key=" + this.wxPayService.getConfig().getMchKey();
                String sign = MD5Utils.md5(stringBuilder).toUpperCase();


                json.put("timeStamp", String.valueOf(time));
                json.put("nonceStr", nonceStr2);
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
    public void payNotice(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException, WxPayException {
        logger.info("=================weixin is now notice,time is" + LocalDate.now() + " ============");
        BufferedReader reader = request.getReader();

        String line;
        StringBuilder stringbuffer = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringbuffer.append(line);
        }
        reader.close();
        logger.info("the accepte message is ==>" + stringbuffer.toString());

        WxPayOrderNotifyResult wxPayOrderNotifyResult = this.wxPayService.parseOrderNotifyResult(stringbuffer.toString());
        logger.info("wxPayOrderNotifyResult->{}", wxPayOrderNotifyResult);

        executorService.execute(() -> {
            String resultCode = wxPayOrderNotifyResult.getResultCode();
            if ("SUCCESS".equals(resultCode)) {
                String outTradeNo = wxPayOrderNotifyResult.getOutTradeNo();
                VipOrder vipOrder = vipOrderRepository.findByOutTradeNoEquals(outTradeNo);
                vipOrder.setStatus(OrderStatus.PAYED);
                vipOrder.setPaymentTime(Instant.now());
                vipOrder.setPaymentResult(stringbuffer.toString());
                vipOrder.setTransactionId(wxPayOrderNotifyResult.getTransactionId());
                vipOrderRepository.save(vipOrder);
                User user = vipOrder.getUser();
                Instant vipEndDate = user.getVipEndDate();
                Instant now = Instant.now();
                Date fromDate = new Date();
                if (!Objects.isNull(vipEndDate) && vipEndDate.isAfter(now)) {
                    fromDate = new Date(vipEndDate.toEpochMilli());
                }
                Date newEndDate = DateUtils.addMonths(fromDate, vipOrder.getMonths());
                user.setVipEndDate(newEndDate.toInstant());
                userRepository.save(user);
                logger.info("更新vip时间成功->{}", user);
            }
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

