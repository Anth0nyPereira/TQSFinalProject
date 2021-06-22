package tqs.proudpapers.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tqs.proudpapers.component.LoginHandlerInterceptor;

/**
 * @author wy
 * @date 2021/6/14 19:44
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/account/**");
    }

}
