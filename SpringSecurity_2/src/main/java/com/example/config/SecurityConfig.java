package com.example.config;

import com.example.filter.JwtAuthenticationTokenFilter;
import com.example.handler.AccessDeniedHandlerImpl;
import com.example.handler.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    @Override
    //获取一个AuthenticationManager的bean实例,
    //AuthenticationManager是负责处理用户认证的核心组件,
    //此处: 是为了在Spring容器中返回注入 对象,以供登录请求获取 校验登录成功\失败
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    //这个方法是Spring Security配置中的关键部分
    //用于定制Web安全策略,详细指定哪些URL路径需要认证、哪些可以公开访问、以及认证失败或成功后的处理逻辑等
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // 对于登录接口 允许匿名访问, 因为登录接口在鉴权之前, 不登录则不能鉴权 必须放行)
                .antMatchers("/user/login").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 把token检验过滤器添加到过滤器连中
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器：
        http.exceptionHandling()
                // 授权失败处理器
                .accessDeniedHandler(accessDeniedHandler)
                // 认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint);

        // 开启跨域
        http.cors();
    }

}
