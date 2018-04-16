package com.qigu.readword.mycode.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.qigu.readword.config.Constants;
import com.qigu.readword.domain.SocialUserConnection;
import com.qigu.readword.domain.User;
import com.qigu.readword.mycode.util.ReadWordConstants;
import com.qigu.readword.repository.SocialUserConnectionRepository;
import com.qigu.readword.repository.UserRepository;
import com.qigu.readword.security.jwt.JWTConfigurer;
import com.qigu.readword.security.jwt.TokenProvider;
import com.qigu.readword.service.SocialService;
import com.qigu.readword.service.UserService;
import com.qigu.readword.service.dto.UserDTO;
import com.qigu.readword.web.rest.UserJWTController;
import com.qigu.readword.web.rest.errors.InternalServerErrorException;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.UserProfile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wechat/user")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TokenProvider tokenProvider;
    private final SocialService socialService;

    private final SocialUserConnectionRepository socialUserConnectionRepository;
    private final WxMaService wxService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final UserRepository userRepository;

    public WxMaUserController(TokenProvider tokenProvider, SocialService socialService, SocialUserConnectionRepository socialUserConnectionRepository, WxMaService wxService, UserDetailsService userDetailsService, UserService userService, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.socialService = socialService;
        this.socialUserConnectionRepository = socialUserConnectionRepository;
        this.wxService = wxService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * 登陆接口
     */
    @GetMapping("login")
    public ResponseEntity<UserJWTController.JWTToken> login(String code, HttpServletRequest request) {

        try {
            WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            String sessionKey = session.getSessionKey();
//            String expiresIn = session.getExpiresin().toString();
            this.logger.info(openid);
            this.logger.info(sessionKey);
//            this.logger.info(expiresIn);
            return loginJhipster(openid, sessionKey, "", request);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            String jwt = "";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new UserJWTController.JWTToken(jwt), httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("info")
    public UserDTO info(String sessionKey, String signature, String rawData, String encryptedData, String iv) {
        this.logger.info("sessionKey-->" + sessionKey);
        this.logger.info("signature-->" + signature);
        this.logger.info("rawData-->" + rawData);
        this.logger.info("encryptedData-->" + encryptedData);
        this.logger.info("iv-->" + iv);
        // 用户信息校验
        if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            this.logger.error("user check failed");
            throw new InternalServerErrorException("User could not be found");
        }
        // 解密用户信息
        WxMaUserInfo userInfo = this.wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        updateUserInfo(userInfo);
        logger.info("userInfo --> {}", userInfo);

        return userService.getUserWithAuthoritiesByLogin(userInfo.getOpenId().toLowerCase())
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));

    }

    @PostMapping("login")
    public ResponseEntity<UserJWTController.JWTToken> loginJhipster(String openid, String sessionKey, String expiresIn, HttpServletRequest request) {
        UserDetails user = null;
        try {
            user = userDetailsService.loadUserByUsername(openid);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ReadWordConstants.WX_SESSION_KEY, sessionKey);
        httpHeaders.add(ReadWordConstants.WX_EXPIRES_IN, expiresIn);
        if (user == null) {
            user = createUserByOpenId(openid);
        }

        String jwt;
        try {
            assert user != null;
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            jwt = tokenProvider.createToken(authenticationToken, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, ReadWordConstants.AUTHORIZATION_NO_USER);
            return new ResponseEntity<>(new UserJWTController.JWTToken(""), httpHeaders, HttpStatus.OK);
        }

        logger.info("##################jwt:" + jwt);
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
       /* try {
            String formId = request.getParameter(WxMaTemplateConstants.FORM_ID);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(formId)) {
                WxMaTemplateMessage loginSuccessTemplate = wxMaTemplateService.getLoginSuccessTemplate(user, formId);
                wxService.getMsgService().sendTemplateMsg(loginSuccessTemplate);
            }
        } catch (WxErrorException e) {
            logger.error(e.getMessage());
        }*/
        return new ResponseEntity<>(new UserJWTController.JWTToken(jwt), httpHeaders, HttpStatus.OK);

    }

    private void updateUserInfo(WxMaUserInfo userInfo) {
        userService.getUserWithAuthoritiesByLogin(userInfo.getOpenId().toLowerCase()).ifPresent(user -> {
            user.setFirstName(userInfo.getNickName());
            user.setLastName(userInfo.getNickName());
            user.setImageUrl(userInfo.getAvatarUrl());
            userRepository.save(user);
            List<SocialUserConnection> socialUserConnections = socialUserConnectionRepository.findAllByUserIdAndProviderIdOrderByRankAsc(user.getLogin(), ReadWordConstants.Wxma);
            if (socialUserConnections.size() > 0) {
                SocialUserConnection socialUserConnection = socialUserConnections.get(0);
                socialUserConnection.setDisplayName(userInfo.getNickName());
                socialUserConnection.setImageURL(userInfo.getAvatarUrl());
                socialUserConnectionRepository.save(socialUserConnection);
            }
        });

    }

    private UserDetails createUserByOpenId(String openId) {
        try {
            String nickName = ReadWordConstants.DEFAULT_NICKNAME_PRE + org.apache.commons.lang3.RandomStringUtils.random(5, true, true);
            UserProfile userProfile = new UserProfile(openId, nickName, nickName, nickName, openId + "@qq.com", openId);
            User user = socialService.createUserIfNotExist(userProfile, Constants.DEFAULT_LANGUAGE, ReadWordConstants.Wxma, ReadWordConstants.DEFAULT_PROFILE_IMAGE);
            SocialUserConnection wxMaConnection = new SocialUserConnection();
            wxMaConnection.setRank(1L);
            wxMaConnection.setProviderId(ReadWordConstants.Wxma);
            wxMaConnection.setProviderUserId(openId);
            wxMaConnection.setDisplayName(nickName);
            wxMaConnection.setImageURL(ReadWordConstants.DEFAULT_PROFILE_IMAGE);
            wxMaConnection.setUserId(user.getLogin());
            wxMaConnection.setAccessToken(openId);
            socialUserConnectionRepository.save(wxMaConnection);
            return userDetailsService.loadUserByUsername(openId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }

    }

}
