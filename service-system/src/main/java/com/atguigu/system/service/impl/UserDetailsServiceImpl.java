package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysUser;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.selectUser(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        //判断用户是否被禁用
        if(sysUser.getStatus() == 0) {
            throw new RuntimeException("账号已停用");
        }
        //根据用户id获取用户的按钮权限标识符
        List<String> buttons = sysUserService.getButtons(sysUser.getId());
        //给用户设置权限
        sysUser.setUserPermsList(buttons);
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
