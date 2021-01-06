package com.example.hospital.config;

import com.example.hospital.component.JwtAuthenticationTokenFilter;
import com.example.hospital.component.RestAuthenticationEntryPoint;
import com.example.hospital.component.RestfulAccessDeniedHandler;
import com.example.hospital.dto.CustomUserDetailSevice;
import com.example.hospital.service.Impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;
    private final RestAuthenticationEntryPoint entryPoint;
    private final RestfulAccessDeniedHandler handler;

    public SecurityConfiguration(UserServiceImpl userService, RestAuthenticationEntryPoint entryPoint,
                                 RestfulAccessDeniedHandler handler) {
        this.userService = userService;
        this.entryPoint = entryPoint;
        this.handler = handler;
    }

    /**
     * AuthenticationManagerBuilder：用来配置全局的认证相关的信息，
     * 其实就是AuthenticationProvider和UserDetailsService，前者是认证服务提供商，后者是用户详情查询服务；
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new CustomUserDetailSevice())
                .passwordEncoder(passwordEncoder());
    }

    /**
     * WebSecurity：
     * 全局请求忽略规则配置（比如说静态文件，比如说注册页面）、
     * 全局HttpFirewall配置、是否debug配置、全局SecurityFilterChain配置、
     * privilegeEvaluator、expressionHandler、securityInterceptor；
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * HttpSecurity：具体的权限控制规则配置。一个这个配置相当于xml配置中的一个标签。
     * 各种具体的认证机制的相关配置，OpenIDLoginConfigurer、AnonymousConfigurer、
     * FormLoginConfigurer、HttpBasicConfigurer等。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                        "/webjars/**"
                )
                .permitAll()
                .antMatchers("/login", "/register")// 对登录注册要允许匿名访问
                .permitAll()
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        // 禁用缓存
        http.logout().logoutUrl("someLogOutUrlToFix");
        // 添加JWT filter
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(handler)
                .authenticationEntryPoint(entryPoint);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}