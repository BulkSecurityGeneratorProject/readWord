package com.qigu.readword.mycode.wechat.controller;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qigu.readword.domain.User;
import com.qigu.readword.service.UserService;
import com.qigu.readword.service.VipOrderService;
import com.qigu.readword.service.dto.VipOrderDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Optional;


@RestController
@RequestMapping("/wechat/pay")
public class WxPayController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WxPayService wxPayService;

    @Resource
    private VipOrderService vipOrderService;

    @Resource
    private UserService userService;

    @RequestMapping("/unifiedOrder")
    public void unifiedOrder() throws WxPayException {
        VipOrderDTO vipOrder = new VipOrderDTO();
        vipOrder.setMonths(12);
        vipOrder.setTotalPrice(0.5);
        vipOrder.setCreateTime(Instant.now());

        Optional<User> user = userService.getUserWithAuthoritiesByLogin("oiq1o5d9gncqnwjzfch-zzcuy4rk");
        user.ifPresent(user1 -> {
            vipOrder.setUserId(user1.getId());
        });
        VipOrderDTO orderSaved = vipOrderService.save(vipOrder);
        String outTradeNo = StringUtils.leftPad(orderSaved.getId().toString(), 10, '0');
        orderSaved.setOutTradeNo(outTradeNo);
        vipOrderService.save(orderSaved);
        logger.info("{}", orderSaved);

        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setOutTradeNo(orderSaved.getOutTradeNo());
        Double v = vipOrder.getTotalPrice() * 100;//单位是分
        request.setTotalFee(v.intValue());
        request.setBody("新新看图识字1年会员");
        request.setNotifyUrl("");
        request.setNonceStr("");

        //   return this.wxService.unifiedOrder(request);
    }


}

