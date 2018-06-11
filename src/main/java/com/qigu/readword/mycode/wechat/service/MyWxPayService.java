package com.qigu.readword.mycode.wechat.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.qigu.readword.domain.Product;
import com.qigu.readword.domain.User;
import com.qigu.readword.domain.VipOrder;
import com.qigu.readword.domain.enumeration.VipOrderStatus;
import com.qigu.readword.mycode.wechat.utils.MyStringUtils;
import com.qigu.readword.repository.ProductRepository;
import com.qigu.readword.repository.UserRepository;
import com.qigu.readword.repository.VipOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Service
public class MyWxPayService {
    private final VipOrderRepository vipOrderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WxPayService wxPayService;
    private final Logger log = LoggerFactory.getLogger(MyWxPayService.class);

    public MyWxPayService(VipOrderRepository vipOrderRepository, ProductRepository productRepository, UserRepository userRepository, WxPayService wxPayService) {
        this.vipOrderRepository = vipOrderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.wxPayService = wxPayService;
    }

    public VipOrder createOrder(Long productId, String openid, User user) {
        Product product = productRepository.findOne(productId);
        VipOrder vipOrder = new VipOrder();
        vipOrder.setMonths(product.getTotalMonths());
        vipOrder.setTotalPrice(product.getPrice());
        vipOrder.setCreateTime(Instant.now());
        vipOrder.setUser(user);
        vipOrder.setTradeType("JSAPI");
        vipOrder.setOpenId(openid);
        vipOrder.setStatus(VipOrderStatus.NOPAY);
        vipOrder.setProduct(product);
        vipOrder = vipOrderRepository.save(vipOrder);
        String outTradeNo = StringUtils.leftPad(vipOrder.getId().toString(), 10, '0');
        vipOrder.setOutTradeNo(outTradeNo);
        vipOrderRepository.save(vipOrder);
        log.info("{}", vipOrder);
        return vipOrder;

    }

    public WxPayUnifiedOrderRequest toWxPayUnifiedOrderRequest(VipOrder vipOrder, String ip) {
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setOutTradeNo(vipOrder.getOutTradeNo());
        Double v = vipOrder.getTotalPrice() * 100;//单位是分
        request.setTotalFee(v.intValue());
        request.setBody(vipOrder.getProduct().getName());
        request.setTradeType(vipOrder.getTradeType());
        String nonceStr = MyStringUtils.get32RandomString();
        request.setNonceStr(nonceStr);
        request.setOpenid(vipOrder.getOpenId());
        request.setNotifyUrl("http://ectest.nipponpaint.com.cn/wx-mp/wechat/pay/paynotice");
        request.setSpbillCreateIp(ip);
        log.info("nonceStr ->{}", nonceStr);

        return request;
    }

    public void updateVipOrder(String payNotice) {

        WxPayOrderNotifyResult wxPayOrderNotifyResult;
        try {
            wxPayOrderNotifyResult = this.wxPayService.parseOrderNotifyResult(payNotice);
            log.info("wxPayOrderNotifyResult->{}", wxPayOrderNotifyResult);
            String resultCode = wxPayOrderNotifyResult.getResultCode();
            String outTradeNo = wxPayOrderNotifyResult.getOutTradeNo();
            VipOrder vipOrder = vipOrderRepository.findByOutTradeNoEquals(outTradeNo);
            if ("SUCCESS".equals(resultCode)) {

                vipOrder.setStatus(VipOrderStatus.PAYED);
                vipOrder.setPaymentTime(Instant.now());
                vipOrder.setPaymentResult(payNotice);
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
                log.info("更新vip时间成功->{}", user);
            } else {
                vipOrder.setStatus(VipOrderStatus.CLOSED);
                vipOrder.setPaymentTime(Instant.now());
                vipOrderRepository.save(vipOrder);
            }
        } catch (WxPayException e) {
            e.printStackTrace();
        }

    }
}
