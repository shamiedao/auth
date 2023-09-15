package com.atguigu.system.filter;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.ResponseUtil;
import com.atguigu.model.system.SysUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 认证解析token过滤器，通过解析请求头中的token实现用户认证的过滤器
 * </p>
 */
//通过解析请求头中的token实现用户认证的过滤器
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //拦截请求的方法
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求地址
        String requestURI = httpServletRequest.getRequestURI();
        //如果是登录的请求，直接放行
        if ("/admin/system/index/login".equals(requestURI)) {
            //证明用户在登录，放行请求
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        //获取认证之后的对象
        Authentication authResult = getAuthenticationAuthResult(httpServletRequest);
        //判断认证之后的对象是否为null
        if (authResult != null) {
            //将认证之后的对象放到上下文中
            SecurityContextHolder.getContext().setAuthentication(authResult);
            //放行请求
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            //没有获取到认证之后的对象，证明无权访问
            ResponseUtil.out(httpServletResponse, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    //获取认证之后的对象的方法
    private Authentication getAuthenticationAuthResult(HttpServletRequest httpServletRequest) {
        //获取请求头中的token
        String token = httpServletRequest.getHeader("token");
        if (token != null) {
            //从Redis中获取用户信息
            SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();
            if (sysUser != null) {
                //获取用户的按钮权限标识符
                List<String> userPermsList = sysUser.getUserPermsList();
                UsernamePasswordAuthenticationToken authResult = null;
                if (userPermsList != null && userPermsList.size() > 0) {
                    //创建一个保存用户权限的集合
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    //遍历按钮权限标识符
                    for (String perms : userPermsList) {
                        //添加用户权限
                        authorities.add(new SimpleGrantedAuthority(perms));
                    }
                    //创建一个认证之后的并且有操作权限的对象
                    authResult = new UsernamePasswordAuthenticationToken(sysUser, null, authorities);
                } else {
                    //创建一个认证之后的但是没有操作权限的对象
                    authResult = new UsernamePasswordAuthenticationToken(sysUser, null, Collections.emptyList());

                }
                return authResult;
            }
        }
        return null;
    }
}