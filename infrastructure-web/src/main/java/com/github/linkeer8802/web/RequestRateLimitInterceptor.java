package com.github.linkeer8802.web;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: weird
 * @date: 2018/12/21
 */
public class RequestRateLimitInterceptor extends HandlerInterceptorAdapter {


    String PREFIX_REQ_LIMIT = "request-limit-";
    String KEY_VALIDATE_CODE = "request-limit-validate-code";

    @Resource
    RateLimit rateLimit;
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String code = (String) request.getSession().getAttribute(KEY_VALIDATE_CODE);
        if (code != null) {
            if (code.equals(request.getParameter(KEY_VALIDATE_CODE))) {
                request.getSession().removeAttribute(KEY_VALIDATE_CODE);
            } else {
                //跳转到输入验证页面
                response.getWriter().append("Please input CAPTCHA");
                response.getWriter().flush();
                response.getWriter().close();
                return false;
            }
        }
        String sid = request.getSession().getId();
        if (!rateLimit.requestRateLimit(PREFIX_REQ_LIMIT + sid, 2L, 100)) {
            System.out.println("Req limit..........");
            //跳转到输入验证页面
            request.getSession().setAttribute(KEY_VALIDATE_CODE, "1314");
        }
        return super.preHandle(request, response, handler);
    }
}
