package com.atguigu.system.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    IPage<SysRole> findPage(Page<SysRole> sysRolePage, SysRoleQueryVo sysRoleQueryVo);

    Map<String, Object> getRoles(Long id);

    void updateRoles(AssignRoleVo assignRoleVo);
}
