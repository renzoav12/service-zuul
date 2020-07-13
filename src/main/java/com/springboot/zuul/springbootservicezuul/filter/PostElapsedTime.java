package com.springboot.zuul.springbootservicezuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostElapsedTime extends ZuulFilter {

  private static Logger log = LoggerFactory.getLogger(PostElapsedTime.class);

  @Override
  public String filterType() {
    return "post";
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();

    log.info("get in post");
    
    Long initTime = (Long)request.getAttribute("initTime");
    Long endTime = System.currentTimeMillis();
    Long timeTotal = endTime - initTime;

    log.info(String.format("Tiempo transcurrido en %s segundos.", timeTotal.doubleValue()/1000.00));
    log.info(String.format("Tiempo transcurrido en %s milisegundos", timeTotal));
    return null;
  }
}
