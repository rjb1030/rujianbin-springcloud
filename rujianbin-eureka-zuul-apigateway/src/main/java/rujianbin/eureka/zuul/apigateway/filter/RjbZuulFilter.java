package rujianbin.eureka.zuul.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rujianbin.common.utils.RjbStringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rujianbin on 2018/2/12.
 */
public class RjbZuulFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(RjbZuulFilter.class);

    @Override
    public String filterType() {
        /**
         * pre表示路由前执行
         */
        return "pre";
    }

    @Override
    public int filterOrder() {
        /**
         * 过滤器执行顺序
         */
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        /**
         * 判断过滤器是否需要执行
         */
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("zuul过滤器执行  method={},url={},param={}",request.getMethod(),request.getRequestURL().toString(), RjbStringUtils.ObjectToString(request.getParameterMap()));
        return null;
    }
}
