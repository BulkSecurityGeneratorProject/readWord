package com.qigu.readword.mycode.vipsecurity;

import com.qigu.readword.domain.User;
import com.qigu.readword.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class VipFilter extends GenericFilterBean {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private final UserService userService;

    public VipFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        log.info("VipFilter--getRequestURI-->{}", requestURI);
        if (requestURI.contains("/api/words") || requestURI.contains("/api/_search/words")) {
            Optional<User> userOptional = userService.getUserWithAuthorities();
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Boolean vipExpired = user.isVipExpired();
                if (vipExpired) {
                    log.info("requestURI->{}->User:{} is Expired!", requestURI, user.getFirstName());
                    return;
                }
            }
        }

        /*String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }*/
        filterChain.doFilter(servletRequest, servletResponse);
    }


}
