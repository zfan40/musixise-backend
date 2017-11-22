package musixise.aop.logging;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhaowei on 2017/11/15.
 */
public class LoggerInterceptor implements HandlerInterceptor
{
    //请求开始时间标识
    private static final String LOGGER_SEND_TIME = "_send_time";
    //请求日志实体标识
    private static final String LOGGER_ENTITY = "_logger_entity";
    private static final String CLIENT_IP = "_client_ip";
    private static final String METHOD = "_method";
    private static final String REQUEST_TYPE = "_request_type";
    private static final String PARAM_DATA = "_param_data";
    private static final String URL = "_url";
    private static final String SESSION_ID = "_session_id";

    private final Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);

    /**
     * 进入SpringMVC的Controller之前开始记录日志实体
     * @param request 请求对象
     * @param response 响应对象
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //获取请求sessionId
        String sessionId = request.getRequestedSessionId();
        //请求路径
        String url = request.getRequestURI();
        //获取请求参数信息
        String paramData = JSON.toJSONString(request.getParameterMap(),
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteMapNullValue);
        //设置客户端ip
        request.setAttribute(CLIENT_IP, getCliectIp(request));
        //设置请求方法
        request.setAttribute(METHOD, request.getMethod());
        //设置请求类型（json|普通请求）
        request.setAttribute(REQUEST_TYPE, getRequestType(request));
        //设置请求参数内容json字符串
        request.setAttribute(PARAM_DATA, paramData);
        //设置请求地址
        request.setAttribute(URL, url);
        //设置sessionId
        request.setAttribute(SESSION_ID, sessionId);
        //设置请求开始时间
        request.setAttribute(LOGGER_SEND_TIME,System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        //获取请求错误码
        int status = response.getStatus();
        //当前时间
        long currentTime = System.currentTimeMillis();
        //请求开始时间
        long time = Long.valueOf(request.getAttribute(LOGGER_SEND_TIME).toString());

        log.info("REST request {} ||| {} ||| {} ||| {} ||| {} ||| {} ||| {} ||| {} ||| {} ||| {} ||| {} || {}",
            request.getAttribute(CLIENT_IP), request.getAttribute(METHOD), request.getAttribute(REQUEST_TYPE),
            request.getAttribute(PARAM_DATA), request.getAttribute(URL), request.getAttribute(SESSION_ID),
            request.getAttribute(LOGGER_SEND_TIME), Integer.valueOf((currentTime - time)+""), status,
            request.getHeader("Authorization"), request.getHeader("Referer"),
            request.getHeader("user-agent"));
    }
    /**
     * 根据传入的类型获取spring管理的对应dao
     * @param clazz 类型
     * @param request 请求对象
     * @param <T>
     * @return
     */
    private <T> T getDAO(Class<T> clazz,HttpServletRequest request)
    {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }


    private static String getCliectIp(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    /**
     * 判断是否为ajax请求
     * @param request
     * @return
     */
    public static String getRequestType(HttpServletRequest request) {
        return request.getHeader("X-Requested-With");
    }
}

