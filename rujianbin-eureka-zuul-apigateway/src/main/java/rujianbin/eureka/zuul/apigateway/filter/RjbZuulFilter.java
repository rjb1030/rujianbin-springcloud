package rujianbin.eureka.zuul.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import rujianbin.common.utils.RjbStringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rujianbin on 2018/2/12.
 */
public class RjbZuulFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(RjbZuulFilter.class);

    @Override
    public String filterType() {
        /**
         * pre 表示请求被路由之前调用
         * routing 路由请求转发时调用
         * post 在routing和error过滤器之后被调用（此时已取得服务实例的返回信息）
         * error 处理请求发送错误时被调用（最终流向还是post阶段，因为需要post过滤器将请求返回）
         */
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        /**
         * 过滤器执行顺序，数字越小优先级越高
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

        try {
            HttpServletRequest request = ctx.getRequest();
            logger.info("zuul过滤器执行  method={},url={},param={}",request.getMethod(),request.getRequestURL().toString(), RjbStringUtils.ObjectToString(request.getParameterMap()));
//        ctx.setResponseStatusCode(401);//如果验证不通过，可以设置错误码
        } catch (Exception e) {
            /**
             * 设置过error.status_code后异常才会被SendErrorFilter处理返回客户端
             * 1. 未配置error类型的filter时，所有其他filter要try-catch并设置异常，以便返回
             * 2. 配置了error类型过滤器（其他filter异常未catch时会进入error过滤器，ctx.getThrowable()可以获取异常），则可以在error过滤器中设置error.status_code，最终filter还是会流向post类型的SendErrorFilter
             * 3. 禁用默认过滤器zuul.<SimpleClassName>.<filterType>.disable=true
             */
            e.printStackTrace();
            ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ctx.set("error.exception",e);
            ctx.set("error.message","自定义异常信息说明");
        }
        return null;
    }
}
