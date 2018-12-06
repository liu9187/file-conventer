package com.minxing.fileconventer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    @Value( "${tmp.location}" )
    private String location;
    /**
     * 配置静态访问资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/conventer/**").addResourceLocations(location);
        super.addResourceHandlers(registry);
    }

}
