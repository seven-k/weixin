package com.ycl.weixin.weixinweb;

import com.ycl.weixin.weixinweb.filter.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.Filter;

@SpringBootApplication
@ComponentScan(basePackages = "com.ycl.weixin.weixinweb.*")
public class WeixinwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinwebApplication.class, args);
    }


    @Bean
    public Filter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public FilterRegistrationBean authFilterReg() {
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(authFilter());
        filterBean.addUrlPatterns("/api/*");
        filterBean.setName("auth-Filter");
        filterBean.setOrder(0);
        return filterBean;
    }
}
