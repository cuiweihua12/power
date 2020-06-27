package com.mr.cwh.system.interceptor;

import com.mr.cwh.system.condition.UserCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: power
 * @description: 拦截器
 * @author: cuiweihua
 * @create: 2020-06-21 10:36
 */
public class InterceptorSystem implements HandlerInterceptor {

    private List<String> excludedUrls;

    private Logger logger = LoggerFactory.getLogger(InterceptorSystem.class);

    public List<String> getExcludedUrls() {
        return excludedUrls;
    }

    public void setExcludedUrls(List<String> excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取访问路径
        String uri = request.getRequestURI();
        //跳过允许的访问
        for (String url : excludedUrls) {
            if (uri.contains(url)){
                return true;
            }
        }
        //获取sessiong
        UserCondition user = (UserCondition) request.getSession().getAttribute("user");
        if (user != null){
            if (!user.getPowers().isEmpty()) {
                for (String s : user.getPowers()) {
                    if (s.contains(uri+":"+request.getMethod())) {
                        return true;
                    }
                }
            }
        }
        logger.error("没有此权限:"+uri+":"+request.getMethod());
        //得到请求的类型
        String type = request.getHeader("X-Requested-with");
        if("XMLHttpRequest".equalsIgnoreCase(type)){
            response.sendError(403);
        }else{
            response.sendRedirect(request.getContextPath()+"/page/login.jsp");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);
            response.setHeader("Pragma", "on-cache");
        }
        return false;
    }
}
