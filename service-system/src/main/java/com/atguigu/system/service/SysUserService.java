package com.atguigu.system.service;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    Page<SysUser> pages(Page<SysUser> sysUserPage, SysUserQueryVo sysUserQueryVo);

    boolean updateStatus(Long id, Integer status);

//    Result selectUser(String username, String password);

    SysUser selectUser(String username);

    Map<String, Object> getMenusforUser(Long id);

    List<String> getButtons(Long id);
}
